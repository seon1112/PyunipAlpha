<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="/css/admin.css" type="text/css">
<style>
<style>
ul{margin:0;}
a{color: var(--White);}
a:hover{color:var(--White); opacity:1; text-decoration:none;}
input{margin:0;}
</style>
<header class="top_header">
	<div class="top_header_logo"></div>
	<nav class="top_header_nav">
    <a class="top_header_item <%=request.getParameter("menu_1")%>" href="/admin/univ/ad_univ_info">대학 관리</a>
    <a class="top_header_item <%=request.getParameter("menu_2")%>" href="/admin/board/ad_apply">게시판 관리</a>
    <a class="top_header_item <%=request.getParameter("menu_3")%>" href="/admin/user/ad_user">회원 관리</a>
	</nav>
    <c:choose>
      <c:when test="${m eq null }">
        <a class="gray_btn" href="/main/login_form">로그인</a>
        <a class="gray_btn" href="/main/main_form">메인 사이트 이동</a>
      </c:when>
      <c:otherwise>
        <a class="gray_btn" href="/login/logout">로그아웃</a>
        <a class="gray_btn" href="/main/main_form">메인 사이트 이동</a>
      </c:otherwise>        
    </c:choose>	
	
</header>
