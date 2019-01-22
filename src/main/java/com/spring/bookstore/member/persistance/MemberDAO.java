package com.spring.bookstore.member.persistance;

import java.util.List;
import java.util.Map;

import com.spring.bookstore.member.vo.Cart;
import com.spring.bookstore.member.vo.Member;
import com.spring.bookstore.member.vo.User;

public interface MemberDAO {
	public Member getPwd(String user_id);
	public int loginDo(String id, String pw);
	public int checkId(String id);
	public int signUpDo(User newUser);
	public int emailCheck(String accept_code);
	public List<Cart> cartList(String user_id);
	public int cartAddDo(Map<String, Object> map);
	public int cartTotalPrice(String user_id);
	public int cartDelDo(Map<String, Object> map);
	public int cartsDelDo(String user_id, String ...book_code);
	
	/*public abstract int cartCheck(String user_id, int book_code) throws SQLException;
	public abstract int cartDelDo(String user_id, int book_code) throws SQLException;
	public abstract int cartsDelDo(String user_id, String[] book_codes) throws SQLException;
	public abstract int buy(String user_id, ArrayList<Map<String, Integer>> orders)throws SQLException;
	public abstract ArrayList<Order> orderList(String user_id) throws SQLException;
	public abstract ArrayList<OrderInfo> orderInfo(String order_code) throws SQLException;
	public abstract int refundAskDo(String order_code) throws SQLException;*/
}
