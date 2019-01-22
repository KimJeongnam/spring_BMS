package com.spring.bookstore.member.persistance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.bookstore.member.vo.Cart;
import com.spring.bookstore.member.vo.Member;
import com.spring.bookstore.member.vo.User;

@Repository
public class MemberDAOImpl implements MemberDAO{
	@Autowired
	SqlSession sqlSession;
	
	
	@Override
	public Member getPwd(String user_id) {
		return sqlSession.selectOne("com.spring.bookstore.member.persistance.MemberDAO.getPwd", user_id);
	}


	@Override
	public int loginDo(String id, String pw) {
		int result = 0;
		
		Member member = getPwd(id);
		
		if(member != null) {
			result = -1;
			if(member.getUser_pw().equals(pw)) {
				result = 1;
				if(member.getAccept_status() == 0)
					result = 2;
			}
		}
		return result;
	}


	@Override
	public int checkId(String id) {
		return sqlSession.selectOne("com.spring.bookstore.member.persistance.MemberDAO.checkId", id);
	}

	@Override
	public int signUpDo(User newUser) {
		return sqlSession.insert("com.spring.bookstore.member.persistance.MemberDAO.signUpDo", newUser);
	}


	@Override
	public int emailCheck(String accept_code) {
		return sqlSession.update("com.spring.bookstore.member.persistance.MemberDAO.emailCheck", accept_code);
	}


	@Override
	public List<Cart> cartList(String user_id) {
		return sqlSession.selectList("com.spring.bookstore.member.persistance.MemberDAO.cartList", user_id);
	}


	@Override
	public int cartAddDo(Map<String, Object> map) {
		return sqlSession.insert("com.spring.bookstore.member.persistance.MemberDAO.cartAddDo", map);
	}


	@Override
	public int cartTotalPrice(String user_id) {
		return sqlSession.selectOne("com.spring.bookstore.member.persistance.MemberDAO.cartTotalPrice", user_id);
	}
	
	@Override
	public int cartDelDo(Map<String, Object> map) {
		return sqlSession.delete("com.spring.bookstore.member.persistance.MemberDAO.cartDelDo", map);
	}


	@Override
	public int cartsDelDo(String user_id, String... book_code) {
		int cnt = 0;
		
		for(String code: book_code) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", user_id);
			map.put("code", code);
			if(cartDelDo(map)==0) {
				return 0;
			}
			cnt++;
		}
		return cnt;
	}
	
	
