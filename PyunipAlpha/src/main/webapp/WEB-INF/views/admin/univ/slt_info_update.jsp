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
	
	$("#SLT_STEP_SELECT").change(function(){
		showInput();
	});
});

function showInput(){
	if($("#SLT_STEP_SELECT option:selected").val() != ""){
		$("#step").css("visibility","hidden");
		$("#SLT_STEP").val("일괄합산");
		$("#SLT_STEP2").val("");		
	}else{
		$("#step").css("visibility","visible");
		$("#SLT_STEP").val("1단계");
		$("#SLT_STEP2").val("2단계");
	}	
}

function insertInfo(){
	var arr=$("input[type=number]");
	Array.from(arr).forEach(function(e){
		if($(e).val().trim() == ''){
			$(e).val("0");
		}
	});
	
	if($("#univ option:selected").val() == ""){
		alert("대학을 선택해주세요");
		return;
	}
	
	if($("#SLT_WAY").val().trim() == ""){
		alert("내용을 입력해주세요");
		$("#SLT_WAY").focus();
		return;
	}
	
	if($("#APY_QUAL").val().trim() == ""){
		alert("내용을 입력해주세요");
		$("#APY_QUAL").focus();
		return;
	}
	
	if($("#SLT_DETAIL_NUM2").val().trim() == ""){
		$("#SLT_DETAIL_NUM2").val("0");
	}
	
	var formData=new FormData($("#sltForm")[0]);
	
	var step= "A";
	if($("#SLT_STEP_SELECT option:selected").val() == ""){
		step = "S"
	}	
	formData.append("step",step);
	
  $.ajax({
	    type:'put',
	    url:'/admin/slt',
	    data:formData,
	    processData:false,
	    contentType:false,
	    success:function(msg){
        if(msg == ""){
        	  alert("성공적으로 수정되었습니다.");
        	  location.href="/admin/univ/ad_slt_info";
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
  <jsp:param value="selected" name="memu_3"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>편입 전형 추가</span>
    </div>
    <form id="sltForm" onsubmit="return false;" method="post" style="width:100%;">
    <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" >
    <input type="hidden" name="SLT_NUM" value="${t.SLT_NUM }">
    <input type="hidden" name="SLT_DETAIL_NUM" value="${t.SLT_DETAIL_NUM }">
    <input type="hidden" name="SLT_DETAIL_NUM2" id="SLT_DETAIL_NUM2" value="${t.SLT_DETAIL_NUM2}">
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
	          		<c:when test="${u.UNIV_NUM eq t.UNIV_NUM}">
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
           <div class="title">공개여부</div>
           <div class="input_box">
 			 <div class="radio_box">
 			 	<c:if test="${t.OPEN_YN eq '공개'}">
 			 	<div class="txtbox_wrap"><input type="radio" class="txtboxtype" name="OPEN_YN" value="Y" checked><label>공개</label></div>
 			 	<div class="txtbox_wrap"><input type="radio" class="txtboxtype" name="OPEN_YN" value="N"><label>비공개</label></div>
 			 	</c:if>
 			 	<c:if test="${t.OPEN_YN ne '공개'}">
 			 	<div class="txtbox_wrap"><input type="radio" class="txtboxtype" name="OPEN_YN" value="Y"><label>공개</label></div>
 			 	<div class="txtbox_wrap"><input type="radio" class="txtboxtype" name="OPEN_YN" value="N" checked><label>비공개</label></div>
 			 	</c:if>
 			 </div>           
           </div>
         </div>
         <div class="write border">
           <div class="title">전형방법</div>
           <div class="input_box">
             <label class="hidden_label" for="SLT_WAY">전형방법</label>
             <textarea id="SLT_WAY" name="SLT_WAY" class="search_input2">${t.SLT_WAY }</textarea>
           </div>
         </div>   
         <div class="write border">
           <div class="title">지원자격(이수학점)</div>
           <div class="input_box">
             <label class="hidden_label" for="APY_QUAL">지원자격(이수학점)</label>
             <input type="text" id="APY_QUAL" name="APY_QUAL" class="search_input2" value="${t.APY_QUAL}">
           </div>
         </div>   
         <div class="write border">
           <div class="title">사정모형</div>
           <div class="input_box">
             <label class="hidden_label" for="SLT_STEP_SELECT">사정모형 선택</label>
             <select id="SLT_STEP_SELECT"  class="dropdown1">
               <c:if test="${t.SLT_STEP eq '일괄합산'}">
               <option value="일괄합산" selected>일괄합산</option>
               <option value="">단계별</option>
               </c:if>
               <c:if test="${t.SLT_STEP ne '일괄합산'}">
               <option value="일괄합산">일괄합산</option>
               <option value="" selected>단계별</option>
               </c:if>
             </select>     
           </div>
         </div>   
         <div class="write_table" id="step_div">
			<table class="table1 gray10">
			  <caption>진행 요소 별 반영 비율 및 배점</caption>
			  <thead>
			    <tr>
			      <th scope="col" colspan="9">진행 요소 별 반영 비율 및 배점</th>
			    </tr>
			    <tr class="nRadius">
			      <th scope="col">사정모형</th>
			      <th scope="col">영어</th>
			      <th scope="col">수학</th>
			      <th scope="col">전공/계열기초</th>
			      <th scope="col">전전대 성적</th>
			      <th scope="col">면접</th>
			      <th scope="col">공인영어</th>
			      <th scope="col">기타</th>
			      <th scope="col" style="border-right : none;">비고</th>
			    </tr>
			  </thead>	
			  <tbody>
			  	<tr>
			  	  <td><label class="hidden_label" for="SLT_STEP"></label><input type="text" id="SLT_STEP" name="SLT_STEP" class="search_input2" readonly="readonly" value="${t.SLT_STEP}"></td>
			  	  <td><label class="hidden_label" for="ENG_PC"></label><input type="number" id="ENG_PC" name="ENG_PC" class="search_input2" value="${t.ENG_PC}"></td>
			  	  <td><label class="hidden_label" for="MATH_PC"></label><input type="number" id="MATH_PC" name="MATH_PC" class="search_input2" value="${t.MATH_PC}"></td>
			  	  <td><label class="hidden_label" for="MAJOR_PC"></label><input type="number" id="MAJOR_PC" name="MAJOR_PC" class="search_input2" value="${t.MAJOR_PC}"></td>
			  	  <td><label class="hidden_label" for="PREV_GRADE"></label><input type="number" id="PREV_GRADE" name="PREV_GRADE" class="search_input2" value="${t.PREV_GRADE}"></td>
			  	  <td><label class="hidden_label" for="INTV_PC"></label><input type="number" id="INTV_PC" name="INTV_PC" class="search_input2" value="${t.INTV_PC}"></td>
			  	  <td><label class="hidden_label" for="RECOG_ENG_PC"></label><input type="number" id="RECOG_ENG_PC" name="RECOG_ENG_PC" class="search_input2" value="${t.RECOG_ENG_PC}"></td>
			  	  <td><label class="hidden_label" for="WHATEVER"></label><input type="number" id="WHATEVER" name="WHATEVER" class="search_input2" value="${t.WHATEVER}"></td>
			  	  <td><label class="hidden_label" for="WHATEVER_NOTE"></label><input type="text" id="WHATEVER_NOTE" name="WHATEVER_NOTE" class="search_input2" value="${t.WHATEVER_NOTE}"></td>
			  	</tr>
			  	<tr id="step">
			  	  <td><label class="hidden_label" for="SLT_STEP2"></label><input type="text" id="SLT_STEP2" name="SLT_STEP2" class="search_input2" readonly="readonly" value="${t.SLT_STEP2}"></td>
			  	  <td><label class="hidden_label" for="ENG_PC2"></label><input type="number" id="ENG_PC2" name="ENG_PC2" class="search_input2" value="${t.ENG_PC2}"></td>
			  	  <td><label class="hidden_label" for="MATH_PC2"></label><input type="number" id="MATH_PC2" name="MATH_PC2" class="search_input2" value="${t.MATH_PC2}"></td>
			  	  <td><label class="hidden_label" for="MAJOR_PC2"></label><input type="number" id="MAJOR_PC2" name="MAJOR_PC2" class="search_input2" value="${t.MAJOR_PC2}"></td>
			  	  <td><label class="hidden_label" for="PREV_GRADE2"></label><input type="number" id="PREV_GRADE2" name="PREV_GRADE2" class="search_input2" value="${t.PREV_GRADE2}"></td>
			  	  <td><label class="hidden_label" for="INTV_PC2"></label><input type="number" id="INTV_PC2" name="INTV_PC2" class="search_input2" value="${t.INTV_PC2}"></td>
			  	  <td><label class="hidden_label" for="RECOG_ENG_PC2"></label><input type="number" id="RECOG_ENG_PC2" name="RECOG_ENG_PC2" class="search_input2" value="${t.RECOG_ENG_PC2}"></td>
			  	  <td><label class="hidden_label" for="WHATEVER2"></label><input type="number" id="WHATEVER2" name="WHATEVER2" class="search_input2" value="${t.WHATEVER2}"></td>
			  	  <td><label class="hidden_label" for="WHATEVER_NOTE2"></label><input type="text" id="WHATEVER_NOTE2" name="WHATEVER_NOTE2" class="search_input2" value="${t.WHATEVER_NOTE2}"></td>
			  	</tr>				  	
			  </tbody>		
			</table>
         </div>   
       </div>
       <div class="btn_box">
         <button type="button" class="base_btn" id="insert_btn" onclick="insertInfo()">저장</button>
         <a class="gray_btn"  href="/admin/univ/ad_slt_info">취소</a>        
       </div>
      </div>
    </div>
    </form>
  </div>
</section>
</body>
</html>