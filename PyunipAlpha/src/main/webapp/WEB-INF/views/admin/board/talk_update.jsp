<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>[관리자페이지]편입 이야기</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript" src="/js/ad_board.js"></script> 
<jsp:include page="/WEB-INF/jsp/summernote.jsp"></jsp:include>
<script type="text/javascript">
$(function(){
	$("#insert_p_btn").click(function(){
		updateBoard();
	});
	$("#cancel_p_btn").click(function(){
		location.reload();
	});
	loadSummernote();
});

function updateBoard(){
   var USER_NUM=$("#USER_NUM").val();
   if(USER_NUM =='' || USER_NUM == null){
     alert("로그인이 필요한 서비스입니다.");
     return;  
   }		
  if($("#title").val()==''){
    alert("제목을 입력해주세요");
    return;
  }	
  
  var formData=new FormData($("#boardForm")[0]);  
  formData.append("UPT_USER_NUM",USER_NUM);
  
  $.ajax({
    type : 'put'
    ,enctype : "multipart/form-data"
    ,url:'/admin/talk'
    ,data : formData
    ,processData : false
    ,contentType : false
    ,success : function(msg){
      if(msg != ""){
        alert(msg);
      }else{
        alert("성공적으로 수정되었습니다."); 
    	var BRD_NUM=$("#BRD_NUM").val();
        location.href="/admin/board/talk_detail?BRD_NUM="+BRD_NUM;
      }
    }
    ,error : function(e){
      alert("error:" + e);
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
	<jsp:param value="selected" name="memu_3"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>편입이야기</span>
    </div>
	<div class="content">
		<div class="content_box">
		<c:choose>
		 <c:when test="${error eq null }">
         <form id="boardForm" name="boardForm" enctype="multipart/form-data" onsubmit="return false;">
         <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
         <input type="hidden" name="filenames" id="filenames" value="${filenames }">
         <input type="hidden" id="BRD_CTG" name="BRD_CTG" value="0">
         <input type="hidden" id="BRD_NUM" name="BRD_NUM" value="${t.BRD_NUM}">
         <div class="write_box">
           <div class="write border">
             <div class="title">구분</div>
             <div class="input_box">
               <div class="txtbox_wrap">
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
           </div>
           <div class="write">
             <div class="title">제목</div>
             <div class="input_box">
               <label class="hidden_label" for="title">제목 입력</label>
               <input type="text" placeholder="제목을 입력해주세요" name="TITLE" id="title" class="search_input2" value="${t.TITLE}">
             </div>
           </div>
           <div class="txtarea" style="border:none;">
             <label class="hidden_label" for="summernote">내용을 입력해주세요</label> 
             <textarea  id="summernote" name="CONTENT">${t.CONTENT}</textarea>
           </div>
         </div>
         <div class="btn_box">
           <button class="base_btn" id="insert_p_btn" type="button">저장</button>
           <a class="gray_btn"  href="/admin/board/ad_talk">취소</a>        
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
<input type="hidden" id="USER_NUM" value="${USER_NUM}">
</body>
</html>