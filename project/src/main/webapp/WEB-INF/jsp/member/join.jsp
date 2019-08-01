<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="회원가입"/>
<%@ include file="../part/head.jspf"%>
<script src="/resource/join/join.js"></script>

<section class="member-form-box">
	<form action="./doJoin" onsubmit="formCheck(this); return false;" methfo="POST" class="con">
		<div class="member-form">
			<div>
				<input type="text" name="name" placeholder="이름">
			</div>
			
			<div>
				<input type="text" name="loginId" placeholder="ID">
			</div>
			
			<div>
				<input type="password" name="loginPw" placeholder="PW">
			</div>
			
			<div>
				<input type="email" name="email" placeholder="e-mail">
			</div>
			
			<div class="member-form-btn">
				<input type="submit" value="가입하기">
				<input type="button" onclick="history.back();" value="취소">
			</div>
		</div>
	</form>
</section>

<%@ include file="../part/foot.jspf"%>