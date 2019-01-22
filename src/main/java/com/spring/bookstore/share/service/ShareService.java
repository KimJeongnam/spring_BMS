package com.spring.bookstore.share.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.spring.bookstore.share.vo.Category;

public interface ShareService {
	public void mainDo(HttpServletRequest request, Model model);
	public List<Category> getCategorys(HttpServletRequest request, Model model);
	public void bookList(HttpServletRequest request, Model model);
	public void bookInfo(HttpServletRequest request, Model model);
}
