package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.TempKey;
import com.example.demo.dao.MemberDao;
import com.example.demo.dto.Member;
import com.example.demo.hndler.MailHandler;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
	@Autowired
	MemberDao memberDao;
	@Autowired
	private JavaMailSender sender;
	@Value("${server.port}")
	private String serverPort;
	
	public Member getMember(long id) {
		Map<String,Object> args = new HashMap<>();
		args.put("id", id);
		args.put("where__id",true);
		return memberDao.getMember(args);
	}
	
	public List<Member> getMemberList(Map<String, Object> param) {
		return memberDao.getMemberList(param);
	}

	public Member getMember(Map <String,Object> args) {
		return memberDao.getMember(args);
	}
	
	public boolean sessionCheck(HttpServletRequest request) {
		return request.getAttribute("loginMemberId") == null ? false : true;
	}
	
	public Map<String, Object> join(Map<String, Object> param) {
		String msg = "";
		String resultCode = "";
		
		param.put("where_joinCondition", true);
		Member loginMember = getMember(param);

		if (loginMember != null) {
			msg += "이미 사용중인 ID 또는 e-mail입니다.";
			resultCode = "F-2";
		} else {
			
			// 숫자 대문자 영문자의 30개 문자 랜덤 조합
			param.put("authKey", new TempKey().getKey());

			memberDao.join(param);
			
			MailHandler mail;
			try {
				mail = new MailHandler(sender);
				mail.setFrom("js_WEB", "커뮤니티 관리자");
				mail.setTo((String)param.get("email"));
				mail.setSubject("회원인증");
				mail.setText(new StringBuffer().append("<h1>회원 이메일 인증</h1>")
						.append("<a href='http://localhost:" + serverPort + "/member/confirm?email=").append((String)param.get("email"))
						.append("&authKey=").append((String)param.get("authKey"))
						.append("' target='_blank'> 클릭시 메일 인증 페이지로 이동합니다. </a>").toString()
						);
				mail.send();
			} catch (Exception e) {
				e.printStackTrace();
			}
				msg += "이메일 인증 후 이용해주세요";
				resultCode = "S-2";

			}

		return Maps.of("msg", msg, "resultCode", resultCode);
	}
	
	public Map<String, Object> updateAuthStatus(Map<String, Object> param) {
		param.put("where__email", true);
		Member member = memberDao.getMember(param);
		
		String msg = "";
		String resultCode = "";
		
		if(member == null) {
			msg = "회원 인증에 실패했습니다.";
			resultCode = "F-1";
		} else {
			 
			if ( ((String)param.get("authKey")).equals(member.getAuthkey()) ) {
				if (  member.isEmailAuthStatus() ) {
					
					msg = "이미 인증되었습니다. 로그인 후 이용해주세요";
					
				} else {
					memberDao.updateAuthStatus(param);
	
					msg = "회원 인증에 성공했습니다. 로그인 후 이용해주세요";
				}

				resultCode = "S-1";
			}
			
		}
		
		return Maps.of("msg", msg, "resultCode", resultCode);
	}

	public Map<String, Object> login(Map<String, Object> param, HttpSession session) {
		String resultCode = "";
		String msg = "";
		int loginMemberId = 0;
		
		param.put("where__loginCondition", true);
		Member loginMember = getMember(param);

		if (loginMember == null) {
			msg += "로그인 정보를 다시 확인하세요";
			resultCode = "F-1";
			return Maps.of("msg", msg, "resultCode", resultCode);
		} else if (!loginMember.isEmailAuthStatus()) { 
			resultCode = "F-2";
			msg = "이메일 인증 후 시도해주세요";
			return Maps.of("msg", msg, "resultCode", resultCode);
		} else {
			resultCode = "S-1";
			msg = loginMember.getName() + "님 환영합니다.";
			loginMemberId = loginMember.getId();
		}

		return Maps.of("msg", msg, "resultCode", resultCode, "loginMemberId", loginMemberId);
	}
	
	public Map<String, Object> modify(Map<String, Object> args,HttpServletRequest request) {
		String resultCode = "";
		String msg = "";
		
		args.put("id", request.getAttribute("loginMemberId"));
		
		// 이메일 인증 안됐을시 진행 X
		if ( ((String)args.get("emailModifyMode")).equals("yes") ) {
			if (((String)args.get("emailModifyAuth")).equals("no")) {
				resultCode = "F-1";
				msg = "이메일 변경은 인증 후 가능합니다.";
				
				return Maps.of("msg", msg, "resultCode", resultCode);
			}
		}
		
		// 비밀번호 불일치시 진행 X
		args.put("loginId", request.getAttribute("loginMemberLoginId"));
		args.put("loginPw", ((String)args.get("loginPw")).trim());
		args.put("where__loginCondition", true);
		Member loginMember = getMember(args);
		
		if (loginMember == null) {
			resultCode = "F-2";
			msg = "기존 비밀번호를 확인해주세요.";
			
			return Maps.of("msg", msg, "resultCode", resultCode);
		}
		
		if (((String)args.get("modifyPassword")).equals("no")) {
			// 비밀번호 변경 안할시 다른 정보만 수정
			args.put("newLoginPw", null);
			memberDao.update(args);
			
			resultCode = "S-1";
			msg = "성공적으로 수정되었습니다.";
		} else {
			// 비밀번호까지 변경
			memberDao.update(args);
			
			resultCode = "S-1";
			msg = "성공적으로 수정되었습니다.";
		}
		
		
		return Maps.of("msg", msg, "resultCode", resultCode);
	}
	
	public Map<String, Object> sendNewEmailAuthCode(String email) {
		
		Map<String,Object> args = new HashMap<>();
		args.put("where__email", true);
		args.put("email", email);
		Member member = getMember(args);
		
		System.out.println(member);
		
		String resultCode = "";
		String msg = "";
		
		if ( member != null ) {
			resultCode = "F-1";
			msg = "변경할 수 없는 e-mail입니다.";
			
			return Maps.of("msg", msg, "resultCode", resultCode);
		}
		
		String authCode = new TempKey(5).getKey();
		MailHandler mail;
		try {
			mail = new MailHandler(sender);
			mail.setFrom("js_WEB", "커뮤니티 관리자");
			mail.setTo(email);
			mail.setSubject("이메일 변경 인증");
			mail.setText(new StringBuffer().append("이메일 인증코드 [ " + authCode + " ]").toString());
			mail.send();
			
			resultCode = "S-1";
			msg += "이메일로 인증코드가 전송되엇습니다.";
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return Maps.of("authCode", authCode, "msg", msg, "resultCode", resultCode);
	}

	public Map<String,Object> withdraw(Map<String, Object> args, HttpServletRequest request) {
		String html = "<script>";
		
		String resultCode = "";
		String msg = "";
		
		args.put("id", request.getAttribute("loginMemberId"));
		args.put("loginId", request.getAttribute("loginMemberLoginId"));
		
		// 비밀번호 불일치시 진행 X
		args.put("where__loginCondition", true);
		Member loginMember = getMember(args);
		
		if (loginMember == null) {
			msg += "기존 비밀번호를 확인해주세요.";
			resultCode += "F-1";
			
			return Maps.of("resultCode", resultCode, "msg", msg);
		}
		
		memberDao.withdraw(args);
		
		msg += "탈퇴되셨습니다.";
		resultCode += "S-1";
		
		request.getSession().removeAttribute("loginMemberId");
		
		return Maps.of("resultCode", resultCode, "msg", msg);
	}
	
	public Map<String, Object> findLoginId(Map<String, Object> param, Member member){
		
		String resultCode ="";
		String msg = "";
		
		MailHandler mail;
		try {
			mail = new MailHandler(sender);
			mail.setFrom("js_WEB", "커뮤니티 관리자");
			mail.setTo(member.getEmail());
			mail.setSubject("아이디 찾기");
			mail.setText(new StringBuffer().append("회원님의 아이디는 " + member.getLoginId() + " 입니다.")
					.append("<a href='http://localhost:" + serverPort + "/member/login")
					.append("' target='_blank'> 로그인하러 가기 </a>").toString()
					);
			mail.send();

			resultCode =  "S-1";
			msg = "메일이 발송되었습니다.";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Maps.of("resultCode", resultCode, "msg", msg);
	}
	
	public Map<String, Object> findLoginPw(Map<String, Object> param, Member member) {

		String resultCode ="";
		String msg = "";
		
		
		// 숫자 대문자 영문자의 10개 문자 랜덤 조합
		String newLoginPw = new TempKey(10).getKey();
		
		param.put("id", member.getId());
		param.put("newLoginPw", newLoginPw);
		memberDao.update(param);
		
		MailHandler mail;
		try {
			mail = new MailHandler(sender);
			mail.setFrom("js_WEB", "커뮤니티 관리자");
			mail.setTo(member.getEmail());
			mail.setSubject("비밀번호 찾기");
			mail.setText(new StringBuffer().append("회원님의 새 비밀번호는 " + newLoginPw + " 입니다.")
					.append("<a href='http://localhost:" + serverPort + "/member/login")
					.append("' target='_blank'> 비밀번호 수정하러 가기 </a>").toString()
					);
			mail.send();

			resultCode = "S-1";
			msg = "메일이 발송되었습니다.";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Maps.of("resultCode", resultCode, "msg", msg);
	}
	
	public Map<String, Object> updatePermission(Map<String, Object> param) {
		String resultCode = "";
		String msg ="";
		
		/*
		String[] memberIdForUpdate = (String[])param.get("memberIdForUpdate");
		
		for (String id : memberIdForUpdate) {
			System.out.println(id);
		}
		System.out.println(param.get("memberIdForUpdate"));
		*/
		
		memberDao.updatePermission(param);
		
		resultCode = "S-1";
		msg = ((String[])param.get("memberIdForUpdate")).length + "명의 회원 권한을 " + param.get("updatedPermissionLevel") + "로 변경하였습니다.";
		
		return Maps.of("resultCode",resultCode,"msg",msg);
	}
}
