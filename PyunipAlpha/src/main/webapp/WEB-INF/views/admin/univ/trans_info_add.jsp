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
	$("#SLT_WAY").change(function(){
		if($("#SLT_WAY option:selected").attr("name") == "special"){
			$("#SLT_WAY_SPECIAL").removeClass("hidden");
		}else{
			$("#SLT_WAY_SPECIAL").addClass("hidden");
		}
	});
});

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
	    type:'post',
	    url:'/admin/trans',
	    data:formData,
	    processData:false,
	    contentType:false,
	    success:function(msg){
        if(msg == ""){
        	  alert("성공적으로 등록되었습니다.");
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
      <span>편입 학과 정보 추가</span>
    </div>
    <form id="transForm" onsubmit="return false;" method="post" style="width:100%;" >
    <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" >
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
		          <option value="${u.UNIV_NUM }">${u.UNIV_NAME }</option>
		          </c:forEach>
		        </select>             
           </div>
         </div>
         <div class="write border">
           <div class="title">모집년도</div>
           <div class="input_box">
             <label class="hidden_label" for="REG_YY">모집년도</label>
             <select id="REG_YY" name ="REG_YY" class="dropdown1">
               <option value="2025">2025</option>
               <option value="2024">2024</option>
               <option value="2023">2023</option>
               <option value="2022">2022</option>
             </select> 
           </div>
         </div>
         <div class="write border">
           <div class="title">계열</div>
           <div class="input_box">
             <label class="hidden_label" for="SERIES">계열</label>
             <select id="SERIES" name ="SERIES" class="dropdown1">
               <option value="N">자연</option>
               <option value="H">인문</option>
             </select>
           </div>
         </div>
         <div class="write border">
           <div class="title">학부명</div>
           <div class="input_box">
             <label class="hidden_label" for="DEPT_NM">학부명</label>
             <input type="text" placeholder="자연과학대학" id="DEPT_NM" name="DEPT_NM" class="search_input2">
           </div>
         </div>
         <div class="write border">
           <div class="title">모집단위명</div>
           <div class="input_box">
             <label class="hidden_label" for="MAJOR_NM">모집단위명</label>
             <input type="text" placeholder="식품영양학과" id="MAJOR_NM" name="MAJOR_NM" class="search_input2">
           </div>
         </div>
         <div class="write border">
           <div class="title">모집인원</div>
           <div class="input_box">
             <label class="hidden_label" for="RECRU_SIZE">모집인원</label>
             <input type="number" placeholder="모집인원" id="RECRU_SIZE" name="RECRU_SIZE" class="search_input2">
           </div>
         </div>   
         <div class="write border">
           <div class="title">지원인원</div>
           <div class="input_box">
             <label class="hidden_label" for="APY_SIZE">지원인원</label>
             <input type="number" placeholder="지원인원" id="APY_SIZE" name="APY_SIZE" class="search_input2">
           </div>
         </div>   
         <div class="write border">
           <div class="title">경쟁률</div>
           <div class="input_box">
             <label class="hidden_label" for="COMPETITION">경쟁률</label>
             <input type="text" placeholder="1:2" id="COMPETITION" name="COMPETITION" class="search_input2">
           </div>
         </div>   
         <div class="write border">
           <div class="title">전형방법</div>
           <div class="input_box">
             <label class="hidden_label" for="SLT_WAY">전형방법</label>
             <select id="SLT_WAY" name ="SLT_WAY" class="dropdown1">
               <option value="0">일반평입</option>
               <option value="1">학사편입</option>
               <option value="" name="special">특별전형</option>
             </select>     
             <label class="hidden_label" for="SLT_WAY_SPECIAL">특별전형</label>
             <input type="text" placeholder="특별전형명을 입력해주세요" id="SLT_WAY_SPECIAL" class="search_input2 hidden">                     
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