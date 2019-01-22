package com.spring.bookstore.member.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.bookstore.member.persistance.MemberDAO;
import com.spring.bookstore.member.vo.Cart;
import com.spring.bookstore.member.vo.User;
import com.spring.bookstore.share.service.ShareService;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	MemberDAO dao;
	@Autowired
	ShareService shareService;

	@Override
	public void loginDo(HttpServletRequest request, RedirectAttributes model) {

		/*if (request.getSession().getAttribute("userId") == null) {
			String id = request.getParameter("id");
			String pw = request.getParameter("pw");

			if (request.getSession().getAttribute("adminId") != null) {
				request.getSession().invalidate();
			}
			int result = 0;
			switch ((result = dao.loginDo(id, pw))) {
			case 0:
				model.addFlashAttribute("message", "로그인실패! 없는 id 입니다.");
				break;
			case -1:
				model.addFlashAttribute("message", "로그인 실패! 비밀번호가 다릅니다.");
				break;
			case 1:
				request.getSession().setAttribute("userId", id);
				model.addFlashAttribute("message", "로그인 성공! '" + id + "'님 환영합니다.!");
				break;
			case 2:
				model.addFlashAttribute("message", "이메일 인증 후 다시 시도하세요!");
				break;
			}
			System.out.println("result : " + result);
		}*/
		/*request.getSession().setAttribute("userId", id);
		model.addFlashAttribute("message", "로그인 성공! '" + id + "'님 환영합니다.!");*/
	}

	@Override
	public void logoutDo(HttpServletRequest request, RedirectAttributes model) {
		request.getSession().invalidate();

		model.addFlashAttribute("message", "로그아웃!");
	}

	@Override
	public void checkId(HttpServletRequest request, Model model) {

		String id = request.getParameter("id");

		model.addAttribute("selectCnt", dao.checkId(id));
		model.addAttribute("id", id);
	}

	@Override
	public void signUpDo(HttpServletRequest request, Model model) {
		User newUser = new User();
		newUser.setUser_id(request.getParameter("id"));
		newUser.setUser_pw(request.getParameter("pw"));
		newUser.setName(request.getParameter("name"));
		newUser.setZipcode(request.getParameter("zipcode"));
		newUser.setAddress1(request.getParameter("address1"));
		newUser.setAddress2(request.getParameter("address2"));

		String ssn = request.getParameter("ssn1") + request.getParameter("ssn2");
		newUser.setSsn(ssn);

		String hp = "";
		String hp1 = request.getParameter("hp1").trim();
		String hp2 = request.getParameter("hp2").trim();
		String hp3 = request.getParameter("hp3").trim();

		if (!hp1.equals("") && !hp2.equals("") && !hp3.equals(""))
			hp = hp1 + "-" + hp2 + "-" + hp3;
		newUser.setPhone_number(hp);

		String email = request.getParameter("email1") + "@" + request.getParameter("email2");
		newUser.setEmail(email);

		UUID uuid = UUID.randomUUID();
		String key = uuid.toString();
		newUser.setAccept_code(key);

		newUser.setHire_date(new Timestamp(System.currentTimeMillis()));

		int result = 0;
		result = dao.signUpDo(newUser);

		if (result == 1) {
			sendEmail(newUser.getEmail(), key);
			model.addAttribute("email", newUser.getEmail());
		}
	}

	public void sendEmail(String toEmail, String key) {
		final String username = "kim910712@gmail.com";
		final String password = "qwjdskap!2";

		Properties props = new Properties();
		props.put("mail.smtp.user", username);
		props.put("mail.smtp.password", password);
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "25");
		props.put("mail.debug", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.EnableSSL.enable", "true");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress("admin@JBook.com"));

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

			String content = "J Book 회원가입 인증 메일 입니다. 링크를 눌러 회원가입을 완료하세요. <br>"
					+ "<a href='http://localhost:8090/bookstore/emailChk?key=" + key + "'> " + key + " </a>";
			message.setSubject("J Book 회원가입 인증 메일"); // 메일 제목
			message.setContent(content, "text/html; charset=utf-8"); // 글내용 타입, charset 설정

			System.out.println("send!!!");
			Transport.send(message);
			System.out.println("SEND");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void emailCheck(HttpServletRequest request, Model model) {
		String accept_code = request.getParameter("key");

		if (dao.emailCheck(accept_code) != 0) {
			model.addAttribute("checked", "true");
		}
	}

	@Override
	public void cartAddDo(HttpServletRequest request, RedirectAttributes model) {
		String user_id = (String) request.getSession().getAttribute("userId");
		int book_code = Integer.parseInt(request.getParameter("book_code"));
		int wish_stock = Integer.parseInt(request.getParameter("wish_stock"));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("book_code", book_code);
		map.put("wish_stock", wish_stock);
		if (dao.cartAddDo(map) != 0)
			model.addFlashAttribute("message", "장바구니 추가 성공");
		else
			model.addFlashAttribute("message", "장바구니 추가 실패! 해당책의 남은 수량이 없습니다.!");
	}

	@Override
	public void cartList(HttpServletRequest request, Model model) {
		int toalPrice = 0;
		List<Cart> dtos = null;

		String user_id = (String) request.getSession().getAttribute("userId");

		shareService.getCategorys(request, model);
		toalPrice = dao.cartTotalPrice(user_id);
		dtos = dao.cartList(user_id);

		model.addAttribute("dtos", dtos);
		model.addAttribute("totalPrice", toalPrice);
	}

	@Override
	public void cartDel(HttpServletRequest request, RedirectAttributes model) {
		String user_id = (String) request.getSession().getAttribute("userId");
		String code = request.getParameter("book_code");

		if (dao.cartsDelDo(user_id, code) == 0)
			model.addFlashAttribute("message", "Error 예기치 못한 오류<br> 장바구니 삭제 실패!!");
	}

	@Override
	public void cartsDel(HttpServletRequest request, RedirectAttributes model) {
		String[] checkeds = request.getParameterValues("chkbox");
		String user_id = (String) request.getSession().getAttribute("userId");

		if (dao.cartsDelDo(user_id, checkeds) == 0)
			model.addFlashAttribute("message", "Error 예외발생<br> 장바구니 삭제에 실패하였습니다.");
	}
}
