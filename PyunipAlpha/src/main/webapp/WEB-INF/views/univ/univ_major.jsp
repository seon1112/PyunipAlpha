<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>편입 학과 정보</title>
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<script type="text/javascript" src="/js/paging.js"></script> 
<link rel="stylesheet" href="/css/univ.css" type="text/css">
<script>
$(function(){
	searchList(1);
	
	$("#search_btn").click(function(){
		searchList(1);
	});
});

function searchList(nSelect){
	$.ajax({
		type:'get',
		url:'/univ/major_list',
		data:{
			nSelect  : nSelect
		   ,YEAR     : $("#sel_year > label").attr("data-value")
		   ,SERIES   : $("#sel_series > label").attr("data-value")
		   ,SLT_WAY  : $("#sel_way > label").attr("data-value")
		   ,UNIV_NAME: $("#univ > label").attr("data-value")
		   ,MAJOR_NM : $("#major > label").attr("data-value")
		},
		success:function(html){
			$(".search_list_box").html(html);
			var nMaxRecordCnt=$("#nMaxRecordCnt").val();
	        var nMaxVCnt=$("#nMaxVCnt").val();
	        paging(nSelect,nMaxRecordCnt,nMaxVCnt);   
	        
	        // 상위 목록 focus 및 스크롤 이동
	        var firstItem = $(".header_nav");
	        firstItem.focus();
	        if (firstItem.length) {
	            firstItem[0].scrollIntoView({ behavior: 'auto', block: 'start' });
	        }    	        
		},
		error:function(html){
			alert("error:"+html);
		}
	});
}

window.onclick = function(event) {
  if (!event.target.matches('.dropdown2') && !event.target.matches('.dropdown2 > label')) {
	  $(".dropdown2").removeClass("active");
  }
}

$(document).on("click",".dropdown2",function(){
	$(this).parent().parent().find(".dropdown2").removeClass("active");
	$(this).toggleClass("active");
});

$(document).on("click", ".dropdown2 > label", function(event){
    event.stopPropagation(); //상위로 이벤트 전파x
    $(this).parent().parent().parent().find(".dropdown2").removeClass("active");
    $(this).parent().toggleClass("active");
});

$(document).on("click",".optionItem",function(){
	var selectOption = $(this).text();
	var value=$(this).data("value");
	
	$(this).siblings().removeClass("selected");
	$(this).addClass("selected");
	
    var label = $(this).closest(".dropdown2").find(".label");
    label.text(selectOption);
    label.attr("data-value", value);
    
	$(this).closest(".dropdown2").removeClass("active");	
});
</script>
</head>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp">
  <jsp:param value="on" name="on_1"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<jsp:include page="/WEB-INF/views/inc/title_box.jsp">
  <jsp:param value="banner_selected" name="menu_6"/>
</jsp:include>
<div class="content">
  <div class="search_btn_wrap">
    <div class="search_btn_box">
      <label for="sel_year" class="hidden_label">년도 선택</label>
      <div class="dropdown2" id="sel_year">
     	<label class="label" data-value="">년도</label>
      	<ul class="optionList">
      		<li class="optionItem" data-value="">전체</li>
      		<li class="optionItem" data-value="2025">2025</li>
      		<li class="optionItem" data-value="2024">2024</li>
      		<li class="optionItem" data-value="2023">2023</li>
      		<li class="optionItem" data-value="2022">2022</li>
      	</ul>
      </div>      
      <label for="sel_series" class="hidden_label">계열 선택</label>
      <div class="dropdown2" id="sel_series">
     	<label class="label" data-value="">계열 선택</label>
      	<ul class="optionList">
      	    <li class="optionItem" data-value="">전체</li>
      		<li class="optionItem" data-value="N">자연</li>
      		<li class="optionItem" data-value="H">인문</li>
      		<li class="optionItem" data-value="E">예체능</li>
      	</ul>
      </div>         
      <label for="sel_way" class="hidden_label">편입구분</label>
      <div class="dropdown2" id="sel_way">
     	<label class="label" data-value="">편입구분</label>
      	<ul class="optionList">
      	    <li class="optionItem" data-value="4">전체</li>
      		<li class="optionItem" data-value="0">일반편입</li>
      		<li class="optionItem" data-value="1">학사편입</li>
      		<li class="optionItem" data-value="2">특별편입</li>
      	</ul>
      </div>        
    </div>
    <div class="search_btn_box2">
      <label class="hidden_label" for="univ">대학 선택</label>
      <div class="dropdown2" id="univ">
     	<label class="label" data-value="">대학명</label>
      	<ul class="optionList">
      	   <li class="optionItem" data-value="">전체</li>
           <c:forEach var="u" items="${univList }">
           <li class="optionItem" data-value="${u.UNIV_NAME }"><c:out value="${u.UNIV_NAME }"></c:out></li>
           </c:forEach>      	
      	</ul>
      </div>       
      <label class="hidden_label" for="major">학부명 선택</label>
      <div class="dropdown2" id="major">
     	<label class="label" data-value="">학부명</label>
      	<ul class="optionList">
      	    <li class="optionItem" data-value="">전체</li>
            <c:forEach var="d" items="${majorList }">
        	<li class="optionItem" data-value="${d.MAJOR_NM }"><c:out value="${d.MAJOR_NM }"></c:out></li>
            </c:forEach>      	
      	</ul>
      </div>       
      <button type="button" class="org_btn" id="search_btn">검색</button>    
    </div>
  </div>
  <p class="search_sub_title">전체로 검색하고 싶은 부분은 선택 안하시면 됩니다.</p>
  <div class="search_list_box"></div>
</div>
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>