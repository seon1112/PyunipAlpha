<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>과년도 편입 모집요강</title>
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<link rel="stylesheet" href="/css/board.css" type="text/css">
<script type="text/javascript" src="/js/board.js"></script> 
<script>
$(function(){
	searchList("1");
	
	$(".search_input").keypress(function(e){
	  if(e.which==13){
	    searchList("1");
	  }
	});
});
</script>
</head>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp">
  <jsp:param value="on" name="on_2"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<jsp:include page="/WEB-INF/views/inc/title_box.jsp">
  <jsp:param value="banner_selected" name="menu_1"/>
</jsp:include>
<div class="content">
  <div class="content_list_wrap">
    <div class="board_btn_box">
      <div class="total_cnt" id="total_cnt">총 <span>0</span>건</div>
      <label class="hidden_label" for="search_input">검색어 입력</label>
      <input class="search_input" id="search_input" type="text" placeholder="검색어를 입력해주세요">
    </div>   
    <div class="content_list"></div>
  </div>
</div>
<input type="hidden" id="BRD_CTG" value="2">
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>