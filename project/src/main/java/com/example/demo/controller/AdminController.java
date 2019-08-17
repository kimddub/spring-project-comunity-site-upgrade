package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.Member;
import com.example.demo.service.MemberService;

import groovy.util.logging.Slf4j;

@Controller
@Slf4j
public class AdminController {
	@Autowired
	MemberService memberService;
	
	@RequestMapping("admin/member")
	public String showMember(@RequestParam Map<String,Object> param, HttpServletRequest request,Model model) {
		if (!(boolean)request.getAttribute("isAdminMode")) {
			
			model.addAttribute("alertMsg", "관리자 권한이 없습니다");
			model.addAttribute("historyBack", true);
			
			return "common/redirect";
		}
		
		List<Member> members = memberService.getMemberList(param);
		
		model.addAttribute("members", members);
		
		return "admin/member";
	}
	
}
