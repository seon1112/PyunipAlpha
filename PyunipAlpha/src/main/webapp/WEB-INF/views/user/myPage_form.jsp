<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>마이페이지</title>
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
<link rel="stylesheet" href="/css/login2.css" type="text/css">
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<script>
$(function(){
	$("#signUp_btn").click(function(){
		signUp();
	});
	
	$("#delete_btn").click(function(){
		deleteMember();  
	});
});

const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

//유호성 검사
var nicknameCheck=false;
var user_nm; //인증받은 아이디


$(document).on("click","#checknickname_btn",function(){
    var USER_NM=$(this).prev().val().trim();
    if(USER_NM == ''){
    	alert("아이디를 입력해주세요.");
    	$("#USER_NM").focus();
    	return;
    }else if(USER_NM.length < 4){
    	alert("아이디는 4~10자 문자만 가능합니다.");
    	$("#USER_NM").focus();
        return;
    }    
    
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
          $(".nickname_confirm").addClass("confirm").removeClass("noconfirm");
          user_nm=$("#USER_NM").val();
          nicknameCheck=true;
        }else{
          $(".nickname_confirm").addClass("noconfirm").removeClass("confirm");
          nicknameCheck=false;
        }
      }
      ,error:function(html){
        alert("error:"+html);
      }      
    }); 
});

//회원 정보 수정
function signUp(){
  var new_NM=$("#USER_NM").val();
  var ck_NM=$("#ck_NM").val();
  
  if(new_NM != ck_NM){
    if(!nicknameCheck){
    	alert("아이디 중복 확인이 필요합니다.");
    	$("#USER_NM").focus();
    	return;
    }
  }
  
  //중복확인을 받은 아이디와 현재 작성된 아이디가 다를 때
  if(nicknameCheck){
	  if(new_NM != user_nm){
		  alert("아이디 중복 확인이 필요합니다.");
		  $("#USER_NM").focus();
		  return;
	  }
  }
  
  $.ajax({
	  type:'post',
	  url:'/user/updateUsers',
	  data:{
		  USER_NM       : $("#USER_NM").val()
	  },
    cache : false,
    beforeSend : function(xhr) {
        xhr.setRequestHeader(header, token);
    },	    
    success:function(data){
	    if(data != ""){
		   alert(data);
	    }else{
		  alert("회원정보가 변경되었습니다.")
		  location.href="/main/main_form";
	    }
    },
    error:function(html){
	    alert("error:"+html);
    }
  });
}
	
//회원탈퇴
function deleteMember(){
   if(!confirm('정말 탈퇴하시겠습니까? 탈퇴 시 작성하신 게시물이 모두 삭제됩니다.')){
       return;
   }
   
   $.ajax({
      url:'/user/users',
      type:'delete',
      cache : false,
      beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
      },
      success : function(msg){
   	   if(msg != ""){
          alert(msg);
   	   }else{
   		   alert("그동안 편입알파를 이용해주셔서 감사드립니다.");
   		    var xhr = new XMLHttpRequest();
   		    xhr.open("POST", "/login/logout", true);
   		    xhr.setRequestHeader("X-CSRF-TOKEN", "${_csrf.token}");
   		    xhr.onreadystatechange = function () {
   		        if (xhr.readyState === XMLHttpRequest.DONE) {
   		            if (xhr.status === 200) {
   		                window.location.href = "/main/main_form"; // 성공 시 리다이렉트
   		            } else {
   		                alert("로그아웃 실패: " + xhr.statusText);
   		            }
   		        }
   		    };
   		    xhr.send();    		   
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
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<div class="content" style="margin-bottom: 10px">
  <div class="info_wrap">
    <div class="info_form">
     <div class="info_title_box">회원 정보 수정</div>
     <c:choose>
      <c:when test="${error eq null }">
	      <input type="hidden" id="ck_NM" value="${dto.USER_NM}">
	      <div class="login_box">
	        <div class="login_info">
	          <span class="info_title">아이디</span>
	          <div class="sign_box">
	           <label class="hidden_label" for="USER_NM">아이디 입력</label>
	           <input class="email_input" type="text" placeholder="아이디 입력해주세요" id="USER_NM" name="USER_NM" value="${dto.USER_NM}" maxlength="10">
	           <button type="button" class="gray_btn" id="checknickname_btn">인증</button>  
	          </div>
	          <div class="nickname_confirm"></div>
	        </div>
	        <div class="login_info">
	          <span class="info_title">이메일</span>
	          <input type="text" class="email_input" readonly="readonly" value="${dto.EMAIL}">
	        </div>
	        <div class="footer_btn_box">
	          <button class="org_large_btn" id="signUp_btn">수정</button>
	          <button class="drakGray_large_btn" id="delete_btn">회원탈퇴</button>
	          <a class="gray_large_btn" href='/user/myPage/goBack'>취소</a>
	        </div>	        
	      </div>  
      </c:when>
      <c:otherwise>
        <div class="login_box">
          <div>${error}</div>
        </div>
      </c:otherwise>
     </c:choose>
    </div>
  </div>
</div>
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>