package com.spring.bookstore.share.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.bookstore.share.service.ShareService;

@Controller
public class ShareController {
	@Autowired
	ShareService service;
	
	private static final Logger logger = LoggerFactory.getLogger(ShareController.class);
	
	@RequestMapping("index")
	public String index(HttpServletRequest request, Model model) throws Exception {
		logger.info("index()");
		
		service.mainDo(request, model);
		
		return "index";
	}
	
	@RequestMapping("bookList")
	public String bookList(HttpServletRequest request, Model model) throws Exception {
		logger.info("bookList()");
		
		service.bookList(request, model);
		
		return "bookList";
	}
	
	@RequestMapping("bookInfo")
	public String bookInfo(HttpServletRequest request, Model model) throws Exception {
		logger.info("bookInfo()");
		
		service.bookInfo(request, model);
		
		return "bookInfo";
	}
	
	@RequestMapping("/loginForm")
	public String loginForm(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		redirectAttributes.addFlashAttribute("openLogin", "open!");
		
		return "redirect:index";
	}
	
	@RequestMapping("/loginFail")
	public String loginFail(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		redirectAttributes.addFlashAttribute("openLogin", true);
		redirectAttributes.addFlashAttribute("message", "id 혹은 비밀번호를 확인하세요.");
		
		String referer = request.getHeader("referer");
		return "redirect:" + referer;
	}
	
	@RequestMapping("/permission/denied")
	public String permissionDenied(RedirectAttributes redirectAttributes) {
		
		redirectAttributes.addFlashAttribute("openLogin", true);
		
		return "redirect: index";
	}
	
}
