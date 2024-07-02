<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>        
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>편입 뉴스</title>
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<link rel="stylesheet" href="/css/board.css" type="text/css">
<script type="text/javascript" src="/js/board.js"></script> 
</script>
</head>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp">
  <jsp:param value="on" name="on_2"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<jsp:include page="/WEB-INF/views/inc/title_box.jsp">
  <jsp:param value="banner_selected" name="menu_4"/>
</jsp:include>
<c:choose>
   <c:when test="${error eq null }">
	<div class="content">
	  <div class="content_wrap">
	    <div class="board_info">
	      <div class="board_title"><c:out value="${b.TITLE}"></c:out></div>
	      <div class="item_info_box">
	        <div class="item_info_wrap">
	          <div class="item_info"><img src="../image/icon/date.png" alt=""><c:out value="${b.REG_DTM}"></c:out></div>
	          <div class="item_info"><img src="../image/icon/view.png" alt=""><c:out value="${b.VIEW_CNT}"></c:out></div>
	        </div>
	      </div>
	    </div>
	    <!-- 게시글 내용 -->
	    <div class="board_content">
	      <c:if test="${files ne null}">
	        <div class="file_list">
	          <c:forEach var="f" items="${files}">
	             <a href='<c:url value="/filedownload?FILE_NUM=${f.FILE_NUM}"/>'>
	              <div class="file_item">
	                <img src="../image/icon/file.png" alt="파일 아이콘">
	                <div class="file_name"><c:out value="${f.ORIGIN_NAME }"></c:out></div>
	                <div class="file_size"><c:out value="${f.FANCY_SIZE }"></c:out></div>
	              </div>
	             </a>
	          </c:forEach>
	        </div>
	      </c:if>
	      <div class="content_txt">${b.CONTENT}</div>
	    </div>
	    <!-- 목록버튼 -->  
	    <div class="footer_btn_box">
	      <a href="javascript:goList('${nSelect}')" class="gray_large_btn">목록으로</a>
	    </div>
	  </div>  
	</div>
   </c:when>
   <c:otherwise>
    <div class="content"><c:out value="${error }"></c:out></div>
   </c:otherwise>
</c:choose>
<input type="hidden" id="BRD_CTG" value="3">
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>