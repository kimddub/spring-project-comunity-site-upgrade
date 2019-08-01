package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.Member;
import com.example.demo.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("BaseInterceptor")
public class BaseInterceptor implements HandlerInterceptor {
	@Autowired
	MemberService memberService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		log.info("실행됨");

		if (request.getSession().getAttribute("loginMemberId") == null) {

			request.setAttribute("isLogined", false);
			request.setAttribute("loginMemberId", 0);
			request.setAttribute("loginMemberLoginId", "");
			request.setAttribute("loginMember", null);

		} else {
			int loginMemverId = (int) request.getSession().getAttribute("loginMemberId");
			Member member = memberService.getMember(loginMemverId);

			request.setAttribute("isLogined", true);
			request.setAttribute("loginMemberId", loginMemverId);
			request.setAttribute("loginMemberLoginId", member.getLoginId());
			request.setAttribute("loginMember", member);

		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

}
