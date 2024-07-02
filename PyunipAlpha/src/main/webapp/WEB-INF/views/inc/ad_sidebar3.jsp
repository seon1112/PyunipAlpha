<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" href="/css/admin.css" type="text/css">
<div class="ad_sidebar">
	<div class="ad_sidebar_title">회원관리</div>
	<div class="ad_sidebar_menu">
	 <a class=" <%=request.getParameter("memu_1") %>" href="/admin/user/ad_user">회원정보</a>
	 <a class=" <%=request.getParameter("memu_2") %>" href="/admin/user/ad_acss_log">회원로그</a>
	</div>
</div>
