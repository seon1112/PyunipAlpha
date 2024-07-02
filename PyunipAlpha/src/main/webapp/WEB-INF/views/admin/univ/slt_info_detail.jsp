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
<script type="text/javascript">
const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

function delInfo(SLT_NUM){
	if(!confirm("정말로 삭제하시겠습니까?")){
		return;
	}
	$.ajax({
		url:'/admin/slt',
		type:'delete',
		data:{SLT_NUM : SLT_NUM},
		cache : false,
	    beforeSend : function(xhr) {
	        xhr.setRequestHeader(header, token);
	    },		
		success:function(msg){
			if(msg == ""){
				alert("삭제되었습니다.");
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
      <span>편입 전형</span>
    </div>
    <div class="content">
    <c:choose>
       <c:when test="${error eq null }">
        <div class="content_box">
          <div class="write_box">
            <div class="write border">
              <div class="title">대학명</div>
              <div class="input_box">${t.UNIV_NAME }</div>
            </div>          
            <div class="write border">
              <div class="title">공개여부</div>
              <div class="input_box">${t.OPEN_YN }</div>
            </div>          
            <div class="write border">
              <div class="title">전형방법</div>
              <div class="input_box">${t.SLT_WAY }</div>
            </div>          
            <div class="write border">
              <div class="title">지원자격(이수학점)</div>
              <div class="input_box">${t.APY_QUAL }</div>
            </div> 
            <div class="write_table">
              <div class="title">상세 전형</div>
              <!--  -->
				<table class="table1 main30">
				  <caption>전형방법, 지원자격, 진행 요소 별 반영 비율 및 배점</caption>
				  <colgroup>
				    <col style="width:40%">
				    <col style="width:10%;">
				    <col>
				  </colgroup>
				  <thead>
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
					<c:choose>
					   <c:when test="${error eq null }">
					    <c:choose>
					     <c:when test="${size eq 0}">
					      <tr>
					        <td colspan="9">자료 준비 중 입니다.</td>
					      </tr>
					     </c:when>
					     <c:otherwise>
					      <c:forEach var="s" items="${list}">
					        <c:choose>
					          <c:when test="${s.size eq 1}">
					            <tr>
					              <td>${s.SLT_STEP}</td>
					              <td>${s.ENG_PC }</td>
					              <td>${s.MATH_PC }</td>
					              <td>${s.MAJOR_PC }</td>
					              <td>${s.PREV_GRADE }</td>
					              <td>${s.INTV_PC }</td>
					              <td>${s.RECOG_ENG_PC }</td>
					              <td>${s.WHATEVER }</td>
					              <td>${s.WHATEVER_NOTE }</td>
					            </tr>        
					          </c:when>
					          <c:otherwise>
					            <tr>
					              <td>${s.SLT_STEP}</td>
					              <td>${s.ENG_PC }</td>
					              <td>${s.MATH_PC }</td>
					              <td>${s.MAJOR_PC }</td>
					              <td>${s.PREV_GRADE }</td>
					              <td>${s.INTV_PC }</td>
					              <td>${s.RECOG_ENG_PC }</td>
					              <td>${s.WHATEVER }</td>
					              <td>${s.WHATEVER_NOTE }</td>
					            </tr>
					            <tr>
					              <td>${s.SLT_STEP2}</td>
					              <td>${s.ENG_PC2 }</td>
					              <td>${s.MATH_PC2 }</td>
					              <td>${s.MAJOR_PC2 }</td>
					              <td>${s.PREV_GRADE2 }</td>
					              <td>${s.INTV_PC2 }</td>
					              <td>${s.RECOG_ENG_PC2 }</td>
					              <td>${s.WHATEVER2 }</td>
					              <td>${s.WHATEVER_NOTE2 }</td> 
					            </tr>      
					          </c:otherwise>
					        </c:choose>
					       </c:forEach>
					      </c:otherwise>    
					    </c:choose>    	   
					   </c:when>
					   <c:otherwise>
				        <tr>
				          <td colspan="9">${error }</td>
				        </tr>	    
					   </c:otherwise>
					</c:choose> 
				  </tbody>
				</table>              
              <!--  -->
            </div> 
          </div>  
          <!-- 목록버튼 -->  
          <div class="btn_box">
            <a class="base_btn" href="/admin/univ/slt_info_update?SLT_NUM=${t.SLT_NUM }">수정</a>     
            <button class="base_btn" onclick="delInfo('${t.SLT_NUM}')">삭제</button> 
            <a class="gray_btn"  href="/admin/univ/ad_slt_info">목록</a>  
          </div>
        </div>
       </c:when>
       <c:otherwise>
        <div class="content">${error }</div>
       </c:otherwise>
    </c:choose>
    </div>
  </div>
</section>
</body>
</html>