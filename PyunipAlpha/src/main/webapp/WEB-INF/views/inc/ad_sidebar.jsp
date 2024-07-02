<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" href="/css/admin.css" type="text/css">
<div class="ad_sidebar">
	<div class="ad_sidebar_title">게시판관리</div>
	<div class="ad_sidebar_menu">
	 <a class=" <%=request.getParameter("memu_1") %>" href="/admin/board/ad_apply">과년도 모집 요강</a>
	 <a class=" <%=request.getParameter("memu_2") %>" href="/admin/board/ad_univ_news">편입 뉴스</a>
	 <a class=" <%=request.getParameter("memu_3") %>" href="/admin/board/ad_talk">편입 이야기</a>
	 <a class=" <%=request.getParameter("memu_4") %>" href="/admin/board/ad_study">편입 스터디</a>
	 <a class=" <%=request.getParameter("memu_5") %>" href="/admin/board/ad_success_story">합격 수기</a>
 	 <a class=" <%=request.getParameter("memu_6") %>" href="/admin/board/ad_proof_shot">합격 인증샷</a> 
	</div>
</div>
