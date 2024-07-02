<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>편입 이야기</title>
<link rel="stylesheet" href="/css/board.css" type="text/css">
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/summernote.jsp"></jsp:include>
<style>
html,body{font-family: "Pretendard" !important;}
ul{margin:0;}
a{color: var(--Gray-80); text-decoration: none!important;}
label{margin:0px;}
</style>
<script type="text/javascript">
$(function(){
  loadSummernote();
  
  $("#insert_t_btn").click(function(){
	  insertTalk();
  });
});

function insertTalk(){
	  var USER_NUM=$("#USER_NUM").val();
	  if(USER_NUM =='' || USER_NUM == null){
	    alert("로그인이 필요한 서비스입니다.");
	    return;  
	  }
	  
	 if($.trim($("#title").val())==''){
		 alert("제목을 입력해주세요");
		 $("#title").focus();
		 return;
	 }

   var filenames=$("#filenames").val();
   filenames = filenames + savedFileName;
   $("#filenames").val(filenames);
 
   var formData = new FormData($("#talkForm")[0]);
   $.ajax({
	 type: 'post'
	 ,enctype : "multipart/form-data"		 
	 ,url: "/user/talk"
     ,data : formData
     ,processData : false
     ,contentType : false	   
     ,success : function(msg){
	   if(msg == ""){
		   location.href="/board/talk_form";
	   }else{
		   alert(msg);
	   }
     },error: function(html){
	     alert("error:"+html);
     }
   }); 
}

function goList(){
  location.href='/board/talk_form';
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
	
	$("#BRD_DV_hidden").val(value);
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
<jsp:include page="/WEB-INF/views/inc/title_box.jsp">
  <jsp:param value="banner_selected" name="menu_3"/>
</jsp:include>
<div class="content">
  <form id="talkForm" name="talkForm" method="post" onsubmit="return false;">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="filenames" id="filenames">
    <input type="hidden" name="BRD_DV" id="BRD_DV_hidden" value="F">
    <div class="content_wrap">
      <div class="insert_info_wrap">
        <div class="info_item">
          <div class="info_title">이야기 주제</div>
	      <label for="BRD_DV" class="hidden_label">이야기주제</label>
	      <div class="dropdown2" id="BRD_DV">
	     	<label class="label2" data-value="F">자유주제</label>
	      	<ul class="optionList">
	      		<li class="optionItem" data-value="F">자유주제</li>
	      		<li class="optionItem" data-value="Q">질문하기</li>
	      	</ul>
	      </div>            
        </div>
      </div>
      <div class="study_info_midle_line"></div>
      <div class="insert_content_wrap">
        <label class="hidden_label" for="title">제목을 입력해주세요</label>
        <input type="text" placeholder="제목을 입력해주세요" class="search_input2" id="title" name="TITLE">
        <label class="hidden_label" for="summernote">내용을 입력해주세요</label>
        <textarea id="summernote" name="CONTENT"></textarea>
      </div>
    </div>
    <div class="footer_btn_box">
      <button type="button" class="org_large_btn" id="insert_t_btn">저장</button>
      <a class="gray_large_btn" href="javascript:goList()" >취소</a>
    </div>
    <input type="hidden" name="BRD_CTG" value="0">
    </form>
</div>
<input type="hidden" id="USER_NUM" value="${USER_NUM}">
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>