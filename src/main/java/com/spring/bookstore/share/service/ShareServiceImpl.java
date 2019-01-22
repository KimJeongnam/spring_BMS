package com.spring.bookstore.share.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.spring.bookstore.share.persistance.ShareDAO;
import com.spring.bookstore.share.vo.BoardVO;
import com.spring.bookstore.share.vo.Category;
import com.spring.bookstore.share.vo.SearchVO;

@Service
public class ShareServiceImpl implements ShareService {
	@Autowired
	ShareDAO dao;

	@Override
	public void mainDo(HttpServletRequest request, Model model) {
		List<BoardVO> newBookList = null;
		List<BoardVO> hotBookList = null;

		getCategorys(request, model);

		newBookList = dao.getNewBooks();
		hotBookList = dao.getHotBooks();

		model.addAttribute("hotBooks", hotBookList);
		model.addAttribute("newBooks", newBookList);
	}

	@Override
	public List<Category> getCategorys(HttpServletRequest request, Model model) {
		List<Category> categorys = null;
		List<Category> parentCategorys = new ArrayList<Category>();
		List<Category> childCategorys = new ArrayList<Category>();

		categorys = dao.getCategorys();

		for (Category category : categorys) {
			if (category.getLevel() == 1) {
				parentCategorys.add(category);
			} else
				childCategorys.add(category);
		}
		model.addAttribute("categorys", categorys);
		model.addAttribute("parentCategorys", parentCategorys);
		model.addAttribute("childCategorys", childCategorys);
		return categorys;
	}

	@Override
	public void bookList(HttpServletRequest request, Model model) {
		List<Category> categorys = null;
		Category p_category = null;
		Category c_category = null;
		boolean categoryIdck = false;

		int pageSize = 15; // 한 페이지당 출력할 글 갯수
		int pageBlock = 5; // 한블럭당 페이지 갯수

		int cnt = 0; // 총 글 갯수
		int start = 0; // 현재 페이지 시작 글번호
		int end = 0; // 현재 페이지 마지막 글 번호
		int number = 0; // 출력용 글번호
		String pageNum = ""; // 페이지 번호
		String category_id = null; // 구분
		int currentPage = 0; // 현재 페이지
		int pageCount = 0; // 페이지 갯수
		int startPage = 0; // 시작 페이지
		int endPage = 0; // 마지막 페이지

		category_id = request.getParameter("category_id");
		categorys = getCategorys(request, model);

		/* ============ 부모 자식 카테고리 START ============== */
		if (category_id != null) {
			if (category_id.trim().length() != 0) {
				categoryIdck = true;
			}
		}

		SearchVO search = new SearchVO();
		if (categoryIdck) {
			for (Category category : categorys) {
				if (category.getCategory_id() == Integer.parseInt(category_id))
					c_category = category;
			}
			for (Category category : categorys) {
				if (category.getCategory_id() == c_category.getSuper_id())
					p_category = category;
			}
			model.addAttribute("p_category", p_category);
			model.addAttribute("c_category", c_category);
			/* ============ 부모 자식 카테고리 END ============== */

			search.setCategory_id(Integer.parseInt(category_id));
			cnt = dao.getTotalBoardCnt(search);
		} else
			cnt = dao.getTotalBoardCnt(search);

		System.out.println("글 갯수 : " + cnt);

		pageNum = request.getParameter("pageNum");

		if (pageNum == null)
			pageNum = "1";

		currentPage = Integer.parseInt(pageNum);

		// 글 30건 기준
		currentPage = Integer.parseInt(pageNum);
		System.out.println("currentPage : " + currentPage);

		// 페이지 갯수 6 = (30 / 5) + (0) 나머지가 0보다크면 1페이지 증가
		pageCount = cnt / pageSize + (cnt % pageSize > 0 ? 1 : 0);

		// 현재 페이지 시작 글 번호 1 (페이지별)
		// 1 = (1 - 1)*5 + 1
		start = (currentPage - 1) * pageSize + 1;

		// 현재 페이지 마지막 글 번호(페이지별)
		// 5 = 1 + 5 -1;
		end = start + pageSize - 1;

		System.out.println("start  : " + start);
		System.out.println("end  : " + end);
		if (end > cnt)
			end = cnt;

		// 출력용 글번호
		// 30 = 30 - (1 - 1) * 5
		number = cnt - (currentPage - 1) * pageSize; // 출력용 글번호
		System.out.println("number : " + number);
		System.out.println("pageSize : " + pageSize);

		if (cnt > 0) {
			// 5-2 게시글 목록 조회
			search = null;
			List<BoardVO> dtos = null;
			if (categoryIdck) {
				search = new SearchVO(start, end, Integer.parseInt(category_id));

			} else
				search = new SearchVO(start, end);

			dtos = dao.getBoardList(search);
			model.addAttribute("dtos", dtos);
		}

		// 6단계 request나 session 객체에 처리 결과를 저장 (jsp에 전달하기 위함)
		// 1 = (1 / 3) * 3 + 1;
		startPage = (currentPage / pageBlock) * pageBlock + 1;

		if (currentPage % pageBlock == 0)
			startPage -= pageBlock;
		System.out.println("StartPage : " + startPage);

		// 3 = 1 + 3 - 1;
		endPage = startPage + pageBlock - 1;
		if (endPage > pageCount)
			endPage = pageCount;
		System.out.println("endPage : " + endPage);
		System.out.println("==================");

		model.addAttribute("cnt", cnt);
		model.addAttribute("number", number);
		model.addAttribute("pageNum", pageNum);

		if (cnt > 0) {
			model.addAttribute("startPage", startPage); // 시작 페이지
			model.addAttribute("endPage", endPage); // 마지막 페이지
			model.addAttribute("pageBlock", pageBlock); // 출력할 페이지 갯수
			model.addAttribute("pageCount", pageCount); // 페이지 갯수
			model.addAttribute("currentPage", currentPage); // 현재 페이지
			model.addAttribute("pageSize", pageSize);
		}
	}

	@Override
	public void bookInfo(HttpServletRequest request, Model model) {
		getCategorys(request, model);
		System.out.println("request URI : " + request.getRequestURI());

		String category_id = request.getParameter("category_id");

		int board_id = Integer.parseInt(request.getParameter("board_id"));
		int pageNum = 1;

		if (request.getParameter("pageNum") != null)
			pageNum = Integer.parseInt(request.getParameter("pageNum"));

		if (category_id != null)
			if (category_id.length() != 0)
				model.addAttribute("category_id", Integer.parseInt(category_id));

		BoardVO dto = dao.getBookInfo(board_id);
		model.addAttribute("dto", dto);

		model.addAttribute("pageNum", pageNum);
	}
}
