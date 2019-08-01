<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="게시물 수정"/>
<%@ include file="../part/head.jspf"%>

<link rel="stylesheet" href="/resource/modify/modify.css">  
<script>
	var originFileCount = ${files.size()};
</script>
<script src="/resource/modify/modify.js"></script>

<form action="./doModify" method="POST" enctype="multipart/form-data">
>
	<div class="article-edit table-common con">
		<table>
			<colgroup>
				<col width="80">
				<col>
			</colgroup>
			<tbody>
				<tr>
					<th>ID</th>
					<td><c:out value="${article.id}" /></td>
					<input type="hidden" name="id" value="${article.id}">
					<input type="hidden" name="boardId" value="${article.boardId}">
				</tr>
				<tr>
					<th>날짜</th>
					<td><c:out value="${article.regDate}" /></td>
				</tr>
				<tr>
					<th>제목</th>
					<td>
						<input autocomplete="off" type="text" name="title" value="${article.title}">
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td>
						<textarea name="body">${article.body}</textarea>
					</td>
				</tr>
				<tr class="article-file">
					<th>첨부파일<button type="button" onclick="addFile(this, 'body');">추가</button></th>
					<td>
						<c:if test="${files.size() > 0}">
							<div class="fileStatus hasFile">
						</c:if>
						<c:if test="${files.size() < 1}">
							<div class="fileStatus">
						</c:if>
							첨부파일이 없습니다.</div>
						
						<div class="deleteList"></div>
						<div class="fileList">
							<c:forEach items="${files}" var="file">
								<div style="display:inline-block;">		
									<img src="/article/showImg?id=${file.id}" alt="${file.originFileName }" style="max-height:100px; max-width:200px"><br>
									<p>${file.originFileName}</p>
									<!-- <button type="button" onclick="addDeleteList(this, ${file.id})">제거하기</button> -->
									<button type="button" onclick="addDeleteList(this,${file.id});">X</button>
								</div>
							</c:forEach>
						</div>
					</td>
				</tr>	
				<tr>
					<td colspan="2" class="btn-box">
						<input type="submit" value="수정">
						<input type="button" onclick="history.back();" value="취소">
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</form>
<%@ include file="../part/foot.jspf"%>