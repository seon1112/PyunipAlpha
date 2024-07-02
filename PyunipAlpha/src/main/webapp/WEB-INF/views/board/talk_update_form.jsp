<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
</style>
<script type="text/javascript">
$(function(){
  loadSummernote();
  
});

function insertTalk(nSelect){
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
     type: 'put'
     ,enctype : "multipart/form-data"    
     ,url: "/user/talk"
     ,data : formData
     ,processData : false
     ,contentType : false    
     ,success : function(msg){
       if(msg == ""){
    	   var BRD_NUM = $("#BRD_NUM").val();
         location.href="/board/talk_detail?BRD_NUM=" + BRD_NUM+"&nSelect="+nSelect;
       }else{
         alert(msg);
       }
     },error: function(html){
       alert("error:"+html);
     }
   }); 
}

function goList(nSelect){
  var BRD_NUM=$("#BRD_NUM").val(); 
  location.href='/board/talk_detail?BRD_NUM='+BRD_NUM+"&nSelect="+nSelect;
}
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
<c:choose>
   <c:when test="${error eq null }">
		<div class="content">
		  <form id="talkForm" name="talkForm" onsubmit="return false;">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
			<input type="hidden" name="filenames" id="filenames" value="${filenames }">
		    <input type="hidden" name="BRD_CTG" value="0">
		    <input type="hidden" id="BRD_NUM" name="BRD_NUM" value="${num}">		
		    <input type="hidden" name="REG_USER_NUM" value="${t.REG_USER_NUM}">  
		    <div class="content_wrap">
		      <div class="insert_info_wrap">
		        <div class="info_item">
		          <div class="info_title">이야기 주제</div>
		          <label id="BRD_DV" class="hidden_label">구분</label>
		          <select name="BRD_DV" id="BRD_DV" class="dropdown1">
		            <c:choose>
		              <c:when test="${t.BRD_DV eq 'F' }">
		                <option value="F" selected>자유주제</option>
		                <option value="Q">질문하기</option>
		              </c:when>
		              <c:otherwise>
		                <option value="F">자유주제</option>
		                <option value="Q" selected>질문하기</option>
		              </c:otherwise>
		            </c:choose>            
		          </select>
		        </div>
		      </div>
		      <div class="study_info_midle_line"></div>
		      <div class="insert_content_wrap">
		        <label class="hidden_label" for="title">제목을 입력해주세요</label>
		        <input type="text" class="search_input2" placeholder="제목을 입력해주세요" id="title" name="TITLE" value="${t.TITLE}">
		        <label class="hidden_label" for="summernote">내용을 입력해주세요</label>
		        <textarea id="summernote" name="CONTENT">${t.CONTENT}</textarea>
		      </div>
		    </div>
		    <div class="footer_btn_box">
		      <button type="button" class="org_large_btn" onclick="insertTalk('${nSelect}')" >저장</button>
		      <a class="gray_large_btn" href="javascript:goList('${nSelect}')">취소</a>
		    </div>
		    </form>
		</div>
   </c:when>
   <c:otherwise>
    <div class="content">${error }</div>
   </c:otherwise>
</c:choose>
<input type="hidden" id="USER_NUM" value="${USER_NUM}">
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>