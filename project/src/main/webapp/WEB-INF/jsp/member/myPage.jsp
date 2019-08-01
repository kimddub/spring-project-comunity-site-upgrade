<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="마이페이지" />
<%@ include file="../part/head.jspf"%>
<link rel="stylesheet" href="/resource/myPage/myPage.css" /> 
<script src="/resource/myPage/myPage.js"></script>

	<form id="modify-form" onsubmit="formCheck(this); return false;" action="./doModify" method="POST">
		<div class="con table-common member-info-table">
			<table>
				<colgroup>
					<col width="40%">
				</colgroup>
				<tbody>
					<tr>
						<th>이름</th>
						<td>
							<input type="text" name="name" value="${loginMember.name}">
						</td>
					</tr>
					
					<tr class="email-form">
						<th>e-mail</th>
						<td>
							<div class="email-read-form">
								${loginMember.email}
								<input type="button" value="변경" onclick="Member__modifyEmail(this);">
							</div>
							<div class="email-modify-form">
								<input type="hidden" name="emailModifyMode" value="no">
								<div class="emailAuthStatus"> 
					 				<h5>e-mail 미인증 상태</h5>
								</div>
								
								<input type="hidden" name="emailModifyAuth" value="no">
								<input type="email" name="newEmail" placeholder="변경할 e-mail">
								
								<input type="button" name="AuthCodeSubmitBtn" value="전송" onclick="getEmailAuthCode();">
								<input type="button" value="취소" onclick="Member__cancelModifyEmail(this);">
								
								<div class="emailCode-form">
									<input type="hidden" name="authCode" value="%인증코드발송전%">
									<input type="text" name="newEmailAuthCode" placeholder="인증코드">
									<input type="button" value="인증" onclick="checkEmailAuthCode();">
								</div>
							</div>
						</td>
					</tr>
					
					<tr>
						<th>아이디</th>
						<td>${loginMember.loginId}</td>
					</tr>
					
					<tr class="password-form">
						<th>비밀번호</th>
						<td>
							<input type="password" name="loginPw" placeholder="기존 비밀번호">
							<input class="password-unmodify-mode" type="button" value="비밀번호 변경" onclick="Member__modifyPw();">
							<div class="password-modify-form">
								<input type="hidden" name="modifyPassword" value="no">
								<input type="password" name="newLoginPw" placeholder="새 비밀번호">
								<input type="button" value="취소" onclick="Member__cancelModifyPw();">
							</div>
						</td>
					</tr>
					<tr>
						<td class="btn-box" colspan="2">
							<input type="submit" value="회원정보 수정">
							<input type="button" onclick="doWithdraw();" value="회원 탈퇴">
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
	
	<form id="withdraw-form" action="./doWithdraw" method="POST">
		<input type="hidden" name="loginPw">
	</form>

</body>
</html>