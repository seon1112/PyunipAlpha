<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>[관리자페이지]합격후기</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript" src="/js/ad_board.js"></script> 
<script type="text/javascript">
$(function(){
	  var ctg=$("#ctg").val();
	  searchList(ctg,"1");
	  
	  $("#dv").change(function(){
		  searchList(ctg,"1");
	  });
	  
	  $("#sch_btn").click(function(){
		  searchList(ctg,"1");
	  });
	  
	  $("#search_input").keypress(function(e){
	    if(e.which==13){
	  	  searchList(ctg,"1");
	    }
	  });
});
</script>
</head>
<body>
<!-- top header -->
<jsp:include page="/WEB-INF/views/inc/ad_header.jsp">
  <jsp:param value="selected" name="menu_2"/>
</jsp:include>
<!-- sidebar -->
<jsp:include page="/WEB-INF/views/inc/ad_sidebar.jsp">
  <jsp:param value="selected" name="memu_5"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>합격 수기</span>
      <div class="header_search">
        <label class="hidden_label" for="search_input">제목 검색</label>
        <input class="search_input2" placeholder="제목을 검색해주세요" id="search_input" type="text">
        <button class="base_btn" id="sch_btn">검색</button>
        <input type="hidden" id="ctg" value="4">
      </div>
    </div>
    <div class="content">
      <div class="btn_wrap">
       <div class="total_cnt">검색결과<span></span></div>
      </div>
      <div class="content_list"></div>
    </div>
  </div>
</section>
</body>
</html>