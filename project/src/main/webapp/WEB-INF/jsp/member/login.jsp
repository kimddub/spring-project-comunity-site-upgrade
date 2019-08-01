<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="로그인"/>
<%@ include file="../part/head.jspf"%>
<link rel="stylesheet" href="/resource/login/login.css" />
<script src="/resource/login/login.js"></script>

<section class="member-form-box">
	<form action="./doLogin" method="POST" onsubmit="formCheck(this); return false;" class="con loginForm">
		<div class="member-form">
			<div>
				<input type="text" name="loginId" placeholder="로그인 ID">
				<a class="find-loginId" href="javascript:;" onclick="findMode(this);"><i class="far fa-question-circle"></i></a>
			</div>
			
			<div class="find-form find-loginId-form relative" >
				<div class="title"><h5>ID 찾기</h5></div>
				<div>가입하신 정보를 입력해주세요. <br/>이메일로 ID를 전송해드립니다.</div>
				<form onsubmit="findLoginId(this); return false;" method="POST">
					<div>
						<input type="text" name="name" placeholder="이름"><br/>
						<input type="email" name="email" placeholder="e-mail">
					</div>
					
					<div class="find-status"></div>
					
					<div>
						<input type="button" onclick="findLoginId(this); return false;" value="확인">
						<input type="button" onclick="unfindMode();" value="취소">
					</div>
				</form>
			</div>
		
			<div>
				<input type="password" name="loginPw" placeholder="로그인 PW">
				<a class="find-loginPw" href="javascript:;" onclick="findMode(this);""><i class="far fa-question-circle"></i></a>
			</div>
			
			<div class="find-form find-loginPw-form" >
				<div class="title"><h5>PW 찾기</h5></div>
				<div>가입하신 정보를 입력해주세요. <br/>이메일로 새 PW를 전송해드립니다.</div>
				<form method="POST">
					<div>
						<input type="text" name="loginId" placeholder="로그인 ID"><br/>
						<input type="email" name="email" placeholder="e-mail">
					</div>
					
					<div class="find-status"></div>
					
					<div>
						<input type="button" onclick="findLoginPw(this); return false;" value="확인">
						<input type="button" onclick="unfindMode();" value="취소">
					</div>
				</form>
			</div>
			
			<div class="member-form-btn">
				<input type="submit" value="로그인">
				<input type="button" onclick="history.back();" value="취소">
			</div>
		</div>
	</form>
</section>

<%@ include file="../part/foot.jspf"%>