<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>편입 스터디</title>
<link rel="stylesheet" href="/css/board.css" type="text/css">
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/summernote.jsp"></jsp:include>
<style>
html,body{font-family: "Pretendard" !important;}
ul{margin:0;}
a{color: var(--Gray-80); text-decoration: none!important;}
label{margin:0px;}
</style>
<script>
$(function(){
   loadSummernote();
});

function insertBoard(){
  /*로그인 여부 재확인*/
  var USER_NUM=$("#USER_NUM").val();
  if(USER_NUM =='' || USER_NUM == null){
    alert("로그인이 종료되었습니다. 다시 로그인 해주세요.");
    return;  
  }	
//   유효성 검사
  if($.trim($("#title").val())==''){
    alert("제목을 입력해주세요");
    $("#title").focus();
    return;
  }
  
  if($.trim($("#ST_DTM").val())==''){
    alert("진행기간을 선택해주세요.");
    $("#ST_DTM").focus();
    return;
  }
  
  if($.trim($("#ED_DTM").val())==''){
    alert("진행기간을 선택해주세요.");
    $("#ED_DTM").focus();
    return;
  } 

  var size=$.trim($("#APY_SIZE").val());
  if(size==''){
    alert("모집인원을 입력해주세요.");
    $("#APY_SIZE").focus();
    return;
  }else if(size == '0'){
	alert("모집인원은 0명 이상이여야 합니다.");
	$("#APY_SIZE").focus();
	return;
  }else if(size == '101'){
	alert("모집인원은 최대 100명까지 가능합니다.");
	$("#APY_SIZE").focus();
	return;
  }
  
  if($.trim($("#APPLY_ED_DTM").val())==''){
    alert("모집 마감일을 선택해주세요.");
    $("#APPLY_ED_DTM").focus();
    return;
  }
  
  if($("#PCD_WAY > label").attr("data-value")==''){
	alert("진행방법을 선택해주세요");
	$("#PCD_WAY").focus();
	return;	  
  }
  
  var filenames=$("#filenames").val();
  filenames = filenames + savedFileName;
  $("#filenames").val(filenames);
  
  var formData = new FormData($("#studyForm")[0]);
  $.ajax({
    type: 'post'
    ,enctype : "multipart/form-data"    
    ,url: "/user/study"
    ,data : formData
    ,processData : false
    ,contentType : false    
    ,success : function(msg){
      if(msg == ""){
        location.href="/board/study_form";
      }else{
        alert(msg);
      }
    },error: function(html){
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
	var value=$(this).data("value");
	
	$(this).siblings().removeClass("selected");
	$(this).addClass("selected");
	
    var label = $(this).closest(".dropdown2").find(".label2");
    label.text(selectOption);
    label.attr("data-value", value);
	
	$("#PCD_WAY_hidden").val(value);
	$(this).closest(".dropdown2").removeClass("active");	
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
  <jsp:param value="banner_selected" name="menu_2"/>
</jsp:include>
<div class="content">
  <form id="studyForm" name="studyForm" method="post" onsubmit="return false;">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
  <input type="hidden" name="filenames" id="filenames">
  <input type="hidden" name="PCD_WAY" id="PCD_WAY_hidden" value="0">
    <div class="content_wrap">
      <div class="insert_info_wrap">
        <div class="info_item">
          <div class="info_title">진행기간</div>
          <div>
            <label class="hidden_label" for="ST_DTM">시작일을 입력해주세요</label>
            <label class="hidden_label" for="ED_DTM">종료일을 입력해주세요</label>
            <input type="date" name="ST_DTM" id="ST_DTM"> ~ <input type="date" name="ED_DTM" id="ED_DTM">
          </div>	
        </div>
        <div class="info_item">
          <div class="info_title">모집인원</div>
          <label class="hidden_label" for="APY_SIZE">모집인원을 입력해주세요</label>
          <input type="number" placeholder="숫자만 입력해주세요" min="1" max="100" name="APY_SIZE" id="APY_SIZE">
        </div>
        <div class="info_item">
          <div class="info_title">진행방법</div>
	      <label for="PCD_WAY" class="hidden_label">진행방법</label>
	      <div class="dropdown2" id="PCD_WAY" style="height: 100%;">
	     	<label class="label2"  data-value="0">온라인</label>
	      	<ul class="optionList" style="top:53px;">
	      		<li class="optionItem" data-value="0">온라인</li>
	      		<li class="optionItem" data-value="1">오프라인</li>
	      		<li class="optionItem" data-value="2">온라인+오프라인</li>
	      	</ul>
	      </div>            
        </div>
        <div class="info_item">
          <div class="info_title">모집 마감일</div>
          <label class="hidden_label" for="APPLY_ED_DTM">모집 마감일을 입력해주세요</label>
          <input type="date" name="APPLY_ED_DTM" id="APPLY_ED_DTM">
        </div>
      </div>
      <div class="study_info_midle_line"></div>
      <div class="insert_content_wrap">
        <label class="hidden_label" for="title">제목을 입력해주세요</label>
        <input type="text" class="search_input2" placeholder="제목을 입력해주세요" name="TITLE" id="title">
        <label class="hidden_label" for="summernote">내용을 입력해주세요</label>
        <textarea id="summernote" name="CONTENT"></textarea>    
      </div>
      <div class="caution">
      개인정보를 남기실 경우 개인 정보 도난의 우려가 있으므로, 기재하셨을 시 모두 삭제해주세요.
      스터디원 모집은 익명채팅을 통해 모집하실 것을 추천드립니다. 
      </div>
    </div>
    <div class="footer_btn_box">
      <button type="button" onclick="insertBoard()" class="org_large_btn">저장</button>
      <a href='/board/study_form' class="gray_large_btn">취소</a>
    </div>
  </form>
</div>
<input type="hidden" id="USER_NUM" value="${USER_NUM}">
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>