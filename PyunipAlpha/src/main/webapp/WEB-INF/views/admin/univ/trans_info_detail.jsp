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

function delInfo(UNIV_NUM, TRANS_NUM){
	if(!confirm("정말로 삭제하시겠습니까?")){
		return;
	}
	
	$.ajax({
		url:'/admin/trans',
		type:'delete',
		data : {UNIV_NUM : UNIV_NUM , TRANS_NUM : TRANS_NUM},
		cache : false,
	    beforeSend : function(xhr) {
	        xhr.setRequestHeader(header, token);
	    }, 		
	    success:function(msg){
	    	if(msg == ""){
	    		alert("성공적으로 삭제되었습니다.");
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
    <div class="content">
    <c:choose>
       <c:when test="${error eq null }">
        <div class="content_box">
          <div class="write_box">
            <div class="write border">
              <div class="title">대학명</div>
              <div class="input_box">${s.UNIV_NAME }</div>
            </div>          
            <div class="write border">
              <div class="title">모집년도</div>
              <div class="input_box">${s.REG_YY}</div>
            </div> 
            <div class="write border">
              <div class="title">계열</div>
              <div class="input_box">${s.SERIES }</div>
            </div>          
            <div class="write border">
              <div class="title">학부명</div>
              <div class="input_box">${s.DEPT_NM}</div>
            </div> 
            <div class="write border">
              <div class="title">모집단위명</div>
              <div class="input_box">${s.MAJOR_NM}</div>
            </div> 
            <div class="write border">
              <div class="title">모집인원</div>
              <div class="input_box">${s.RECRU_SIZE}</div>
            </div> 
            <div class="write border">
              <div class="title">지원인원</div>
              <div class="input_box">${s.APY_SIZE}</div>
            </div> 
            <div class="write border">
              <div class="title">경쟁률</div>
              <div class="input_box">${s.COMPETITION}</div>
            </div> 
            <div class="write border">
              <div class="title">전형방법</div>
              <div class="input_box">${s.SLT_WAY}</div>
            </div> 
          </div>  
          <!-- 목록버튼 -->  
          <div class="btn_box">
            <a class="base_btn" href="/admin/univ/trans_info_update?UNIV_NUM=${s.UNIV_NUM }&TRANS_NUM=${s.TRANS_NUM}">수정</a>      
            <button class="base_btn" onclick="delInfo('${s.UNIV_NUM}','${s.TRANS_NUM}')">삭제</button>      
            <a class="gray_btn"  href="/admin/univ/ad_trans_info">목록</a>  
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