<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>         
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>합격 수기</title>
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<link rel="stylesheet" href="/css/board.css" type="text/css">
<script type="text/javascript" src="/js/board.js"></script> 
<script>
$(function(){
  searchList2("1");
  
  $(".search_input").keypress(function(e){
      if(e.which==13){
        searchList2("1");
      }
    });
  $("#my_item").click(function(){
    if($(this).hasClass("selected")){
       $(this).removeClass("selected");
     }else{
       $(this).addClass("selected");
     }
     searchList2("1");
  });
  
  $("#my_heart").click(function(){
    if($(this).hasClass("selected")){
       $(this).removeClass("selected");
     }else{
       $(this).addClass("selected");
     }
     searchList2("1");
  });    
});

function insertTalk(){
  var USER_NUM=$("#USER_NUM").val();
  if(USER_NUM =='' || USER_NUM == null){
	 if(!confirm("로그인이 필요한 서비스입니다. 로그인하시겠습니까?")){
		 return;
	 }else{
		 location.href='/board/success_story_insert';
	 }
  }	
	location.href='/board/success_story_insert';
}
</script>
<style type="text/css">
.study_box2 .list_item4 .item_content_box2 .item_txt > p > span{
    font-family: "Pretendard" !important;	
	font-size : 14px !important;
	color : var(--Gray-50) !important;
	font-weight: 400 !important;
}
.study_box2 .list_item4 .item_content_box2 .item_txt > p > img{
	display: none !important;
}
</style>
</head>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp">
  <jsp:param value="on" name="on_4"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<jsp:include page="/WEB-INF/views/inc/title_box.jsp">
  <jsp:param value="banner_selected" name="menu_5"/>
</jsp:include>
<div class="content">
  <div class="content_list_wrap">
    <div class="board_btn_box search">
      <div class="filter_box">
        <div class="total_cnt" id="total_cnt">총 <span>0</span>건</div>
        <a class="plus_btn main_round_btn" id="talk_insert_btn" href="javascript:insertTalk()">글쓰기</a>
      </div>
      <div class="filter_container">
      	<div class="inner_container">
      		<c:if test="${m ne null}">
 	        <button class="gray_square_btn my" id="my_item">내 수기</button>      
	        <button class="gray_square_btn heart" id="my_heart">관심</button>
	        <div class="filter_middle_line"></div>     	
	        </c:if>      	
      	</div>
	    <label class="hidden_label" for="search_input">대학명을 검색해주세요</label>
        <input class="search_input" id="search_input" type="text" placeholder="대학명을 검색해주세요">
      </div>	
    </div>  
    <div class="study_list"></div>    
  </div>
</div>
<input type="hidden" id="BRD_CTG" value="4">
<input type="hidden" id="USER_NUM" value="${USER_NUM}">
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>