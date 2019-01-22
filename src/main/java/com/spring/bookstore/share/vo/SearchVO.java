package com.spring.bookstore.share.vo;

public class SearchVO {
	private String target;
	private String keyword;
	private int category_id;
	private int start;
	private int end;
	private int board_id;
	private boolean hot;
	
	public SearchVO() {}
	
	public SearchVO(int start, int end){
		this.start = start;
		this.end = end;
		this.hot = false;
		this.category_id = 0;
	}
	
	public SearchVO(int start, int end, int category_id) {
		this(start, end);
		this.category_id = category_id;
	}
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public int getBoard_id() {
		return board_id;
	}

	public void setBoard_id(int board_id) {
		this.board_id = board_id;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public boolean isHot() {
		return hot;
	}

	public void setHot(boolean hot) {
		this.hot = hot;
	}
}
