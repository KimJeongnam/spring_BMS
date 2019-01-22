package com.spring.bookstore.share.persistance;

import java.sql.SQLException;
import java.util.List;

import com.spring.bookstore.share.vo.BoardVO;
import com.spring.bookstore.share.vo.Category;
import com.spring.bookstore.share.vo.SearchVO;

public interface ShareDAO {
	public List<Category> getCategorys();
	public int getTotalBoardCnt(SearchVO search);
	public List<BoardVO> getBoardList(SearchVO search);
	public BoardVO getBookInfo(int board_id);
	public List<BoardVO> getHotBooks();
	public List<BoardVO> getNewBooks();
}
