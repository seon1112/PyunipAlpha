<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>[관리자페이지]대학관리</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript" src="/js/common.js"></script> 
<script type="text/javascript">
$(function(){
	showInput();
	
	$("#SLT_WAY").change(function(){
		showInput();
	});
});

function showInput(){
	if($("#SLT_WAY option:selected").attr("name") == "special"){
		$("#SLT_WAY_SPECIAL").removeClass("hidden");
	}else{
		$("#SLT_WAY_SPECIAL").addClass("hidden");
	}	
}

function insertTrans(){
	if($("#SLT_WAY option:selected").val()== ""){
		var way = $("#SLT_WAY_SPECIAL").val().trim();
		if(way == ""){
			alert("전형방법을 입력해주세요");
			return;
		}
		$("#SLT_WAY option[name=special]").val(way);
	}
	
	if($("#RECRU_SIZE").val().trim() == ""){
		$("#RECRU_SIZE").val("0");
	}
	
	if($("#APY_SIZE").val().trim() == ""){
		$("#APY_SIZE").val("0");
	}
	
	if($("#univ option:selected").val() == ""){
		alert("대학을 선택해주세요");
		return;
	}
	var formData=new FormData($("#transForm")[0]);
  $.ajax({
	    type:'put',
	    url:'/admin/trans',
	    data:formData,
	    processData:false,
	    contentType:false,
	    success:function(msg){
        if(msg == ""){
        	  alert("성공적으로 수정되었습니다.");
        	  location.href="/admin/univ/ad_trans_info";
          }else{
            alert(msg);
          }
	    },
	    error:function(e){
	      alert("error:"+e);
	    }
	  });
}
</script>
</head>
<body>
<!-- top header -->
<jsp:include page="/WEB-INF/views/inc/ad_header.jsp">
  <jsp:param value="selected" name="menu_1"/>
</jsp:include>
<!-- sidebar -->
<jsp:include page="/WEB-INF/views/inc/ad_sidebar2.jsp">
  <jsp:param value="selected" name="memu_2"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>편입 학과 정보</span>
    </div>
    <form id="transForm" onsubmit="return false;" method="post" style="width:100%;" >
    <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" >
    <input type="hidden" name="TRANS_NUM" value="${s.TRANS_NUM}">
    <div class="content">
      <div class="content_box">
       <div class="write_box">
         <div class="write border">
           <div class="title">대학명</div>
           <div class="input_box">
		        <label class="hidden_label" for="univ">대학 선택</label>
		        <select id="univ" class="dropdown1" name="UNIV_NUM">
		          <option value="">대학명</option>
		          <c:forEach var="u" items="${univList }">
		          	<c:choose>
		          		<c:when test="${u.UNIV_NUM eq s.UNIV_NUM}">
				          <option value="${u.UNIV_NUM }" selected>${u.UNIV_NAME }</option>
		          		</c:when>
		          		<c:otherwise>
				          <option value="${u.UNIV_NUM }">${u.UNIV_NAME }</option>
		          		</c:otherwise>
		          	</c:choose>
		          </c:forEach>
		        </select>             
           </div>
         </div>
         <div class="write border">
           <div class="title">모집년도</div>
           <div class="input_box">
             <label class="hidden_label" for="REG_YY">모집년도</label>
             <select id="REG_YY" name ="REG_YY" class="dropdown1">
             <c:forEach begin="2022" end="2025" step="1" varStatus="status">
             	<c:choose>
             		<c:when test="${status.index eq s.REG_YY}">
	             	<option value="${status.index }" selected>${status.index}</option>
             		</c:when>
             		<c:otherwise>
	             	<option value="${status.index }">${status.index}</option>
             		</c:otherwise>
             	</c:choose>
             </c:forEach>
             </select> 
           </div>
         </div>
         <div class="write border">
           <div class="title">계열</div>
           <div class="input_box">
             <label class="hidden_label" for="SERIES">계열</label>
             <select id="SERIES" name ="SERIES" class="dropdown1">
             	<c:if test="${s.SERIES eq '자연'}">
                <option value="N" selected>자연</option>
                <option value="H">인문</option>
             	</c:if>
             	<c:if test="${s.SERIES eq '인문'}">
                <option value="N">자연</option>
                <option value="H" selected>인문</option>
             	</c:if>
             </select>
           </div>
         </div>
         <div class="write border">
           <div class="title">학부명</div>
           <div class="input_box">
             <label class="hidden_label" for="DEPT_NM">학부명</label>
             <input type="text" placeholder="자연과학대학" id="DEPT_NM" name="DEPT_NM" class="search_input2" value="${s.DEPT_NM }">
           </div>
         </div>
         <div class="write border">
           <div class="title">모집단위명</div>
           <div class="input_box">
             <label class="hidden_label" for="MAJOR_NM">모집단위명</label>
             <input type="text" placeholder="식품영양학과" id="MAJOR_NM" name="MAJOR_NM" class="search_input2" value="${s.MAJOR_NM}">
           </div>
         </div>
         <div class="write border">
           <div class="title">모집인원</div>
           <div class="input_box">
             <label class="hidden_label" for="RECRU_SIZE">모집인원</label>
             <input type="number" placeholder="모집인원" id="RECRU_SIZE" name="RECRU_SIZE" class="search_input2" value="${s.RECRU_SIZE}">
           </div>
         </div>   
         <div class="write border">
           <div class="title">지원인원</div>
           <div class="input_box">
             <label class="hidden_label" for="APY_SIZE">지원인원</label>
             <input type="number" placeholder="지원인원" id="APY_SIZE" name="APY_SIZE" class="search_input2" value="${s.APY_SIZE }">
           </div>
         </div>   
         <div class="write border">
           <div class="title">경쟁률</div>
           <div class="input_box">
             <label class="hidden_label" for="COMPETITION">경쟁률</label>
             <input type="text" placeholder="1:2" id="COMPETITION" name="COMPETITION" class="search_input2" value="${s.COMPETITION}">
           </div>
         </div>   
         <div class="write border">
           <div class="title">전형방법</div>
           <div class="input_box">
             <label class="hidden_label" for="SLT_WAY">전형방법</label>
             <select id="SLT_WAY" name ="SLT_WAY" class="dropdown1">
             	<c:choose>
             		<c:when test="${s.SLT_WAY eq '일반편입' }">
	                <option value="0" selected>일반편입</option>
	                <option value="1">학사편입</option>
	                <option value="" name="special">특별전형</option>             		
             		</c:when>
             		<c:when test="${s.SLT_WAY eq '학사편입'}">
	                <option value="0">일반편입</option>
	                <option value="1" selected>학사편입</option>
	                <option value="" name="special">특별전형</option>             		
             		</c:when>
             		<c:otherwise>
	                <option value="0">일반편입</option>
	                <option value="1">학사편입</option>
	                <option value="" name="special" selected>특별전형</option>             		
             		</c:otherwise>
             	</c:choose>
             </select>     
             <label class="hidden_label" for="SLT_WAY_SPECIAL">특별전형</label>
             <input type="text" placeholder="특별전형명을 입력해주세요" id="SLT_WAY_SPECIAL" class="search_input2 hidden" value="${s.SLT_WAY }">                     
           </div>
         </div>   
       </div>
       <div class="btn_box">
         <button type="button" class="base_btn" id="insert_btn" onclick="insertTrans()">저장</button>
         <a class="gray_btn"  href="/admin/univ/ad_trans_info">취소</a>        
       </div>
      </div>
    </div>
    </form>
  </div>
</section>
</body>
</html>