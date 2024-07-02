<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>편입알파</title>
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<link rel="stylesheet" href="/css/main.css" type="text/css">
</head>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<div class="content_main" style="width: 100%; height: 100%; display: flex; justify-content: center; align-items: center; flex-direction: column;">
	<img src="../image/fix_img.png" style="width:800px;">
	<div style="font-size:18px; font-weight: 600;">오류가 발생했습니다. 관리자에게 문의해주세요</div>
</div>  
<!-- 하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>