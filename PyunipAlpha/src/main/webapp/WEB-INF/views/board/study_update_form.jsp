<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
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
</style>
<script>
$(function(){
   loadSummernote();
});

function goList(nSelect){
	var BRD_NUM=$("#BRD_NUM").val(); 
    location.href='/board/study_detail?BRD_NUM='+BRD_NUM+"&nSelect="+nSelect;
}

function insertBoard(nSelect){
   var USER_NUM=$("#USER_NUM").val();
   if(USER_NUM =='' || USER_NUM == null){
     alert("로그인이 필요한 서비스입니다.");
     return;  
   }	
// 	유효성 검사
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
  
  var filenames=$("#filenames").val();
  filenames = filenames + savedFileName;
  $("#filenames").val(filenames);
  
  var formData = new FormData($("#studyForm")[0]);
  $.ajax({
    type: 'put'
    ,enctype : "multipart/form-data"    
    ,url: "/user/study"
    ,data : formData
    ,processData : false
    ,contentType : false    
    ,success : function(msg){
      if(msg == ""){
    	  alert("성공적으로 수정되었습니다.");
    	  var BRD_NUM=$("#BRD_NUM").val(); 
          location.href="/board/study_detail?BRD_NUM="+ BRD_NUM+"&nSelect="+nSelect;
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
  <jsp:param value="on" name="on_3"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<jsp:include page="/WEB-INF/views/inc/title_box.jsp">
  <jsp:param value="banner_selected" name="menu_2"/>
</jsp:include>
<c:choose>
   <c:when test="${error eq null }">
	<div class="content">
	  <form id="studyForm" name="studyForm" onsubmit="return false;" >
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	    <input type="hidden" name="filenames" id="filenames" value="${filenames }">
	    <input type="hidden" name="BRD_CTG" value="1">
	    <input type="hidden" id="BRD_NUM" name="BRD_NUM" value="${s.BRD_NUM}">
	    <input type="hidden" id="USER_NUM" value="${USER_NUM}">	  
	    <input type="hidden" name="REG_USER_NUM" value="${s.REG_USER_NUM }">
	    <div class="content_wrap">
	      <div class="insert_info_wrap">
	        <div class="info_item">
	          <div class="info_title">진행기간</div>
	          <div>
	            <label class="hidden_label" for="ST_DTM">시작일을 입력해주세요</label>
	            <label class="hidden_label" for="ED_DTM">종료일을 입력해주세요</label>
	            <input type="date" name="ST_DTM" id="ST_DTM" value="${s.ST_DTM}"> ~ <input type="date" name="ED_DTM" id="ED_DTM" value="${s.ED_DTM}">
	          </div>
	        </div>
	        <div class="info_item">
	          <div class="info_title">모집인원</div>
	          <label class="hidden_label" for="APY_SIZE">모집인원을 입력해주세요</label>
	          <input type="number" placeholder="숫자만 입력해주세요" min="1" max="100" name="APY_SIZE" id="APY_SIZE" value="${s.APY_SIZE}">
	        </div>
	        <div class="info_item">
	          <div class="info_title">진행방법</div>
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
	        <div class="info_item">
	          <div class="info_title">모집 마감일</div>
	          <label class="hidden_label" for="APPLY_ED_DTM">모집 마감일을 입력해주세요</label>
	          <input type="date" name="APPLY_ED_DTM" id="APPLY_ED_DTM" value="${s.APPLY_ED_DTM}">
	        </div>
	      </div>
	      <div class="study_info_midle_line"></div>
	      <div class="insert_content_wrap">
	        <label class="hidden_label" for="title">제목을 입력해주세요</label>
	        <input type="text" class="search_input2" placeholder="제목을 입력해주세요" name="TITLE" id="title" value="${s.TITLE }">
	        <label class="hidden_label" for="summernote">내용을 입력해주세요</label>
	        <textarea id="summernote" name="CONTENT">${s.CONTENT }</textarea>    
	      </div>
	    </div>
	    <div class="footer_btn_box">
	      <button type="button" onclick="insertBoard('${nSelect}')" class="org_large_btn">저장</button>
	      <a href="javascript:goList('${nSelect}')" class="gray_large_btn">취소</a>
	    </div>
	  </form>
	</div>   
   </c:when>
   <c:otherwise>
    <div class="content">${error }</div>
   </c:otherwise>
</c:choose>
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>