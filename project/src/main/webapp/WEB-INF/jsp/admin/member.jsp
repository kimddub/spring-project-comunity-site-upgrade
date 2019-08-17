<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="회원관리" />
<%@ include file="../part/head.jspf"%>

	<div class="con title">
		
		<h1>${pageTitle}</h1>
		
	</div>

<div class="con article-list-tool row">
	<div class="con search-form cell-right">
		<form action="./list" method="GET">
			<input type="hidden" name="boardId" value="${param.boardId }">
			<select name="searchType">
				<option value="title">제목</option>
				<option value="body">내용</option>
				<option value="writer">작성자</option>
			</select>
	
			<c:if test="${param.searchType != null && param.searchType != ''}">
				<script>
					$('select[name="searchType"]').val('${param.searchType}');
				</script>
			</c:if>
	
			<input type="text" name="searchKeyword" placeholder="검색어" value="${param.searchKeyword}"> 
			<input type="submit" value="검색" />
		</form>
	</div>
	
	<div class="con sort-select-box cell">
		<select class="sort-select" onchange="Article__sortChanged(this)">
			<option value="latest">최신순</option>
			<option value="old">오래된 순</option>
			<option value="highView">조회수 높은 순</option>
			<option value="lowView">조회수 낮은 순</option>
		</select>
		<c:if test="${param.sort != null && param.sort != ''}">
			<script>
				$('.sort-select').val("${param.sort}");
			</script>
		</c:if>
	</div>
</div>

추가할 컬럼? 작성 글 수/ 댓글 수/ 탈퇴or차단or해제 버튼
<div class="table-common article-list con">
	<table>
		<thead>
			<tr>
				<th>id</th>
				<th>가입날짜</th>
				<th>이름</th>
				<th>e-mail</th>
				<th>로그인ID</th>
				<th>회원등급</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="member" items="${members}">
				<tr>
					<td><c:out value="${member.id}" /></td>
					<td><c:out value="${member.regDate}" /></td>
					<td><c:out value="${member.name}" /></td>
					<td><c:out value="${member.email}" /></td>
					<td><c:out value="${member.loginId}" /></td>
					<td><c:out value="${member.permissionLevel}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<%@ include file="../part/foot.jspf"%>