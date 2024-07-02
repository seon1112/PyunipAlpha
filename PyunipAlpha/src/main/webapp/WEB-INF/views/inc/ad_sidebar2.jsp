<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" href="/css/admin.css" type="text/css">
<div class="ad_sidebar">
	<div class="ad_sidebar_title">대학관리</div>
	<div class="ad_sidebar_menu">
	 <a class=" <%=request.getParameter("memu_1") %>" href="/admin/univ/ad_univ_info">대학정보</a>
	 <a class=" <%=request.getParameter("memu_2") %>" href="/admin/univ/ad_trans_info">학과정보</a>
	 <a class=" <%=request.getParameter("memu_3") %>" href="/admin/univ/ad_slt_info">편입전형</a>
	</div>
</div>
