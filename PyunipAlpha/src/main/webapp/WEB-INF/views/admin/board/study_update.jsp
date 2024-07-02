<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>[관리자페이지]편입 스터디</title>
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
   
  //유효성 검사
  if($.trim($("#title").val())==''){
    alert("제목을 입력해주세요");
    $("#title").focus();
    return;
  }
  if($.trim($("#ST_DTM").val())==''){
    alert("진행기간을 선택해주세요.");
    return;
  }
  if($.trim($("#ED_DTM").val())==''){
    alert("진행기간을 선택해주세요.");
    return;
  } 
  if($.trim($("#APY_SIZE").val())==''){
    alert("모집인원을 입력해주세요.");
    return;
  }
  if($.trim($("#APPLY_ED_DTM").val())==''){
    alert("모집 마감일을 선택해주세요.");
    return;
  }
  
  var filenames=$("#filenames").val();
  filenames = filenames + savedFileName;
  $("#filenames").val(filenames);
  
  var formData = new FormData($("#studyForm")[0]);
  
  $.ajax({
    type: 'put'
    ,enctype : "multipart/form-data"    
    ,url: "/admin/study"
    ,data : formData
    ,processData : false
    ,contentType : false    
    ,success : function(msg){
      if(msg == ""){
    	alert("성공적으로 수정되었습니다.");
    	var BRD_NUM=$("#BRD_NUM").val(); 
        location.href="/admin/board/study_detail?BRD_NUM=" + BRD_NUM;
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
	<jsp:param value="selected" name="memu_4"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>편입 스터디</span>
    </div>
	<div class="content">
		<div class="content_box">
		<c:choose>
		 <c:when test="${error eq null }">
         <form id="studyForm" name="studyForm" onsubmit="return false;">
         <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
         <input type="hidden" name="filenames" id="filenames" value="${filenames }">
         <input type="hidden" id="BRD_CTG" name="BRD_CTG" value="1">
         <input type="hidden" id="BRD_NUM" name="BRD_NUM" value="${s.BRD_NUM}">
         <input type="hidden" id="USER_NUM" value="${USER_NUM}">	
         <div class="write_box">
           <div class="write border">
             <div class="title">제목</div>
             <div class="input_box">
	          <label class="hidden_label" for="title">제목을 입력해주세요</label>
	          <input type="text" class="search_input2" placeholder="제목을 입력해주세요" name="TITLE" id="title" value="${s.TITLE }">             
             </div>
           </div>
           <div class="write border">
             <div class="title">진행기간</div>
             <div class="input_box">
	            <label class="hidden_label" for="ST_DTM">시작일을 입력해주세요</label>
	            <label class="hidden_label" for="ED_DTM">종료일을 입력해주세요</label>
	            <input type="date" name="ST_DTM" id="ST_DTM"  class="search_input2" value="${s.ST_DTM}"> ~ <input type="date" name="ED_DTM" id="ED_DTM"  class="search_input2" value="${s.ED_DTM}">             
             </div>
           </div>
           <div class="write border">
             <div class="title">모집인원</div>
             <div class="input_box">
	          <label class="hidden_label" for="APY_SIZE">모집인원을 입력해주세요</label>
	          <input type="number" placeholder="숫자만 입력해주세요" min="0" name="APY_SIZE" id="APY_SIZE"  class="search_input2" value="${s.APY_SIZE}">             
             </div>
           </div>
           <div class="write border">
             <div class="title">진행방법</div>
             <div class="input_box">
	          <select name="PCD_WAY" class="dropdown1">
	            <c:if test="${s.PCD_WAY eq '0' }">
	              <option value="0" selected>온라인</option>
	              <option value="1">오프라인</option>
	              <option value="2">온라인+오프라인</option>            
	            </c:if>
	            <c:if test="${s.PCD_WAY eq '1' }">
	              <option value="0">온라인</option>
	              <option value="1" selected>오프라인</option>
	              <option value="2">온라인+오프라인</option>            
	            </c:if>
	            <c:if test="${s.PCD_WAY eq '2' }">
	              <option value="0">온라인</option>
	              <option value="1">오프라인</option>
	              <option value="2" selected>온라인+오프라인</option>            
	            </c:if>
	          </select>             
             </div>
           </div>
           <div class="write border">
             <div class="title">모집 마감일</div>
             <div class="input_box">
	          <label class="hidden_label" for="APPLY_ED_DTM">모집 마감일을 입력해주세요</label>
	          <input type="date" name="APPLY_ED_DTM" id="APPLY_ED_DTM"  class="search_input2" value="${s.APPLY_ED_DTM}">             
             </div>
           </div>
           <div class="txtarea" style="border:none;">
             <label class="hidden_label" for="summernote">내용을 입력해주세요</label> 
             <textarea  id="summernote" name="CONTENT">${s.CONTENT}</textarea>
           </div>
         </div>
         <div class="btn_box">
           <button type="button" class="base_btn" id="insert_p_btn">저장</button>
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
</body>
</html>