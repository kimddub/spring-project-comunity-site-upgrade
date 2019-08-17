package com.example.demo.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.groovy.util.Maps;

import com.example.demo.dto.Member;

public interface MemberService {
	
	public Member getMember(long id) ;

	public Member getMember(Map <String,Object> args) ;

	public List<Member> getMemberList(Map<String, Object> param);
	
	public Map<String, Object> login(Map<String, Object> param,HttpSession session);

	public boolean sessionCheck(HttpServletRequest request);

	public Map<String, Object> join(Map<String, Object> param);

	public Map<String, Object> updateAuthStatus(Map<String, Object> param);

	public Map<String, Object> modify(Map<String, Object> args,HttpServletRequest request);

	public Map<String,Object> withdraw(Map<String, Object> args, HttpServletRequest request);

	public Map<String, Object> findLoginId(Map<String, Object> param, Member member);

	public Map<String, Object> findLoginPw(Map<String, Object> param, Member member);

	public Map<String, Object> sendNewEmailAuthCode(String newEmail);
	
}
