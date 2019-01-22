package com.spring.bookstore.share.persistance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.bookstore.share.vo.BoardVO;
import com.spring.bookstore.share.vo.Category;
import com.spring.bookstore.share.vo.SearchVO;

@Repository
public class ShareDAOImpl implements ShareDAO{
	@Autowired
	SqlSession sqlSession;

	@Override
	public List<Category> getCategorys() {
		return sqlSession.selectList("com.spring.bookstore.share.persistance.ShareDAO.getCategorys");
	}

	@Override
	public int getTotalBoardCnt(SearchVO search) {
		return sqlSession.selectOne("com.spring.bookstore.share.persistance.ShareDAO.getTotalBoardCnt", search);
	}

	@Override
	public List<BoardVO> getBoardList(SearchVO search) {
		return sqlSession.selectList("com.spring.bookstore.share.persistance.ShareDAO.getBoardList", search);
	}

	@Override
	public BoardVO getBookInfo(int board_id) {
		return sqlSession.selectOne("com.spring.bookstore.share.persistance.ShareDAO.getBookInfo", board_id);
	}

	@Override
	public List<BoardVO> getHotBooks(){
		SearchVO search = new SearchVO(1, 15);
		search.setHot(true);
		return getBoardList(search);
	}

	@Override
	public List<BoardVO> getNewBooks() {
		SearchVO search = new SearchVO(1, 15);
		return getBoardList(search);
	}
}
