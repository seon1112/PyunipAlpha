<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
<title>편입 스터디</title>
<link rel="stylesheet" href="/css/board.css" type="text/css">
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<script type="text/javascript" src="/js/reply.js"></script> 
<script type="text/javascript" src="/js/heart.js"></script> 
<script>
$(function(){
  loadRpy();
});

const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

function goList(nSelect){
  location.href='/board/study_form?nSelect='+nSelect;
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
      ,BRD_CTG:'1'
    },
    cache : false,
    beforeSend : function(xhr) {
        xhr.setRequestHeader(header, token);
    },    
    success:function(msg){
    	if(msg == ""){
	      location.href='/board/study_form';
    	}else{
    		alert(msg);
    	}
    },
    error:function(html){
    	alert("error:"+html);
    }
  });
}

function updateStudy(nSelect){
	var USER_NUM=$("#USER_NUM").val();
	if(USER_NUM =='' || USER_NUM == null){
		alert("로그인이 종료되었습니다. 다시 로그인해주세요");
		return;
	}
    var BRD_NUM=$("#BRD_NUM").val(); 
    location.href='/board/study_update_form?BRD_NUM='+BRD_NUM+'&nSelect='+nSelect;  
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
<div class="content">
  <div class="content_wrap">
	  <c:choose>
	   <c:when test="${error eq null }">
	    <div class="board_info">
	      <div class="study_header">
	        <div class="study_writer"><c:out value="${s.USER_NM}"></c:out> </div>
	        <div class="header_btn">
	          <c:if test="${s.REG_USER_NUM eq USER_NUM }">
	            <a class="org_btn" href="javascript:updateStudy('${nSelect}')">수정</a>
	            <button type="button" class="darkGray_btn" onclick="deleteBoard()">삭제</button>
	          </c:if>
	          <a class="gray_btn" href="javascript:goList('${nSelect}')">목록</a>
	        </div>  
	      </div>    
	      <div class="board_title"><c:out value="${s.TITLE }"></c:out></div>
	      <div class="item_info_box">
	        <div class="item_info_wrap">
	          <div class="item_info"  aria-label="조회수">
	            <img src="../image/icon/view.png" alt="">
	             <c:out value="${s.VIEW_CNT }"></c:out>
	          </div>
	          <div class="item_info" id="like_size">
	            <c:choose>
	              <c:when test="${s.LIKE_YN eq 'Y'}">
	                <div class="like_icon full"  aria-label="꽉찬하트"></div>
	              </c:when>
	              <c:otherwise>
	                <div class="like_icon"  aria-label="빈하트"></div>
	              </c:otherwise>
	            </c:choose>
	            <div id="like_cnt"><c:out value="${s.LIKE_CNT }"></c:out></div>
	          </div>
	        </div>
	        <div class="reg_date">등록일 <span><c:out value="${s.REG_DTM }"></c:out></span></div>
	      </div>
	    </div>
	    <div class="study_info_midle_line"></div>
	    <div class="study_info_content">
	      <div class="item_sub_wrap2">
	        <div >모집 마감일 : <span><c:out value="${s.APPLY_ED_DTM }"></c:out></span></div>
	        <div >모집인원 : <span><c:out value="${s.APY_SIZE }명"></c:out></span></div>
	        <div >진행방법 : <span><c:out value="${s.PCD_WAY_ST}"></c:out></span></div>
	        <div >진행기간 : <span><c:out value="${s.ST_DTM} ~ ${s.ED_DTM}"></c:out></span></div>
	      </div>      
	    </div>
	    <div class="study_info_midle_line"></div>
	    <div class="study_content">${s.CONTENT}</div>
	   </c:when>
	   <c:otherwise>
	    <div class="board_info"><c:out value="${error }"></c:out></div>
	   </c:otherwise>
	  </c:choose>  
    <div class="study_info_midle_line"></div>
    <div class="caution">
	   개인정보를 남기실 경우 개인 정보 도난의 우려가 있으므로, 기재하셨을 시 모두 삭제해주세요.
	   스터디원 모집은 익명채팅을 통해 모집하실 것을 추천드립니다. 
    </div>    
    <!-- 댓글 -->
    <div class="rpy_list_wrap">
      <div class="rpy_count"><img src="../image/icon/comment.png" alt="">댓글<span></span></div>
      <label class="hidden_label" for="rpy_content">댓글입력</label>
      <textarea class="rpy_textarea" placeholder="댓글을 입력해주세요"  id="rpy_content"></textarea>
      <div class="rpy_search_btn">
        <button type="button" class="org_btn"  onclick="insertRpy()">댓글 등록</button>
      </div>
      <div class="rpy_list"></div>
    </div>
  </div>
</div>
<input type="hidden" id="BRD_NUM" value="${num}">
<input type="hidden" id="USER_NUM" value="${USER_NUM}">
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>