/*
	@Override
	public int cartCheck(String user_id, int book_code) throws SQLException {
		init();
		
		sql = "SELECT COUNT(*) as cnt FROM carts WHERE user_id=? AND book_code=?";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, user_id);
		pstmt.setInt(2, book_code);
		
		rs = pstmt.executeQuery();
		
		if(rs.next())
			result = rs.getInt("cnt");
		
		close();
		return result;
	}

	@Override
	public int cartDelDo(String user_id, int book_code) throws SQLException {
		init();
		sql = "DELETE FROM carts WHERE user_id=? AND book_code=?";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, user_id);
		pstmt.setInt(2, book_code);
		
		result = pstmt.executeUpdate();
		
		close();
		return result;
	}

	@Override
	public int cartsDelDo(String user_id, String[] book_codes) throws SQLException {
		setSavePoint("savePoint1");
		
		for(String code: book_codes) {
			if(cartDelDo(user_id, Integer.parseInt(code))==0) {
				rollback("savePoint1");
				commit();
				close();
				return 0;
			}
		}
		commit();
		close();
		return 1;
	}

	@Override
	public int buy(String user_id, ArrayList<Map<String, Integer>> orders) throws SQLException {
		setSavePoint("savePoint1");
		String uuid = "";
		
		while(result==0) {
			sql = "INSERT INTO orders(\n" + 
					"    order_code\n" + 
					"    , user_id\n" + 
					"    , status\n" + 
					"    , order_date\n" + 
					"    , total_price)\n" + 
					"    VALUES(?, ? , ? , ?, 0)";
			
			pstmt = conn.prepareStatement(sql);
			
			uuid = UUID.randomUUID().toString();
			
			pstmt.setString(1, uuid);
			pstmt.setString(2, user_id);
			pstmt.setString(3, OrderStatus.BUY_ASK);
			pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			
			result = pstmt.executeUpdate();
		}
		setSavePoint("savePoint2");
		
		for(Map<String, Integer> cart : orders) {
			sql = "INSERT INTO order_info(\n" + 
					"    order_code\n" + 
					"    , book_code\n" + 
					"    , buy_stock)\n" + 
					"    VALUES(?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uuid);
			pstmt.setInt(2, cart.get("book_code"));
			pstmt.setInt(3, cart.get("wish_stock"));
			
			if((result = pstmt.executeUpdate())==0) {
				rollback("savePoint1");
			}
		}
		
		commit();
		close();
		return result;
	}

	@Override
	public ArrayList<Order> orderList(String user_id) throws SQLException {
		ArrayList<Order> dtos = null;
		
		init();
		
		sql = "SELECT order_code\n" + 
				"    , order_date\n" + 
				"    , (SELECT COUNT(*) FROM order_info WHERE order_code=o.order_code) as cnt\n" + 
				"    , total_price\n" + 
				"    , status\n" + 
				"    FROM orders o \n" + 
				"    WHERE user_id=?\n" + 
				"    group by order_code, order_date, total_price, status\n" + 
				"    ORDER BY o.order_date DESC";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, user_id);
		
		rs = pstmt.executeQuery();
		
		if(rs.next()) {
			dtos = new ArrayList<Order>();
			do {
				Order dto = new Order();
				dto.setOrder_code(rs.getString("order_code"));
				dto.setOrder_date(rs.getTimestamp("order_date"));
				dto.setOrder_cnt(rs.getInt("cnt"));
				dto.setTotal_price(rs.getInt("total_price"));
				dto.setStatus(OrderStatus.codeToStatus(rs.getString("status")));
				dtos.add(dto);
			}while(rs.next());
		}
		
		close();
		return dtos;
	}

	@Override
	public ArrayList<OrderInfo> orderInfo(String order_code) throws SQLException {
		init();
		ArrayList<OrderInfo> dtos = null;
		
		sql = "SELECT oi.order_code\n" + 
				"    , bb.board_id\n" + 
				"    , oi.book_code\n" + 
				"    , oi.buy_stock\n" + 
				"    , b.title\n" + 
				"    , b.author\n" + 
				"    , b.price\n" + 
				"    , b.image_path\n" + 
				"    , b.price*oi.buy_stock total_price\n" + 
				"    FROM order_info oi JOIN books b\n" + 
				"    ON oi.book_code = b.book_code\n" + 
				"    JOIN book_board bb\n" + 
				"    ON b.book_code = bb.book_code\n" + 
				"    WHERE oi.order_code = ?";	
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, order_code);
		
		rs = pstmt.executeQuery();
		
		if(rs.next()) {
			dtos = new ArrayList<OrderInfo>();
			do {
				OrderInfo dto = new OrderInfo();
				dto.setOrder_code(rs.getString("order_code"));
				dto.setTitle(rs.getString("title"));
				dto.setBoard_id(rs.getInt("board_id"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBuy_stock(rs.getInt("buy_stock"));
				dto.setImage_path(rs.getString("image_path"));
				dto.setPrice(rs.getInt("price"));
				dto.setTotal_price(rs.getInt("total_price"));
				dtos.add(dto);
			}while(rs.next());
		}
		
		close();
		return dtos;
	}

	@Override
	public int refundAskDo(String order_code) throws SQLException {
		init();
		
		sql = "UPDATE orders SET\n" + 
				"    status='REFUND_ASK'\n" + 
				"    WHERE order_code = ?";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, order_code);
		
		result = pstmt.executeUpdate();
		
		close();
		return result;
	}*/

}
