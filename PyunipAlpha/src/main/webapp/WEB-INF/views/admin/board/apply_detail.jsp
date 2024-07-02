<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
<title>[관리자페이지]과년도 모집 요강 상세</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript" src="/js/ad_board.js"></script> 
<script>
const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");
function deleteBoard(){
  if(!confirm('정말로 삭제하시겠습니까?')){
    return;
  }
  $.ajax({
    url:'/user/board',
    type:'delete',
    data:{
      BRD_NUM:$("#BRD_NUM").val()  
      ,BRD_CTG:'2'
    },
    cache : false,
    beforeSend : function(xhr) {
        xhr.setRequestHeader(header, token);
    },
    success:function(msg){
      if(msg == ""){
        location.href='/admin/board/ad_apply';
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
	<jsp:param value="selected" name="menu_2"/>
</jsp:include>
<!-- sidebar -->
<jsp:include page="/WEB-INF/views/inc/ad_sidebar.jsp">
	<jsp:param value="selected" name="memu_1"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
	<div class="ad_content_box">
    <div class="header">
      <span>과년도 모집 요강</span>
    </div>
		<div class="content">
		<c:choose>
		   <c:when test="${error eq null }">
		    <div class="content_box">
		      <div class="write_box">
            <div class="write border">
              <div class="title">제목</div>
              <div class="input_box">${b.TITLE}</div>
            </div>		      
            <div class="write border">
              <div class="title">등록일</div>
              <div class="input_box">${b.REG_DTM}</div>
            </div>		      
            <div class="write border">
              <div class="title">조회수</div>
              <div class="input_box">${b.VIEW_CNT}</div>
            </div>		      
            <div class="write border">
              <div class="title">상단공지여부</div>
              <div class="input_box">${b.TOP_NOTICE_YN}</div>
            </div>		      
		    <!-- 게시글 내용 -->
	        <div class="board_content">
	          <c:if test="${files ne null}">
	            <div class="file_list">
	              <c:forEach var="f" items="${files}">
	                 <a href='<c:url value="/filedownload?FILE_NUM=${f.FILE_NUM}"/>'>
	                  <div class="file_item">
	                    <div class="file_icon"></div>
	                    <div class="file_name" aria-label="파일이름">${f.ORIGIN_NAME }</div>
	                    <div class="file_size" aria-label="fancy 크기">${f.FANCY_SIZE }</div>
	                  </div>
	                 </a>
	              </c:forEach>
	            </div>
	          </c:if>
	          <div class="content_txt">${b.CONTENT}</div>
	        </div>
	        </div>  
		      <!-- 목록버튼 -->  
		      <div class="btn_box">
<!-- 			<a class="base_btn" href="/admin/board/apply_update">수정</a>       -->
		        <button type="button" class="base_btn" onclick="deleteBoard()">삭제</button>      
		        <a class="gray_btn"  href="/admin/board/ad_apply">목록</a>  
		      </div>
		    </div>
		   </c:when>
		   <c:otherwise>
		    <div class="content">${error }</div>
		   </c:otherwise>
		</c:choose>
		<input type="hidden" id="ctg" value="2">
		<input type="hidden" id="BRD_NUM" value="${num}">
		</div>
	</div>
</section>
</body>
</html>