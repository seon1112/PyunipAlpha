<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>로그인</title>
<link rel="stylesheet" href="/css/login.css" type="text/css">
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
$(function(){
// 	$("#naver").click(function(){
// 		alert("죄송합니다. 아직 준비 중인 서비스입니다.");
// 	});
});
</script> 
</head>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<div class="login_content">
  <div class="login_wrap">
    <div class="login_title_wrap">
    	<span class="info_title">소셜 로그인</span>
        <span>소셜 로그인을 통해 가입이 가능합니다.</span>    
    </div>
	  <div class="login_mid_line"></div>
	  <div class="social_box">
	    <a class="social_item" id="naver" href="/oauth2/authorization/naver"></a>
	    <a class="social_item" id="kakao" href='/oauth2/authorization/kakao'></a>
	    <p style="color:var(--Gray-50); font-size: 17px;">로그인 시 <strong style="color:var(--Siren)"><a href="/filedownload2?NUM=1">서비스 약관,</a><a href="/filedownload2?NUM=2">개인정보처리방침</a></strong>에 동의하게 됩니다.</p>
	  </div>
  </div>
</div>
<input type="hidden" data-error="${error}" id="errorM">
</body>
</html>