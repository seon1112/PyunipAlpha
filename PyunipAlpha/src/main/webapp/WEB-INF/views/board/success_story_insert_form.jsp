<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>합격 수기</title>
<link rel="stylesheet" href="/css/board.css" type="text/css">
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/summernote.jsp"></jsp:include>
<style>
html,body{font-family: "Pretendard" !important;}
ul{margin:0;}
a{color: var(--Gray-80); text-decoration: none!important;}
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
  
  if($.trim($("#SUC_YY").val())==''){
    alert("편입년도를 입력해주세요.");
    $("#SUC_YY").focus();
    return;
  }
  
  if($.trim($("#SUC_UNIV").val())==''){
    alert("편입 대학교를 입력해주세요.");
    $("#SUC_UNIV").focus();
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
  
  var formData = new FormData($("#sucForm")[0]);
  $.ajax({
    type: 'post'
    ,enctype : "multipart/form-data"    
    ,url: "/user/success_story"
    ,data : formData
    ,processData : false
    ,contentType : false    
    ,success : function(msg){
      if(msg == ""){
        location.href="/board/success_story_form";
      }else{
        alert(msg);
      }
    },error: function(html){
      alert("error:"+html);
    }
  });   
}
</script>
</head>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp">
  <jsp:param value="on" name="on_4"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<jsp:include page="/WEB-INF/views/inc/title_box.jsp">
  <jsp:param value="banner_selected" name="menu_5"/>
</jsp:include>
<div class="content">
  <form id="sucForm" name="sucForm" method="post" onsubmit="return false;">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
  <input type="hidden" name="filenames" id="filenames">
  <input type="hidden" id="USER_NUM" value="${USER_NUM}">
  <input type="hidden" name="BRD_CTG" value="4">
    <div class="content_wrap">
      <div class="insert_info_wrap">
        <div class="info_item">
          <div class="info_title">편입년도</div>
          <label class="hidden_label" for="SUC_YY">편입년도 입력해주세요</label>
          <input type="text" class="search_input2"  name="SUC_YY" id="SUC_YY" placeholder="0000년">          
        </div>
        <div class="info_item">
          <div class="info_title">편입대학교</div>
          <label class="hidden_label" for="SUC_UNIV">편입대학교 입력해주세요</label>
          <input type="text" class="search_input2" name="SUC_UNIV" id="SUC_UNIV" placeholder="00대학교">
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
      </div>
    </div>
    <div class="footer_btn_box">
      <button type="button" onclick="insertBoard()" class="org_large_btn">저장</button>
      <a href='/board/success_story_form' class="gray_large_btn">취소</a>
    </div>
  </form>
</div>
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>