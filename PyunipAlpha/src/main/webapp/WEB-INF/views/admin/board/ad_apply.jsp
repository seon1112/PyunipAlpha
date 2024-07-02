<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>[관리자페이지]과년도 모집 요강</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript" src="/js/ad_board.js"></script> 
<script type="text/javascript">
$(function(){
	  var ctg=$("#ctg").val();
	  searchList(ctg,"1");
	  
	  $("#sch_btn").click(function(){
		  searchList(ctg,"1");
	  });
	  
	  $("#search_input").keypress(function(e){
      if(e.which==13){
    	  searchList(ctg,"1");
      }
    });	  
});

function add(){
	  var USER_NUM=$("#USER_NUM").val();
	  if(USER_NUM =='' || USER_NUM == null){
		 if(!confirm("로그인이 필요한 서비스입니다. 로그인하시겠습니까?")){
			 return;
		 }else{
			 location.href='/admin/board/apply_add';
		 }
	  }	
		location.href='/admin/board/apply_add';	
}
</script>
</head>
<body>
<!-- top header -->
<jsp:include page="/WEB-INF/views/inc/ad_header.jsp">
  <jsp:param value="selected" name="menu_2"/>
</jsp:include>
<!-- sidebar -->
<jsp:include page="/WEB-INF/views/inc/ad_sidebar.jsp">
  <jsp:param value="selected" name="memu_1"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>과년도 모집 요강</span>
      <div class="header_search">
        <label class="hidden_label" for="search_input">제목/내용 검색</label>
        <input class="search_input2" placeholder="제목/내용을 검색해주세요" id="search_input" type="text">
        <button class="base_btn" id="sch_btn">검색</button>
        <input type="hidden" id="ctg" value="2">
      </div>
    </div>
    <div class="content">
      <div class="btn_wrap">
       <div class="total_cnt">검색결과<span></span></div>
       <div class="btn_container">
         <a class="base_btn" href="javascript:add()">추가</a>
       </div>
      </div>
      <div class="content_list"></div>
    </div>
  </div>
</section>
<input type="hidden" id="USER_NUM" value="${USER_NUM}">
</body>
</html>