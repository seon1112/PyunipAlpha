<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
<title>[관리자페이지]대학관리</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript" src="/js/common.js"></script> 
<script type="text/javascript">
const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

var inputFile='';
var maxSize=5*1024*1024;

function insertImage(input) {
    if (!input.files || input.files.length === 0) {
        alert("파일이 선택되지 않았습니다");
        return;
    }
    if ($("#img_file").val() == '') return;
    
    var file = input.files[0]; 
    if(file.size > maxSize){
    	alert("첨부파일 사이즈는 5MB 이내로 등록 가능합니다.");
    	return;
    }
    
    if (
       file.name.toLowerCase().endsWith(".jpeg") ||
       file.name.toLowerCase().endsWith(".jpg") ||
       file.name.toLowerCase().endsWith(".png")
    ) {
      var reader = new FileReader();
      reader.onload = function (e) {
        $('.logo_image').css("background-image", "url('" + e.target.result + "')");
      }
      
      reader.readAsDataURL(file);
      inputFile=file;
        
    } else {
        alert("파일 형식에 오류가 있습니다. jpeg/jpg/png만 가능합니다.");
        $("#img_file").val("");
        return;
    }
}
 
function insertUniv(){
	  var SUC_APY_DTM="";
	  $("input[name=suc]").each(function(){
		  if($(this).val().trim() != ''){
			  SUC_APY_DTM=SUC_APY_DTM+$(this).val().trim().replaceAll("-","")+"^";
		  }
	  });
	  var DOC_SUB_DTM="";
	  $("input[name=doc]").each(function(){
		  if($(this).val().trim() != ''){
			  DOC_SUB_DTM=DOC_SUB_DTM+$(this).val().trim().replaceAll("-","")+"^";
		  }
	  });
	  
	  var INTV_DTM="";
	  $("input[name=intv]").each(function(){
		  if($(this).val().trim() != ''){
			  INTV_DTM=INTV_DTM+$(this).val().trim().replaceAll("-","")+"^";
		  }
	  });
	  
	  var APY_ST_DT="";
	  if($("#APY_ST_DTM").val().trim() != ''){
		  APY_ST_DT = $("#APY_ST_DTM").val().replaceAll("-","")+$("#APY_ST_DTM_HH option:selected").val()+$("#APY_ST_DTM_MM option:selected").val();
	  }
	  
	  var APY_ED_DTM="";
	  if($("#APY_ED_DTM").val().trim() != ''){
		  APY_ED_DTM = $("#APY_ED_DTM").val().replaceAll("-","")+$("#APY_ED_DTM_HH option:selected").val()+$("#APY_ED_DTM_MM option:selected").val();
	  }
	  
	  var formData=new FormData();
	  formData.append("UNIV_NUM",$("#UNIV_NUM").val());
	  formData.append("UNIV_NAME",$("#UNIV_NAME").val());
	  formData.append("ADDR",$("#ADDR").val());
	  formData.append("URL",$("#URL").val());
	  formData.append("PHONE",$("#PHONE").val());
	  formData.append("REG_YY",$("#REG_YY").val());
	  formData.append("APY_ST_DTM",APY_ST_DT);
	  formData.append("APY_ED_DTM",APY_ED_DTM);
	  formData.append("APY_NOTE",$("#APY_NOTE").val());
	  formData.append("SLT_DATE",$("#SLT_DATE").val().replaceAll("-",""));
	  formData.append("SLT_NOTE",$("#SLT_NOTE").val());
	  formData.append("SUC_APY_NOTE",$("#SUC_APY_NOTE").val());
	  formData.append("DOC_SUB_NOTE",$("#DOC_SUB_NOTE").val());
	  formData.append("INTV_NOTE",$("#INTV_NOTE").val());
	  formData.append("filename",$("#filename").val());
	  formData.append("SUC_APY_DTM",SUC_APY_DTM);
	  formData.append("DOC_SUB_DTM",DOC_SUB_DTM);
	  formData.append("INTV_DTM",INTV_DTM);
	  formData.append("images",inputFile);
	  
	  $.ajax({
		  type:'put',
		  url : '/admin/univ',
	      async: false,     
	      processData : false,
	      contentType : false,
	      data:formData,
	    cache : false,
	    beforeSend : function(xhr) {
	        xhr.setRequestHeader(header, token);
	    },   	    
	    success:function(msg){
	        if(msg == ""){
	        	var num =$("#UNIV_NUM").val();
	          location.href='/admin/univ/univ_info_detail?UNIV_NUM='+num;
	        }else{
	          alert(msg);
	        }
	      },
      error:function(html){
        alert("error:"+html);
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
  <jsp:param value="selected" name="memu_1"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>대학 정보 수정</span>
    </div>
    <div class="content">
      <div class="content_box">
       <input type="hidden" id="filename" value="${s.LOGO}">
       <div class="write_box">
         <div class="write border">
           <div class="title">대학 일련번호</div>
           <div class="input_box">
             <input type="text" id="UNIV_NUM" class="search_input2" value="${s.UNIV_NUM}" readonly="readonly">
           </div>
         </div>
         <div class="write border">
           <div class="title">대학명</div>
           <div class="input_box">
             <label class="hidden_label" for="UNIV_NAME">대학명</label>
             <input type="text" placeholder="대학명" id="UNIV_NAME" class="search_input2" value="${s.UNIV_NAME }">
           </div>
         </div>
         <div class="write border">
           <div class="title">주소</div>
           <div class="input_box">
             <label class="hidden_label" for="ADDR">주소</label>
             <input type="text" placeholder="주소" id="ADDR" class="search_input2" value="${s.ADDR }">
           </div>
         </div>
         <div class="write border">
           <div class="title">홈페이지 URL</div>
           <div class="input_box">
             <label class="hidden_label" for="URL">홈페이지 URL</label>
             <input type="text" placeholder="홈페이지 URL" id="URL" class="search_input2" value="${s.URL }">
           </div>
         </div>
         <div class="write border">
           <div class="title">전화번호</div>
           <div class="input_box">
             <label class="hidden_label" for="PHONE">전화번호</label>
             <input type="text" placeholder="02-0000-0000" id="PHONE" class="search_input2" value="${s.PHONE }" >
           </div>
         </div>
         <div class="write border">
           <div class="title">모집년도</div>
           <div class="input_box">
             <label class="hidden_label" for="REG_YY">모집년도</label>
             <input type="text" placeholder="모집년도" id="REG_YY" class="search_input2" value="${s.REG_YY }" >
           </div>
         </div>          
         <div class="write border">
           <div class="title">원서접수</div>
           <div class="input_box">
             <div class="date_wrap">
	             <label class="hidden_label" for="APY_ST_DTM">원서접수 시작일</label>
	             <input type="date"  id="APY_ST_DTM" class="search_input2" value="${s.APY_ST_DTM }">
	             <label for="APY_ST_DTM_HH">
	               <span class="hidden_label">원서접수 시작일 시간을 선택</span>
	               <select id="APY_ST_DTM_HH" class="dropdown1">
	                 <option value="00">시</option>
                   <c:forEach begin="0" end="23" step="1" varStatus="status">
                   <c:if test="${status.index < 10}">
                   <c:choose>
                    <c:when  test="${'0' + status.index eq APY_ST_DTM_HH}">
                     <option value="0${status.index }" selected>0${status.index }시</option>
                    </c:when>
                    <c:otherwise>
                     <option value="0${status.index }">0${status.index }시</option>
                    </c:otherwise>
                   </c:choose> 
                   </c:if>
                   <c:if test="${status.index > 9}">
                   <c:choose>
                    <c:when  test="${status.index eq APY_ST_DTM_HH}">
                     <option value="${status.index }" selected>${status.index }시</option>
                    </c:when>
                    <c:otherwise>
                     <option value="${status.index }">${status.index }시</option>
                    </c:otherwise>
                   </c:choose>                    
                   </c:if>
                   </c:forEach>	                 
	               </select>
	             </label>
	             <label for="APY_ST_DTM_MM">
	               <span class="hidden_label">원서접수 시작일 분을 선택</span>
	               <select id="APY_ST_DTM_MM" class="dropdown1">
	                 <option value="00">분</option>
                   <c:forEach begin="0" end="59" step="1" varStatus="status">
                   <c:if test="${status.index < 10}">
                   <c:choose>
                    <c:when  test="${'0' + status.index eq APY_ST_DTM_MM}">
                     <option value="0${status.index }" selected>0${status.index }분</option>
                    </c:when>
                    <c:otherwise>
                     <option value="0${status.index }">0${status.index }분</option>
                    </c:otherwise>
                   </c:choose> 
                   </c:if>
                   <c:if test="${status.index > 9}">
                   <c:choose>
                    <c:when  test="${status.index eq APY_ST_DTM_MM}">
                     <option value="${status.index }" selected>${status.index }분</option>
                    </c:when>
                    <c:otherwise>
                     <option value="${status.index }">${status.index }분</option>
                    </c:otherwise>
                   </c:choose>                    
                   </c:if>
                   </c:forEach>
	               </select>
	             </label>
             </div>
             ~
             <div class="date_wrap">
	             <label class="hidden_label" for="APY_ED_DTM">원서접수 종료일</label>
	             <input type="date" id="APY_ED_DTM" class="search_input2" value="${s.APY_ED_DTM }">
               <label for="APY_ED_DTM_HH">
                 <span class="hidden_label">원서접수 종료일 시간을 선택</span>
                 <select id="APY_ED_DTM_HH" class="dropdown1">
                   <option value="00">시</option>
                   <c:forEach begin="0" end="23" step="1" varStatus="status">
                   <c:if test="${status.index < 10}">
                   <c:choose>
                    <c:when  test="${'0' + status.index eq APY_Ed_DTM_HH}">
                     <option value="0${status.index }" selected>0${status.index }시</option>
                    </c:when>
                    <c:otherwise>
                     <option value="0${status.index }">0${status.index }시</option>
                    </c:otherwise>
                   </c:choose> 
                   </c:if>
                   <c:if test="${status.index > 9}">
                   <c:choose>
                    <c:when  test="${status.index eq APY_ED_DTM_HH}">
                     <option value="${status.index }" selected>${status.index }시</option>
                    </c:when>
                    <c:otherwise>
                     <option value="${status.index }">${status.index }시</option>
                    </c:otherwise>
                   </c:choose>                    
                   </c:if>
                   </c:forEach>                  
                 </select>
               </label>
               <label for="APY_ED_DTM_MM">
                 <span class="hidden_label">원서접수 종료일 분을 선택</span>
                 <select id="APY_ED_DTM_MM" class="dropdown1">
                   <option value="00">분</option>
                   <c:forEach begin="0" end="59" step="1" varStatus="status">
                   <c:if test="${status.index < 10}">
                   <c:choose>
                    <c:when  test="${'0' + status.index eq APY_ED_DTM_MM}">
                     <option value="0${status.index }" selected>0${status.index }분</option>
                    </c:when>
                    <c:otherwise>
                     <option value="0${status.index }">0${status.index }분</option>
                    </c:otherwise>
                   </c:choose> 
                   </c:if>
                   <c:if test="${status.index > 9}">
                   <c:choose>
                    <c:when  test="${status.index eq APY_ED_DTM_MM}">
                     <option value="${status.index }" selected>${status.index }분</option>
                    </c:when>
                    <c:otherwise>
                     <option value="${status.index }">${status.index }분</option>
                    </c:otherwise>
                   </c:choose>                    
                   </c:if>
                   </c:forEach>
                 </select>
               </label>	             
             </div>
           </div>
         </div>
         <div class="write border">
           <div class="title">원서접수 비고</div>
           <div class="input_box">
             <label class="hidden_label" for="APY_NOTE">원서접수 비고</label>
             <input type="text" placeholder="원서접수 비고" id="APY_NOTE" class="search_input2" value="${s.APY_NOTE }">
           </div>
         </div>            
         <div class="write border">
           <div class="title">전형일</div>
           <div class="input_box">
             <label class="hidden_label" for="SLT_DATE">전형일</label>
             <input type="date" id="SLT_DATE" class="search_input2" value="${s.SLT_DATE }">
           </div>
         </div>            
         <div class="write border">
           <div class="title">전형일 비고</div>
           <div class="input_box">
             <label class="hidden_label" for="SLT_NOTE">전형일 비고</label>
             <input type="text" placeholder="전형일 비고" id="SLT_NOTE" class="search_input2" value="${s.SLT_NOTE }">
           </div>
         </div>     
         <div class="write border">
           <div class="title">합격자 발표</div>
           <div class="input_box">
             <label class="" for="suc1">1차 합격자 발표</label>
             <input type="date" id="suc1" class="search_input2" name ="suc" value="${suc[0]}">
             <label class="" for="suc2">2차 합격자 발표</label>
             <input type="date" id="suc2" class="search_input2" name ="suc" value="${suc[1]}">
           </div>
         </div>     
         <div class="write border">
           <div class="title">합격자발표 비고</div>
           <div class="input_box">
             <label class="hidden_label" for="SUC_APY_NOTE">합격자발표 비고</label>
             <input type="text" placeholder="합격자발표 비고" id="SUC_APY_NOTE" class="search_input2" value="${s.SUC_APY_NOTE }">
           </div>
         </div>            
         <div class="write border">
           <div class="title">서류제출</div>
           <div class="input_box">
             <label class="hidden_label" for="doc1">서류제출 시작일</label>
             <label class="hidden_label" for="doc2">서류제출 종료일</label>
             <input type="date" id="doc1" name="doc" class="search_input2" value="${doc[0]}">
             ~
             <input type="date" id="doc2"  name="doc" class="search_input2" value="${doc[1]}">
           </div>
         </div>           
         <div class="write border">
           <div class="title">서류제출 비고</div>
           <div class="input_box">
             <label class="hidden_label" for="DOC_SUB_NOTE">서류제출 비고</label>
             <input type="text" placeholder="서류제출 비고" id="DOC_SUB_NOTE" class="search_input2" value="${s.DOC_SUB_NOTE }">
           </div>
         </div>           
         <div class="write border">
           <div class="title">면접일정</div>
           <div class="input_box">
             <label class="" for="intv1">1차 면접</label>
             <input type="date"  id="intv1" name="intv" class="search_input2" value="${intv[0]}">
             <label class="" for="intv2">2차 면접</label>
             <input type="date" id="intv2" name="intv" class="search_input2" value="${intv[1]}">
           </div>
         </div>    
         <div class="write border">
           <div class="title">면접일정 비고</div>
           <div class="input_box">
             <label class="hidden_label" for="INTV_NOTE">면접일정 비고</label>
             <input type="text" placeholder="면접일정 비고" id="INTV_NOTE" class="search_input2" value="${s.INTV_NOTE }">
           </div>
         </div>                          
           
         <div class="write">
           <div class="title">로고</div>
           <div class="input_box cursor">
             <c:choose>
               <c:when test="${s.LOGO eq null }">
                 <div class="logo_image"></div>
               </c:when>
               <c:otherwise>
                 <div class="logo_image" style="background-image:url('/upload/image/logo/${s.LOGO}')"></div>
               </c:otherwise>
             </c:choose>           
             <div class="gray_round_btn" style="width:108px;"><label for="img_file">파일첨부</label></div>
           </div>
           <input type="file" id="img_file"  onchange="insertImage(this)" name="img_file" style="opacity:0; width:0px; height: 0px;">
         </div> 
       </div>
       <div class="btn_box">
         <button type="button" class="base_btn" id="insert_btn" onclick="insertUniv();">저장</button>
         <a class="gray_btn"  href="/admin/univ/univ_info_detail?UNIV_NUM=${s.UNIV_NUM}">취소</a>        
       </div>
      </div>
    </div>
  </div>
</section>
</body>
</html>