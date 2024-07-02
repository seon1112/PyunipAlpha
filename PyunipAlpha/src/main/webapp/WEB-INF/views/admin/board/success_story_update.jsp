<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>[관리자페이지]합격 수기</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript" src="/js/ad_board.js"></script> 
<jsp:include page="/WEB-INF/jsp/summernote.jsp"></jsp:include>
<style>
html,body{font-family: "Pretendard" !important;}
ul{margin:0;}
a{color: var(--Gray-80); text-decoration: none!important;}
</style>
<script type="text/javascript">
$(function(){
	$("#insert_p_btn").click(function(){
		updateBoard();
	});
	loadSummernote();
});

function updateBoard(){
   var USER_NUM=$("#USER_NUM").val();
   if(USER_NUM =='' || USER_NUM == null){
     alert("로그인이 필요한 서비스입니다.");
     return;  
   }
   
//	유효성 검사
   if($.trim($("#title").val())==''){
     alert("제목을 입력해주세요");
     $("#title").focus();
     return;
   }
   
   if($.trim($("#SUC_YY").val())==''){
     alert("편입년도를 입력해주세요.");
     return;
   }
   
   if($.trim($("#SUC_UNIV").val())==''){
     alert("편입 대학교를 입력해주세요.");
     return;
   } 
 	   
  
  var filenames=$("#filenames").val();
  filenames = filenames + savedFileName;
  $("#filenames").val(filenames);
  
  var formData = new FormData($("#sucForm")[0]);
  
  $.ajax({
    type: 'put'
    ,enctype : "multipart/form-data"    
    ,url: "/admin/success_story"
    ,data : formData
    ,processData : false
    ,contentType : false    
    ,success : function(msg){
      if(msg == ""){
    	alert("성공적으로 수정되었습니다.");
    	var BRD_NUM=$("#BRD_NUM").val(); 
        location.href="/admin/board/success_story_detail?BRD_NUM=" + BRD_NUM;
      }else{
        alert(msg);
      }
    }
  	,error: function(html){
      alert("error:"+html);
    }
  }); 
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
	<jsp:param value="selected" name="memu_5"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>합격 수기</span>
    </div>
	<div class="content">
		<div class="content_box">
		<c:choose>
		 <c:when test="${error eq null }">
         <form id="sucForm" name="sucForm" onsubmit="return false;">
         <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
         <input type="hidden" name="filenames" id="filenames" value="${filenames }">
         <input type="hidden" id="BRD_CTG" name="BRD_CTG" value="4">
         <input type="hidden" id="BRD_NUM" name="BRD_NUM" value="${s.BRD_NUM}">
         <input type="hidden" id="USER_NUM" value="${USER_NUM}">	
         <div class="write_box">
           <div class="write border">
             <div class="title">편입년도</div>
             <div class="input_box">
	          <label class="hidden_label" for="SUC_YY">편입년도을 입력해주세요</label>
	          <input type="text" class="search_input2" placeholder="편입년도을 입력해주세요" name="SUC_YY" id="SUC_YY" value="${s.SUC_YY }">             
             </div>
           </div>
           <div class="write border">
             <div class="title">편입대학교</div>
             <div class="input_box">
	          <label class="hidden_label" for="SUC_UNIV">편입대학교 입력해주세요</label>
	          <input type="text" class="search_input2" placeholder="편입대학교을 입력해주세요" name="SUC_UNIV" id="SUC_UNIV" value="${s.SUC_UNIV }">             
             </div>
           </div>
           <div class="write border">
             <div class="title">제목</div>
             <div class="input_box">
	          <label class="hidden_label" for="title">제목을 입력해주세요</label>
	          <input type="text" class="search_input2" placeholder="제목을 입력해주세요" name="TITLE" id="title" value="${s.TITLE }">             
             </div>
           </div>
           <div class="txtarea" style="border:none;">
             <label class="hidden_label" for="summernote">내용을 입력해주세요</label> 
             <textarea  id="summernote" name="CONTENT">${s.CONTENT}</textarea>
           </div>
         </div>
         <div class="btn_box">
           <button type="button" class="base_btn" id="insert_p_btn">저장</button>
           <a class="gray_btn"  href="/admin/board/ad_success_story">취소</a>        
         </div>
         </form>			   
		 </c:when>
		 <c:otherwise>
		   <div>${error }</div>
		 </c:otherwise>
		</c:choose>
		</div>
	</div>
  </div>
</section>
</body>
</html>