<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>합격 인증샷</title>
<link rel="stylesheet" href="/css/board.css" type="text/css">
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/summernote.jsp"></jsp:include>
<style>
html,body{font-family: "Pretendard" !important;}
ul{margin:0;}
a{color: var(--Gray-80); text-decoration: none!important;}
</style>
<script>
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
        $('.item_image').css("background-image", "url('" + e.target.result + "')");
      }
      
      reader.readAsDataURL(file);
        inputFile=file;
        
    } else {
        alert("파일 형식에 오류가 있습니다");
        $("#img_file").val("");
        return;
    }
}
 
function insertBoard(nSelect){
  /*로그인 여부 재확인*/
  var USER_NUM=$("#USER_NUM").val();
  if(USER_NUM =='' || USER_NUM == null){
    alert("로그인이 종료되었습니다. 다시 로그인 해주세요.");
    return;  
  }	
//   유효성 검사
  if($.trim($("#title").val())==''){
    alert("제목을 입력해주세요");
    $("#title").focus();
    return;
  }
  
  
  var formData = new FormData($("#proofForm")[0]);
  formData.append("images",inputFile);
  formData.append("filename",$("#filename").val());
  
  $.ajax({
    type: 'put'
    ,enctype : "multipart/form-data"    
    ,url: "/user/proof_shot"
    ,data : formData
    ,processData : false
    ,contentType : false    
    ,success : function(response){
    	if(response.success){
        	var BRD_NUM=$("#BRD_NUM").val();
            location.href="/board/proof_shot_detail?BRD_NUM="+BRD_NUM+"&nSelect="+nSelect;
    	}else{
    		alert(response.message);
    	}
    },error: function(html){
      alert("error:"+html);
    }
  });   
}

function goList(nSelect){
	  var BRD_NUM=$("#BRD_NUM").val(); 
	  location.href='/board/proof_shot_detail?BRD_NUM='+BRD_NUM+"&nSelect="+nSelect;
	}
</script>
</head>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp">
  <jsp:param value="on" name="on_4"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<jsp:include page="/WEB-INF/views/inc/title_box.jsp">
  <jsp:param value="banner_selected" name="menu_9"/>
</jsp:include>
<div class="content">
  <form id="proofForm" name="proofForm" method="post" onsubmit="return false;">
  <input type="hidden" id="filename" value="${s.FILE_NAME}">
  <input type="hidden" id="BRD_NUM" name="BRD_NUM" value="${s.BRD_NUM}">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
  <input type="hidden" id="USER_NUM" value="${USER_NUM}">
    <div class="content_wrap">
      <div class="insert_content_wrap">
        <label class="hidden_label" for="title">제목을 입력해주세요</label>
        <input type="text" class="search_input2" placeholder="제목을 입력해주세요" name="TITLE" id="title" value="${s.TITLE }">
        <div class="info_item">
          <div class="info_title">사진첨부</div>
		  <div class="gray_round_btn" style="width:108px;"><label for="img_file">파일첨부</label></div>	   
		  <input type="file" id="img_file"  onchange="insertImage(this)" name="img_file" style="opacity:0; width:0px; height: 0px;">
        </div>        
        <div class="item_image" style="background-image:url('/upload/image/proof/${s.FILE_NAME}')"></div>
      </div>
      <div class="study_info_midle_line"></div>
      <div class="caution">
      개인정보를 남기실 경우 개인 정보 도난의 우려가 있으므로, 기재하셨을 시 모두 삭제해주세요.
      </div>
    </div>
    <div class="footer_btn_box">
      <button type="button" onclick="insertBoard('${nSelect}')" class="org_large_btn">저장</button>
      <a href="javascript:goList('${nSelect}')" class="gray_large_btn">취소</a>
    </div>
  </form>
</div>
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>