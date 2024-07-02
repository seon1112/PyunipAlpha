<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>[관리자페이지]대학관리</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript" src="/js/paging.js"></script> 
<script type="text/javascript">
$(function(){
    searchList("1");
    
    $("#sch_btn").click(function(){
      searchList("1");
    });
    
    $("#search_input").keypress(function(e){
      if(e.which==13){
        searchList("1");
      }
    });   
});

function searchList(nSelect){
  $.ajax({
    url:'/admin/univ/univ_info_list'
    ,type:'get'
    ,data:{
      nSelect:nSelect,  
      SCH:$(".search_input2").val().trim()
    }
    ,success:function(html){
      $(".content_list").html(html);
      var size=$("#nMaxRecordCnt").val();
      $(".total_cnt > span").html(size);
      /*paging*/
      var nMaxRecordCnt=$("#nMaxRecordCnt").val();
      var nMaxVCnt=$("#nMaxVCnt").val();
      paging(nSelect,nMaxRecordCnt,nMaxVCnt);      
    }
  });
}

function goDetail(UNIV_NUM){
	location.href="/admin/univ/univ_info_detail?UNIV_NUM="+UNIV_NUM;
}
</script>
</head>
<body>
<!-- top header -->
<jsp:include page="/WEB-INF/views/inc/ad_header.jsp">
  <jsp:param value="selected" name="menu_1"/>
</jsp:include>
<!-- sidebar -->
<jsp:include page="/WEB-INF/views/inc/ad_sidebar2.jsp">
  <jsp:param value="selected" name="memu_1"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>대학관리</span>
      <div class="header_search">
        <label class="hidden_label" for="search_input">대학명을 검색해주세요</label>
        <input class="search_input2" placeholder="대학명을 검색해주세요" id="search_input" type="text">
        <button class="base_btn" id="sch_btn">검색</button>
      </div>
    </div>
    <div class="content">
      <div class="btn_wrap">
       <div class="total_cnt">검색결과<span></span></div>
       <div class="btn_container">
         <a class="base_btn" href="/admin/univ/univ_info_add">추가</a>
       </div>
      </div>
      <div class="content_list"></div>
    </div>
  </div>
</section>
</body>
</html>