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
<script type="text/javascript" src="/js/common.js"></script> 
<script type="text/javascript">
const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

var nicknameCheck=false;
function checkNM(obj){
    var USER_NM=$(obj).prev().val().trim();
    
    $.ajax({
      url:'/user/checkNickname',
      data:{USER_NM:USER_NM},
      type:'post',
      cache : false,
      beforeSend : function(xhr) {
          xhr.setRequestHeader(header, token);
      },      
      success:function(data){
        if(data=='true'){
          alert("사용 가능한 아이디입니다.");
          nicknameCheck=true;
        }else{
          alert("사용 불가능한 아이디입니다.");
          nicknameCheck=false;
        }
      }
      ,error:function(html){
        alert("error:"+html);
      }      
    }); 	
}

function insertInfo(){
  var new_NM=$("#USER_NM").val();
  var ck_NM=$("#ck_NM").val();
  if(new_NM != ck_NM){
    if(!nicknameCheck){
    	alert("아이디 중복 확인이 필요합니다.");
    	return;
    }
  }	
	
  
  $.ajax({
    type:'put',
    url:'/admin/user',
    data:{
    	USER_NM : $("#USER_NM").val()
    	,USER_NUM : $("#USER_NUM").val()
    	,ROLE : $("#ROLE").val()
    },
    cache : false,
    beforeSend : function(xhr) {
        xhr.setRequestHeader(header, token);
    }, 
    success:function(msg){
      if(msg == ""){
	   	  alert("성공적으로 수정되었습니다.");
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
    <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" >
    <input type="hidden" id="ck_NM" value="${t.USER_NM}">
    <input type="hidden" id="USER_NUM" value="${t.USER_NUM }">
    <div class="content">
      <div class="content_box">
       <div class="write_box">
         <div class="write border">
           <div class="title">아이디</div>
           <div class="input_box">
             <label class="hidden_label" for="USER_NM">아이디</label>
             <input type="text" id="USER_NM" name="USER_NM" class="search_input2" value="${t.USER_NM}">
             <button type="button" class="gray_btn"  onclick="checkNM($(this))" style="height: 100%;">인증</button>
           </div>
         </div>   
         <div class="write border">
           <div class="title">권한</div>
           <div class="input_box">
             <label class="hidden_label" for="SLT_STEP_SELECT">권한</label>
             <select id="ROLE" name="ROLE" class="dropdown1">
               <c:if test="${t.ROLE eq '사용자'}">
               <option value="ROLE_USER" selected>사용자</option>
               <option value="ROLE_ADMIN">관리자</option>
               </c:if>
               <c:if test="${t.ROLE ne '사용자'}">
               <option value="ROLE_USER">사용자</option>
               <option value="ROLE_ADMIN" selected>관리자</option>
               </c:if>
             </select>     
           </div>
         </div>   
       </div>
       <div class="btn_box">
         <button type="button" class="base_btn" id="insert_btn" onclick="insertInfo()">저장</button>
         <a class="gray_btn"  href="/admin/user/ad_user">취소</a>        
       </div>
      </div>
    </div>
  </div>
</section>
</body>
</html>