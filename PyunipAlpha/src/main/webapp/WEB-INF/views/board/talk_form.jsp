<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>편입 이야기</title>
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<link rel="stylesheet" href="/css/board.css" type="text/css">
<script type="text/javascript" src="/js/paging.js"></script> 
<style type="text/css">
.talk_list .talk_item .talk_content > p > span{
    font-family: "Pretendard" !important;	
	font-size : 14px !important;
	color : var(--Gray-50) !important;
	font-weight: 400 !important;
}
.talk_list .talk_item .talk_content > p > img{
	display: none !important;
}
</style>
<script>
$(function(){
	if($("#nSelect").val()==''){
		searchList("1");
	}else{
		searchList($("#nSelect").val());
	}
	
  $(".search_input").keypress(function(e){
	  if(e.which==13){
		  searchList("1");
	  }
  });
  
  $("#my_item").click(function(){
	  if($(this).hasClass("selected")){
		  $(this).removeClass("selected");
	  }else{
		  $(this).addClass("selected");
	  }
	  searchList("1");
  });
  
  $("#my_heart").click(function(){
    if($(this).hasClass("selected")){
       $(this).removeClass("selected");
     }else{
       $(this).addClass("selected");
     }
     searchList("1");
  });  
});

function searchList(nSelect){
	var dv = $(".dropdown2 .optionList > li.selected").data("value");
	var sch = $('.search_input').val().trim();
	var mine = "N"; //내 게시물 
	var heart= "N"; //관심
	
	if($("#my_item").hasClass("selected")){
		mine ="Y"
	}else{
		min="N";
	}
	
   if($("#my_heart").hasClass("selected")){
	   heart="Y";  
   }else{
	   heart="N";
   }
		  	
	
  $.ajax({
    url:'/board/talk_list',
    type:'get',
    data:{
        SCH      : sch
        ,nSelect : nSelect  
        ,DV      : dv
        ,MINE    : mine 	
        ,BRD_CTG : $("#BRD_CTG").val()
        ,HEART : heart
    },
    success:function(html){
      $(".talk_list").html(html);
      var size=$("#size").val();
      $("#total_cnt > span").html(size);
      /*paging*/
      var nMaxRecordCnt=$("#nMaxRecordCnt").val();
      var nMaxVCnt=$("#nMaxVCnt").val();
      paging(nSelect,nMaxRecordCnt,nMaxVCnt);
      
      // 상위 목록 focus 및 스크롤 이동
      var firstItem = $(".header_nav");
      firstItem.focus();
      if (firstItem.length) {
          firstItem[0].scrollIntoView({ behavior: 'auto', block: 'start' });
      }      
    }
  });
}

function goTalkDetail(num){
	var nSelect;
 	var li=$(".paging").find("li.select_on");
 	
	if($(li).data("page") != ''){
		nSelect=$(li).data("page");
	}else{
		nSelect="1";
	}
	location.href='/board/talk_detail?BRD_NUM='+num+"&nSelect="+nSelect;
}

function insertTalk(){
  var USER_NUM=$("#USER_NUM").val();
  if(USER_NUM =='' || USER_NUM == null){
	 if(!confirm("로그인이 필요한 서비스입니다. 로그인하시겠습니까?")){
		 return;
	 }else{
		 location.href='/user/board/talk_insert';
	 }
  }	
	location.href='/user/board/talk_insert';
}

window.onclick = function(event) {
  if (!event.target.matches('.dropdown2') && !event.target.matches('.dropdown2 > label')) {
	  $(".dropdown2").removeClass("active");
  }
}

$(document).on("click",".dropdown2",function(){
	if($(this).hasClass("active")){
		$(this).removeClass("active");
	}else{
		$(this).addClass("active");
	}	
});

$(document).on("click", ".dropdown2 > label", function(event){
    event.stopPropagation(); //상위로 이벤트 전파x
    $(this).parent().toggleClass("active");
});

$(document).on("click",".optionItem",function(){
	var selectOption = $(this).text();
	$(this).siblings().removeClass("selected");
	$(this).addClass("selected");
	$(this).closest(".dropdown2").find(".label").text(selectOption);
	$(this).closest(".dropdown2").removeClass("active");	
	
	searchList("1");
});
</script>
</head>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp">
  <jsp:param value="on" name="on_3"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<jsp:include page="/WEB-INF/views/inc/title_box.jsp">
  <jsp:param value="banner_selected" name="menu_3"/>
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
	        <c:if test="${m ne null }">
	        <button class="gray_square_btn my" id="my_item">내 이야기</button>      
	        <button class="gray_square_btn heart" id="my_heart">관심</button>
	        <div class="filter_middle_line"></div>
	        </c:if>
	      	<label class="hidden_label" for="dv">구분</label>
	      	<div class="dropdown2" id="dv">
	      		<label class="label">전체</label>
	      		<ul class="optionList">
	      			<li class="optionItem selected" data-value="N">전체</li>
	      			<li class="optionItem" data-value="F">자유주제</li>
	      			<li class="optionItem" data-value="Q">질문하기</li>
	      		</ul>
	      	</div>
      	</div>
        <label class="hidden_label" for="search_input">제목을 검색해주세요</label>
        <input class="search_input" id="search_input" type="text" placeholder="제목을 검색해주세요">
      </div>
    </div>
    <div class="talk_list"></div>
  </div>
</div>
<input type="hidden" id="USER_NUM" value="${USER_NUM}">
<input type="hidden" id="BRD_CTG" value="0">
<input type="hidden" id="nSelect" value="${nSelect}">
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>