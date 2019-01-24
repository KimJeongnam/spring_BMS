package com.spring.bookstore.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	private static int TIME = 60; // 1분

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		request.getSession().setMaxInactiveInterval(TIME);
		request.getSession().setAttribute("userId", ((User) authentication.getPrincipal()).getUsername());
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
