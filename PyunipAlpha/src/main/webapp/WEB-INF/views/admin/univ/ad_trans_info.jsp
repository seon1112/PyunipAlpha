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
	
	$("#univ").change(function(){
		searchList("1");
	});
	$("#major").change(function(){
		searchList("1");
	});
});
function searchList(nSelect){
	var UNIV_NAME = $("#univ option:selected").val();
	var MAJOR_NM = $("#major option:selected").val();
	
	$.ajax({
		url:'/admin/univ/trans_info_list',
		type:'get',
		data : {
			UNIV_NAME : UNIV_NAME,
			MAJOR_NM : MAJOR_NM,
			nSelect  : nSelect
		},
		success:function(html){
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
function goDetail(TRANS_NUM , UNIV_NUM){
	location.href='/admin/univ/trans_info_detail?UNIV_NUM='+UNIV_NUM+'&TRANS_NUM='+TRANS_NUM;
}
function add(){
	  var USER_NUM=$("#USER_NUM").val();
	  if(USER_NUM =='' || USER_NUM == null){
		 if(!confirm("로그인이 필요한 서비스입니다. 로그인하시겠습니까?")){
			 return;
		 }else{
			 location.href='/admin/trans_info_add';
		 }
	  }		
	location.href="/admin/trans_info_add";
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
  <jsp:param value="selected" name="memu_2"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>대학관리</span>
      <div class="header_search">
          <label class="hidden_label" for="univ">대학 선택</label>
	      <select id="univ" class="dropdown1">
	        <option value="">대학명</option>
	        <c:forEach var="u" items="${univList }">
	        <option value="${u.UNIV_NAME }">${u.UNIV_NAME }</option>
	        </c:forEach>
	      </select>
	      <label class="hidden_label" for="major">학부명 선택</label>
	      <select id="major" class="dropdown1">
	        <option value="">학부명</option>
	        <c:forEach var="d" items="${majorList }">
	        <option value="${d.MAJOR_NM }">${d.MAJOR_NM }</option>
	        </c:forEach>      
	      </select>
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