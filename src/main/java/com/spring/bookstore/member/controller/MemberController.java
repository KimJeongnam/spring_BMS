package com.spring.bookstore.member.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.bookstore.member.service.MemberService;

@Controller
public class MemberController {
	@Autowired
	MemberService service;

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@RequestMapping("login")
	public String login(HttpServletRequest request, RedirectAttributes model) throws Exception {
		logger.info("login()");

		service.loginDo(request, model);

		String referer = request.getHeader("referer");
		return "redirect:" + referer;
	}

	@RequestMapping("logout")
	public String logout(HttpServletRequest request, RedirectAttributes model) throws Exception {
		logger.info("logout()");

		service.logoutDo(request, model);

		String referer = request.getHeader("referer");
		return "redirect:" + referer;
	}

	@RequestMapping("signUpForm")
	public String signUpForm() throws Exception {
		logger.info("signUpForm()");

		return "signup";
	}

	@RequestMapping("checkId")
	public String checkId(HttpServletRequest request, Model model) throws Exception {
		logger.info("checkId()");

		service.checkId(request, model);

		return "checkId";
	}

	@RequestMapping("signUpDo")
	public String signUpDo(HttpServletRequest request, Model model) throws Exception {
		logger.info("signUpDo()");

		service.signUpDo(request, model);

		return "signUpDo";
	}

	@RequestMapping("emailChk")
	public String emailChk(HttpServletRequest request, Model model) throws Exception {
		logger.info("emailChk()");

		service.emailCheck(request, model);

		return "signUpDo";
	}

	@RequestMapping("member/cartAddDo")
	public String cartAddDo(HttpServletRequest request, RedirectAttributes model) {

		service.cartAddDo(request, model);

		String referer = request.getHeader("referer");
		return "redirect:" + referer;
	}

	@RequestMapping("member/cartList")
	public String cartList(HttpServletRequest request, Model model) {

		service.cartList(request, model);

		return "cartList";
	}

	@RequestMapping("member/cartDel")
	public String cartDel(HttpServletRequest request, RedirectAttributes model) {

		service.cartDel(request, model);
		
		String referer = request.getHeader("referer");
		return "redirect:" + referer;
	}

	@RequestMapping("member/cartsDel")
	public String carsDels(HttpServletRequest request, RedirectAttributes model) {
		
		service.cartsDel(request, model);
		
		String referer = request.getHeader("referer");
		return "redirect:" + referer;
	}

	 /* @RequestMapping("member/nowBuy")
	 * 
	 * @RequestMapping("member/cartsBuy")
	 * 
	 * @RequestMapping("member/orders")
	 * 
	 * @RequestMapping("member/orderInfo")
	 * 
	 * @RequestMapping("member/refundAsk")
	 */
	
	/*switch(url) {
	case "/cartAddDo":
		if(!sessionCheck(request, response)) return; 
		
		service.runMemberService(request, response, Code.CART_ADD_DO);
		
		return;
	case "/cartList":
		if(!sessionCheck(request, response)) return;
		
		service.runMemberService(request, response, Code.CART_LIST);

		viewPage = "/view/cartList.jsp";
		break;
	case "/cartDel":
		if(!sessionCheck(request, response)) return;
		
		service.runMemberService(request, response, Code.CART_DEL_DO);
		
		return;
	case "/cartsDel":
		if(!sessionCheck(request, response)) return; 
		
		service.runMemberService(request, response, Code.CARTS_DEL_DO);
		
		return;
	case "/nowBuy":
		if(!sessionCheck(request, response)) return; 
		
		service.runMemberService(request, response, Code.NOW_BUY);
		
		return;
	case "/cartsBuy":
		if(!sessionCheck(request, response)) return; 
		
		service.runMemberService(request, response, Code.CART_ITEMS_BUY);
		
		return;
	case "/orders":
		if(!sessionCheck(request, response)) return; 
		
		service.runMemberService(request, response, Code.MEMBER_ORDER_LIST);
		
		viewPage = "/view/orderStatus.jsp";
		break;
	case "/orderInfo":
		if(!sessionCheck(request, response)) return; 
		
		service.runMemberService(request, response, Code.MEMBER_ORDER_INFO); 
		
		viewPage = "/view/orderInfo.jsp";
		break;
	case "/refundAsk":
		if(!sessionCheck(request, response)) return; 
		
		service.runMemberService(request, response, Code.MEMBER_REFUND_ASK);
		
		return;
	}*/
}
