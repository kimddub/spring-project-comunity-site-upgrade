<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="메인페이지"/>
<%@ include file="../part/head.jspf"%>

	<section class="con text-align-center">
		<div class="article-preview row">
		    <div class="article-preview-list high-view-article cell">
		        <span>조회수 BEST 게시글</span>
		        
			    <ul>
		        <c:forEach var="article" items="${highViewArticle}">
		        
		        	<c:if test="${article.boardId == 1}">
		        		<c:set var="boardName" value="자유게시판" />
		        	</c:if>
		        	<c:if test="${article.boardId == 2}">
		        		<c:set var="boardName" value="Q $ A" />
		        	</c:if>
		        	
		            <li>
		            	<a class="row" href="../article/detail?id=${article.id}&boardId=${article.boardId}">
							<span class="cell article-rank">${article.extraInt}</span>
							${article.title}
		            		<span class="cell-right article-board-name" >${boardName}</span>
						</a>
		            </li>
		        
		        </c:forEach>
		        </ul>
		        
		    </div>
		    
		     <div class="article-preview-list cell">
		        <span>댓글 BEST 게시글</span>
		        
			    <ul>
		        <c:forEach var="article" items="${mostRepliesArticle}">
		        
		        	<c:if test="${article.boardId == 1}">
		        		<c:set var="boardName" value="자유게시판" />
		        	</c:if>
		        	<c:if test="${article.boardId == 2}">
		        		<c:set var="boardName" value="Q $ A" />
		        	</c:if>
		        
		            <li>
		            	<a class="row" href="../article/detail?id=${article.id}&boardId=${article.boardId}">
							<span class="cell article-rank">${article.extraInt}</span>
							${article.title}
		            		<span class="cell-right article-board-name" >${boardName}</span>
						</a>
		            </li>
		        
		        </c:forEach>
		        </ul>
		        
		    </div>
		</div>
	</section>

<%@ include file="../part/foot.jspf"%>