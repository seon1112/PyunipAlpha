<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
$(function(){
  $(".mobile_menu").click(function(){
     $(".mobile_menu_box").css("display","block");
     $(".menu_background").css("display","block");
     $("html body").addClass("scroll_none");
  }); 
  $("#close_mobile_menu_btn").click(function(){
     $(".mobile_menu_box").css("display","none");
     $(".menu_background").css("display","none");   
     $("html body").removeClass("scroll_none");
  });
  
  $(window).resize(function(){
    var width = $(window).width();
    
    if(width >= 1180 ){
       $(".mobile_menu_box").css("display","none");
       $(".menu_background").css("display","none");        
       $("html body").removeClass("scroll_none");
    }
  });
  
  $(document).mousedown(function(e){
    if($(e.target).parents(".mobile_menu_box").length == 0){
       $(".mobile_menu_box").css("display","none");
       $(".menu_background").css("display","none");         
       $("html body").removeClass("scroll_none");
    }
  });
});

function showDropDown(){
  $(".gnb").css("display","block");
}

function hideDropDown(){
  $(".gnb").css("display","none");
}

function cancelHide(){
  $(".gnb").css("display","block");
}

function notOpen(){
  alert("오픈 준비 중 입니다.");
}
</script>
<!--헤더 고정  -->
<header>
  <div class="header_nav">
    <div class="header_box">
      <a class="header_logo mobile_hidden" href="/main/main_form"></a>
      <a class="mobile_logo mobile_view" href="/main/main_form"></a>
      <div class="header_ul mobile_hidden" id="pc_menu" onmouseover="showDropDown()">
        <a class="header_li <%=request.getParameter("on_1")%>" href="/univ/univ_apply">대학정보</a>
        <a class="header_li <%=request.getParameter("on_2")%>" href="/board/apply_form">입시정보</a>
        <a class="header_li <%=request.getParameter("on_3")%>" href="/board/talk_form">커뮤니티</a>
        <a class="header_li <%=request.getParameter("on_4")%>" href="/board/success_story_form">성공스토리</a>
        <c:if test="${m.ROLE eq 'ROLE_ADMIN'}">
          <a  class="header_li" href="/admin/univ/ad_univ_info">관리자</a>
        </c:if>
      </div>
    </div>
    <div class="header_box2 ">
      <div>
      	<a class="header_box_btn white_round_btn" href="https://open.kakao.com/o/spPYQ7yg">문의하기</a>
        <c:choose>
          <c:when test="${m eq null }">
            <a class="header_box_btn main_round_btn2" href="/main/login_form">로그인</a>
          </c:when>
          <c:otherwise>
            <a class="header_box_btn2 white_round_btn" href="/user/myPage_form">마이페이지</a>
            <a class="header_box_btn main_round_btn2" id="logoutButton" onclick="document.getElementById('logoutForm').submit();">로그아웃</a>
			<form id="logoutForm" action="/login/logout" method="post">
			    <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
			</form>            
          </c:otherwise>        
        </c:choose>
      </div>
      <div class="mobile_menu"></div>
    </div>
  </div>
</header>
<div class="gnb mobile_hidden" onmouseover="cancelHide()" onmouseout="hideDropDown()">
  <div class="gnb_nav">
    <ul>
      <li><a href="/univ/univ_apply">최신 모집 요강</a></li>
      <li><a href="/univ/univ_major">편입 학과 정보</a></li>
    </ul>
    <ul>
      <li><a href="/board/apply_form">과년도 모집 요강</a></li>
      <li><a href="/board/univ_news_form">편입 뉴스</a></li>
    </ul>
    <ul style="margin-left : 9px;">
      <li><a href="/board/talk_form">편입 이야기</a></li>
      <li><a href="/board/study_form">편입 스터디</a></li>
    </ul>
    <ul style="margin-left : 12px;">
      <li style="margin-left:16px;"><a href="/board/success_story_form">합격 수기</a></li>
      <li style="margin-left:16px;"><a href="/board/proof_shot_form">합격 인증샷</a></li>
    </ul>
  </div>
</div>