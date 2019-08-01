<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ include file="../part/head.jspf"%>
<link rel="stylesheet" href="/resource/list/list.css">
<script src="/resource/list/list.js"></script>

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


<div class="table-common article-list con">
	<table>
		<colgroup>
			<col width="80">
			<col width="180">
			<col>
			<col width="100">
			<col width="100">
		</colgroup>
		<thead>
			<tr>
				<th>작성자</th>
				<th>등록날짜</th>
				<th>제목</th>
				<th>댓글</th>
				<th>조회수</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="article" items="${list}">
				<tr>
					<td><c:out value="${article.extra.writerName}" /></td>
					<td><c:out value="${article.regDate}" /></td>
					<td><a class="article-list-title" href="detail?id=${article.id}&${search}"><c:out
								value="${article.title}" /></a></td>
					<td>${article.extra.repliesCount}</td>
					<td>${article.view}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<nav class="pageBlock">
	<ul>
	
		<c:if test="${maker.prev}"><a href="./list?cPage=${maker.startPage - 1}&${search}"> ◀ </a></c:if>
	
		<c:forEach begin="${maker.startPage}" end="${maker.endPage}" var="index">
			
			
			<c:if test="${param.cPage == index}">
				<a class="current-page-number" href="./list?cPage=${index}&${search}">
			</c:if>
			
			<c:if test="${param.cPage != index}">
				<a href="./list?cPage=${index}&${search}">
			</c:if>
			
			${index}</a>
			
		</c:forEach>
		
		<c:if test="${maker.next}"><a href="./list?cPage=${maker.endPage + 1}&${search}"> ▶ </a></c:if>
		
	</ul>
</nav>

<%@ include file="../part/foot.jspf"%>