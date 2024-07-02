<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
<title>편입이야기 상세</title>
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<link rel="stylesheet" href="/css/board.css" type="text/css">
<script type="text/javascript" src="/js/reply.js"></script> 
<script type="text/javascript" src="/js/heart.js"></script> 
<script>
$(function(){
	loadRpy();
});
const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

function updateTalk(nSelect){
	var USER_NUM=$("#USER_NUM").val();
	if(USER_NUM =='' || USER_NUM == null){
		alert("로그인이 종료되었습니다. 다시 로그인해주세요");
		return;
	}
	
	var BRD_NUM=$("#BRD_NUM").val(); 
	location.href='/board/talk_update_form?BRD_NUM='+BRD_NUM+'&nSelect='+nSelect;
}

function deleteBoard(){
	if(!confirm('정말로 삭제하시겠습니까?')){
		return;
	}
	$.ajax({
		url:'/user/board',
		type:'delete',
		data:{
			BRD_NUM:$("#BRD_NUM").val()  
			,BRD_CTG:$("#BRD_CTG").val()
		},
		cache : false,
	    beforeSend : function(xhr) {
	        xhr.setRequestHeader(header, token);
	    },
		success:function(msg){
			if(msg == ""){
				location.href='/board/talk_form';
			}else{
				alert(msg);				
			}
		},
		error:function(html){
			alert("error:"+html);
		}
	});
}

function goList(nSelect){
	location.href='/board/talk_form?nSelect='+nSelect;
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
  <jsp:param value="banner_selected" name="menu_3"/>
</jsp:include>
<div class="content">
  <div class="content_wrap">
	<c:choose>
	   <c:when test="${error eq null }">
	    <div class="board_info">
	      <div class="talk_header">
	        <c:choose>
	          <c:when test="${t.BRD_DV eq 'F'}">
	             <div class="badge1">자유주제</div>
	          </c:when>
	          <c:otherwise>
	             <div class="badge2">질문하기</div>
	          </c:otherwise>
	        </c:choose>
	        <div class="talk_writer">${t.USER_NM }</div>
	        <div class="header_btn">
	          <c:if test="${t.REG_USER_NUM eq USER_NUM }">
	            <a class="org_btn" href="javascript:updateTalk('${nSelect}')">수정</a>
	            <button type="button" class="darkGray_btn" onclick="deleteBoard()">삭제</button>
	          </c:if>
	          <a class="gray_btn" href="javascript:goList('${nSelect}')">목록</a>
	        </div>
	      </div>
	      <div class="board_title"><c:out value="${t.TITLE}"></c:out></div>
	      <div class="item_info_box">
	        <div class="item_info_wrap">
	          <div class="item_info" aria-label="조회수">
	            <img src="../image/icon/view.png" alt="">
	            <c:out value="${t.VIEW_CNT }"></c:out>
	          </div>
	          <div class="item_info" id="like_size">
	            <c:choose>
	              <c:when test="${t.LIKE_YN eq 'Y'}">
	                <button type="button" class="like_icon full" aria-label="꽉찬 하트"></button>
	              </c:when>
	              <c:otherwise>
	                <button type="button" class="like_icon" aria-label="빈 하트"></button>
	              </c:otherwise>
	            </c:choose>
	            <div id="like_cnt"><c:out value="${t.LIKE_CNT }"></c:out></div>
	          </div>
	        </div>
	        <div class="reg_date">등록일 <span><c:out value="${t.REG_DTM}"></c:out></span></div>
	      </div>
	    </div>
	    <div class="study_info_midle_line"></div>
	    <div class="study_content">${t.CONTENT}</div>		   
	   </c:when>
	   <c:otherwise>
	    <div class="board_info"><div><c:out value="${error }"></c:out></div></div>
	   </c:otherwise>
	</c:choose>  
    <div class="study_info_midle_line"></div>
    <!-- 댓글 -->
    <div class="rpy_list_wrap">
      <div class="rpy_count"><img src="../image/icon/comment.png" alt="">댓글<span></span></div>
      <label class="hidden_label" for="rpy_content">댓글을 입력해주세요</label>
      <textarea class="rpy_textarea" placeholder="댓글을 입력해주세요" id="rpy_content"></textarea>
      <div class="rpy_search_btn">
        <button type="button" class="org_btn" onclick="insertRpy()">댓글 등록</button>
      </div>
      <div class="rpy_list"></div>
    </div>
  </div>
</div>
<input type="hidden" id="BRD_NUM" value="${num}">
<input type="hidden" id="USER_NUM" value="${USER_NUM}">
<input type="hidden" id="BRD_CTG" value="0">
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>