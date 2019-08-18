package com.example.demo.controller;

import java.util.HashMap;
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
	
	@RequestMapping("admin/doUpdateMemberPermission")
	public String doUpdateMemberPermission(String updatedPermissionLevel,String[] memberIdForUpdate, HttpServletRequest request,Model model) {
		if (!(boolean)request.getAttribute("isAdminMode")) {
			
			model.addAttribute("alertMsg", "관리자 권한이 없습니다");
			model.addAttribute("historyBack", true);
			
			return "common/redirect";
		}
		
		/*
		for (String id : memberIdForUpdate) {
			System.out.println(id);
		}
		*/
		
		Map<String,Object> param = new HashMap<>();
		param.put("updatedPermissionLevel", updatedPermissionLevel);
		param.put("memberIdForUpdate", memberIdForUpdate);
		
		Map<String,Object> updatePermissionRs = memberService.updatePermission(param);
		
		model.addAttribute("alertMsg", updatePermissionRs.get("msg"));
		model.addAttribute("resultCode", updatePermissionRs.get("resultCode"));
		model.addAttribute("redirectUrl", "./member");
		
		return "common/redirect";
	}
}
