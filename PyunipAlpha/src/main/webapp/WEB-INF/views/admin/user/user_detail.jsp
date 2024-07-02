<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
<title>[관리자페이지]회원관리</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript">
const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

function delUser(USER_NUM){
	if(!confirm("회원정보를 삭제하시겠습니까? 삭제 시 회원이 작성한 게시물 모두가 삭제됩니다.")){
		return;
	}
	$.ajax({
		url:'/admin/user',
		type:'delete',
		data:{USER_NUM : USER_NUM},
		cache : false,
	    beforeSend : function(xhr) {
	        xhr.setRequestHeader(header, token);
	    }, 			
		success:function(msg){
			if(msg == ""){
				alert("삭제되었습니다.");
				location.href="/admin/user/ad_user";
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
  <jsp:param value="selected" name="menu_3"/>
</jsp:include>
<!-- sidebar -->
<jsp:include page="/WEB-INF/views/inc/ad_sidebar3.jsp">
  <jsp:param value="selected" name="memu_1"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>회원 상세</span>
    </div>
    <div class="content">
    <c:choose>
       <c:when test="${error eq null }">
        <div class="content_box">
          <div class="write_box">
            <div class="write border">
              <div class="title">아이디</div>
              <div class="input_box">${s.USER_NM }</div>
            </div>          
            <div class="write border">
              <div class="title">이메일</div>
              <div class="input_box">${s.EMAIL }</div>
            </div>          
            <div class="write border">
              <div class="title">가입일</div>
              <div class="input_box">${s.REG_DTM }</div>
            </div>          
            <div class="write border">
              <div class="title">최근 접속일</div>
              <div class="input_box">${s.RCT_REG_DTM}</div>
            </div> 
            <div class="write border">
              <div class="title">권한</div>
              <div class="input_box">${s.ROLE}</div>
            </div> 
            <div class="write border">
              <div class="title">소셜</div>
              <div class="input_box">${s.SOCIAL_NM}</div>
            </div> 
          </div>  
          <!-- 목록버튼 -->  
          <div class="btn_box">
            <a class="base_btn" href="/admin/user/user_update?USER_NUM=${s.USER_NUM }">수정</a> 
            <button type="button" class="base_btn" onclick="delUser('${s.USER_NUM}')">삭제</button>     
            <a class="gray_btn"  href="/admin/user/ad_user">목록</a>  
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