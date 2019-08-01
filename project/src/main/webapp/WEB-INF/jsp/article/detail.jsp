<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="게시물 상세" />
<%@ include file="../part/head.jspf"%>

<link rel="stylesheet" href="/resource/detail/detail.css" /> 
 
<script>
	var articleId = parseInt('${article.id}');
	var articleMemberId = parseInt('${article.memberId}');
	var sessionId = parseInt('${loginMemberId}');
</script>

<script src="/resource/detail/detail.js"></script>

<div class="article-detail table-common con">
	<div class="article-writer cell">
        <div class="writer-icon">
        
        </div>
        <span>${article.extra.writerName}</span>
	</div>
        
	<table>
		<colgroup>
			<col width="80">
		</colgroup>
		<tbody>
			<tr class="article-title">
				<td colspan="6"><c:out value="${article.title}" /></td>
			</tr>
			<tr class="article-info">
				<th>ID</th>
				<td><c:out value="${article.id}" /></td>
				<th>날짜</th>
				<td><c:out value="${article.regDate}" /></td>
				<th>조회수</th>
				<td><c:out value="${article.view}" /></td>
			</tr>
			<tr class="article-body">
				<td colspan="6">
					<span>${article.bodyForPrint}</span>
					<div class="files">
						<div class="image-list">
							<c:forEach items="${files}" var="file">
								<div class="img-box">
									<img src="/article/showImg?id=${file.id}" alt="${file.originFileName }">
								</div><br/>
							</c:forEach>
						</div>
						
						<c:if test="${files.size() > 0}">
							<div class="download-list">
								<h3>첨부파일</h3>
								
								<c:forEach items="${files}" var="file">
									<a href="./downloadFile?id=${file.id}">${file.originFileName}</a><br/>
								</c:forEach>
							</div>
						</c:if>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>

<section class="reply con">
	<h2>
		댓글 작성
		</h1>

		<div class="reply-form">
			<form onsubmit="Article__addReply(this); return false;" method="POST">
				<div>
					<c:if test="${!isLogined}">
						<textarea readonly="readonly" placeholder="로그인 후 이용 가능합니다."></textarea>
					</c:if>
					<c:if test="${isLogined}">
						<textarea name="body" placeholder="욕설은 자제해주세요."></textarea>
						<input type="submit" value="작성">
					</c:if>
				</div>
			</form>
		</div>

		<div class="reply-list table-common con"">
			<table>
				<colgroup>
					<col width="80">
					<col>
					<col width="120">
					<col width="120">
				</colgroup>
				<thead>
					<tr>
						<th>ID</th>
						<th>내용</th>
						<th>등록날짜</th>
						<th>비고</th>
					</tr>
				</thead>
				<tbody>

				<!-- 댓글 AJAX -->

				</tbody>
			</table>
		</div>
</section>
<%@ include file="../part/foot.jspf"%>