package com.spring.bookstore.member.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface MemberService {
	public void loginDo(HttpServletRequest request, RedirectAttributes model);
	public void logoutDo(HttpServletRequest request, RedirectAttributes model);
	public void checkId(HttpServletRequest request, Model model);
	public void signUpDo(HttpServletRequest request, Model model);
	public void emailCheck(HttpServletRequest request, Model model);
	public void cartAddDo(HttpServletRequest request, RedirectAttributes model);
	public void cartList(HttpServletRequest request, Model model);
	public void cartDel(HttpServletRequest request, RedirectAttributes model);
	public void cartsDel(HttpServletRequest request, RedirectAttributes model);
	/*switch(code) {
	case Code.CART_DEL_DO:
		service = new CartDelDo(); break;
	case Code.CARTS_DEL_DO:
		service = new CartsDelDo(); break;
	case Code.NOW_BUY:
		service = new NowBuyDo(); break;
	case Code.CART_ITEMS_BUY:
		service = new CartsBuy(); break;
	case Code.MEMBER_ORDER_LIST:
		service = new OrderList(); break;
	case Code.MEMBER_ORDER_INFO:
		service = new OrderInfo(); break;
	case Code.MEMBER_REFUND_ASK:
		service = new RefundAskDo(); break;
	}*/
}
