<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>[관리자페이지]합격 인증샷</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript" src="/js/ad_board.js"></script> 
<jsp:include page="/WEB-INF/jsp/summernote.jsp"></jsp:include>
<script type="text/javascript">
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
 
function insertBoard(){
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
    ,url: "/admin/proof_shot"
    ,data : formData
    ,processData : false
    ,contentType : false    
    ,success : function(msg){
      if(msg == ""){
      	var BRD_NUM=$("#BRD_NUM").val();
        location.href="/admin/board/proof_shot_detail?BRD_NUM="+BRD_NUM;
      }else{
        alert(msg);
      }
    },error: function(html){
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
	<jsp:param value="selected" name="memu_6"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
	<div class="ad_content_box">
    <div class="header">
      <span>합격 인증샷 수정</span>
    </div>
	<div class="content">
		<div class="content_box">
			<form id="proofForm" name="proofForm" enctype="multipart/form-data" onsubmit="return false;" method="post">
			  <input type="hidden" id="filename" value="${s.FILE_NAME}">
			  <input type="hidden" id="BRD_NUM" name="BRD_NUM" value="${s.BRD_NUM}">
			  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
			  <input type="hidden" name="BRD_CTG" value="5">
			  <div class="write_box">
				  <div class="write">
					<div class="title">제목</div>
					<div class="input_box">
					  <label class="hidden_label" for="title">제목 입력</label>
						<input type="text" placeholder="제목을 입력해주세요" name="TITLE" id="title" class="search_input2" value="${s.TITLE }">
					</div>
				  </div>
				  <div class="write">
			     	<div class="title">사진 첨부</div>
				    <div class="input_box2 cursor">
				    <div class="gray_round_btn" style="width:108px;"><label for="img_file">파일첨부</label></div>
		              <!-- 첨부된 파일 목록 -->
		           	  <div class="img_box"></div>
		            </div>
		          	<input type="file" id="img_file"  onchange="insertImage(this)" name="img_file" style="opacity:0; width:0px; height: 0px;">
				  </div>
				  <div class="txtarea">
				  	<div class="item_image" style="background-image:url('/upload/image/proof/${s.FILE_NAME}')"></div>
				  </div>
			  </div>
			  <div class="btn_box">
				  <button class="base_btn" onclick="insertBoard()">저장</button>
				  <a class="gray_btn"  href="/admin/board/ad_proof_shot">취소</a>				
			  </div>
			</form>
		</div>
	</div>
	</div>
<input type="hidden" id="USER_NUM" value="${USER_NUM}">	
</section>
</body>
</html>