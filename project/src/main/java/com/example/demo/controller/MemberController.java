package com.example.demo.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Member;
import com.example.demo.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MemberController {
	@Autowired
	private MemberService memberService;

	@RequestMapping("/member/join")
	public String showJoin(@RequestParam Map<String, Object> param, HttpSession session) {
		return "member/join";
	}

	@RequestMapping("/member/doJoin")
	public String doJoin(@RequestParam Map<String, Object> param, Model model) {

		Map<String, Object> rs = memberService.join(param);

		String resultCode = (String) rs.get("resultCode");
		String redirectUrl = (String) param.get("redirectUrl");

		if (redirectUrl == null || redirectUrl.length() == 0) {
			redirectUrl = "/member/login";
		}

		model.addAttribute("alertMsg", rs.get("msg"));

		if (resultCode.startsWith("S-")) {
			model.addAttribute("redirectUrl", redirectUrl);
		} else {
			model.addAttribute("historyBack", true);
		}

		return "common/redirect";
	}

	@RequestMapping("/member/confirm")
	public String confirm(@RequestParam Map<String, Object> param, Model model) {
		// email, authKey
		Map<String, Object> rs = memberService.updateAuthStatus(param);
		String msg = (String) rs.get("msg");
		String resultCode = (String) rs.get("resultCode");

		String redirectUrl = "/home/main";

		if (resultCode.startsWith("S-")) {
			redirectUrl = "/member/login";
		}

		model.addAttribute("alertMsg", msg);
		model.addAttribute("redirectUrl", redirectUrl);

		return "common/redirect";
	}

	@RequestMapping("/member/login")
	public String showLogin(@RequestParam Map<String, Object> param, HttpSession session) {
		return "member/login";
	}

	@RequestMapping("/member/doLogin")
	public String doLogin(@RequestParam Map<String, Object> param, HttpSession session, Model model) {

		Map<String, Object> rs = memberService.login(param, session);

		String resultCode = (String) rs.get("resultCode");
		String redirectUrl = (String) param.get("redirectUrl");

		if (redirectUrl == null || redirectUrl.length() <= 0) {
			redirectUrl = "/home/main";
		}

		model.addAttribute("alertMsg", rs.get("msg"));

		if (resultCode.startsWith("S-")) {
			model.addAttribute("redirectUrl", redirectUrl);
			session.setAttribute("loginMemberId", rs.get("loginMemberId"));
			session.setMaxInactiveInterval(30 * 60);
		} else {
			model.addAttribute("historyBack", true);
		}

		return "common/redirect";
	}

	@RequestMapping("/member/doLogout")
	public String doLogout(@RequestParam Map<String, Object> param, HttpSession session, Model model) {

		session.removeAttribute("loginMemberId");
		model.addAttribute("alertMsg", "로그아웃 되었습니다.");
		model.addAttribute("redirectUrl", "./login");

		return "common/redirect";
	}

	@RequestMapping("/member/myPage")
	public String myPage(HttpSession session) {
		System.out.println("세션자 로그인아이디 : " + session.getAttribute("loginMemberId"));
		return "member/myPage";
	}

	@RequestMapping("/member/doModify")
	public String doModify(@RequestParam Map<String, Object> param, HttpServletRequest request, Model model) {
		System.out.println(param.size());
		System.out.println(param);
		System.out.println("====회원정보 수정 값 테스트====");
		System.out.println("이메일 수정 모드인가? : " + param.get("emailModifyMode"));
		System.out.println("이메일 인증을 받았는가? : " + param.get("emailModifyAuth"));
		System.out.println("===============================");

		Map<String, Object> rs = memberService.modify(param, request);

		String resultCode = (String) rs.get("resultCode");
		String msg = (String) rs.get("msg");
		String redirectUrl = "";

		if (resultCode.startsWith("S-")) {
			redirectUrl = "/member/myPage";
		} else {
			model.addAttribute("historyBack", true);
		}

		model.addAttribute("alertMsg", msg);
		model.addAttribute("redirectUrl", redirectUrl);

		return "common/redirect";
	}

	@RequestMapping("/member/sendNewEmailAuthCode")
	@ResponseBody
	public Map<String, Object> sendNewEmailAuthCode(@RequestParam Map<String, Object> param, Model model) {

		Map<String, Object> rs = memberService.sendNewEmailAuthCode((String) param.get("email"));

		return rs;
	}

	@RequestMapping("/member/doWithdraw")
	public String doWithdraw(@RequestParam Map<String, Object> param, Model model, HttpServletRequest request) {

		System.out.println(param);
		Map<String, Object> rs = memberService.withdraw(param, request);
		String redirectUrl = "../home/main";
		String msg = (String) rs.get("msg");

		if (((String) rs.get("resultCode")).startsWith("F-")) {
			model.addAttribute("historyBack",true);
		}

		model.addAttribute("redirectUrl", redirectUrl);
		model.addAttribute("alertMsg", msg);

		return "common/redirect";
	}

	@RequestMapping("/member/findLoginId")
	@ResponseBody
	public Map<String, Object> findLoginId(@RequestParam Map<String, Object> param) {

		String resultCode = "";
		String msg = "";

		param.put("where__email", true);
		Member member = memberService.getMember(param);

		// 이름과 이메일 일치 확인 어디서??
		if (member == null) {
			resultCode = "F-1";
			msg = "존재하지 않는 이메일 정보입니다.";
		} else if (!member.isEmailAuthStatus()) {
			resultCode = "F-2";
			msg = "확인되지 않은 이메일 정보입니다.";
		} else if (!((String) param.get("name")).equals(member.getName())) {
			resultCode = "F-3";
			msg = "정보가 일치하지 않습니다.";
		} else {
			Map<String, Object> findIdRs = memberService.findLoginId(param, member);
			resultCode = (String) findIdRs.get("resultCode");
			msg = (String) findIdRs.get("msg");
		}

		return Maps.of("resultCode", resultCode, "msg", msg);
	}

	@RequestMapping("/member/findLoginPw")
	@ResponseBody
	public Map<String, Object> findLoginPw(@RequestParam Map<String, Object> param, Model model) {

		String resultCode = "";
		String msg = "";

		param.put("where__email", true);
		Member member = memberService.getMember(param);

		// 이름과 이메일 일치 확인 어디서??
		if (member == null) {
			resultCode = "F-1";
			msg = "존재하지 않는 이메일 정보입니다.";
		} else if (!((String) param.get("loginId")).equals(member.getLoginId())) {
			resultCode = "F-2";
			msg = "정보가 일치하지 않습니다.";
		} else {
			Map<String, Object> findIdRs = memberService.findLoginPw(param, member);
			resultCode = (String) findIdRs.get("resultCode");
			msg = (String) findIdRs.get("msg");
		}

		return Maps.of("resultCode", resultCode, "msg", msg);
	}
}
