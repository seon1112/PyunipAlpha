<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>편입 스터디</title>
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<link rel="stylesheet" href="/css/board.css" type="text/css">
<script type="text/javascript" src="/js/paging.js"></script> 
<script>
$(function(){
  searchList("1");
  
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
 var ing = "N"; //모집중
 var mine= "N"; //내 게시물
 var heart="N"; //관심
 
  if($(".study_search_btn").hasClass("selected")){
    ing='Y';
  }
  
  if($("#my_item").hasClass("selected")){
    mine ="Y";
  }
  
  if($("#my_heart").hasClass("selected")){
	heart="Y";  
  }
  
  $.ajax({
    url:'/board/study_list',
    type:'get',
    data:{
        SCH:$(".search_input").val().trim()
        ,nSelect:nSelect  
        ,ING:ing
        ,MINE : mine
        ,BRD_CTG : $("#BRD_CTG").val()
        ,HEART : heart
    },
    success:function(html){
      $(".study_list").html(html);
      var size=$("#nMaxRecordCnt").val();
      
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

function ingList(){
	if(!$(".study_search_btn").hasClass("selected")){
		$(".study_search_btn").addClass("selected");
	}else{
		$(".study_search_btn").removeClass("selected");
	}
	
	searchList("1");
}

function goDetail(num){
  var nSelect;
  var li=$(".paging").find("li.select_on");
	
  if($(li).data("page") != ''){
	nSelect=$(li).data("page");
  }else{
	nSelect="1";
  }
	
  location.href='/board/study_detail?BRD_NUM='+num+"&nSelect="+nSelect;
}

function insertStudy(){
  var USER_NUM=$("#USER_NUM").val();
  if(USER_NUM =='' || USER_NUM == null){
	 if(!confirm("로그인이 필요한 서비스입니다. 로그인하시겠습니까?")){
		 return;
	 }else{
		 location.href='/user/board/study_insert';
	 }
  }	
  
  location.href='/user/board/study_insert';
}
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
  <jsp:param value="banner_selected" name="menu_2"/>
</jsp:include>
<div class="content" >
  <div class="content_list_wrap">
    <div class="board_btn_box search">
      <div class="filter_box">
        <div class="total_cnt" id="total_cnt">총 <span>0</span>건</div>
        <div class="btn_box">
	      <a class="plus_btn main_round_btn" href="javascript:insertStudy()">스터디만들기</a>
	      <button type="button" class="study_search_btn gray_round_btn" onclick="ingList()">모집 중</button>
        </div>
      </div>
      <div class="filter_container">
      	  <div class="inner_container">  
	          <c:if test="${m ne null}">
	          <button class="gray_square_btn my" id="my_item">내 스터디</button>
	          <button class="gray_square_btn heart" id="my_heart">관심</button>
	          <div class="filter_middle_line"></div>
		      </c:if>      
	      </div>  
	      <label class="hidden_label" for="search_input">제목 검색</label>
	      <input class="search_input" id="search_input" type="text" placeholder="제목을 검색해주세요">
      </div>
    </div>  
    <div class="study_list"></div>
  </div>
</div>
<input type="hidden" id="USER_NUM" value="${USER_NUM}">
<input type="hidden" id="BRD_CTG" value="1">
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>