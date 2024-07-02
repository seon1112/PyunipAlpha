<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>[관리자페이지]편입뉴스</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript" src="/js/ad_board.js"></script> 
<jsp:include page="/WEB-INF/jsp/summernote.jsp"></jsp:include>
<script type="text/javascript">
$(function(){
	$("#insert_p_btn").click(function(){
		insertBoard();
	});
	$("#cancel_p_btn").click(function(){
		location.reload();
	});
	loadSummernote();
});

</script>
</head>
<body>
<!-- top header -->
<jsp:include page="/WEB-INF/views/inc/ad_header.jsp">
	<jsp:param value="selected" name="menu_2"/>
</jsp:include>
<!-- sidebar -->
<jsp:include page="/WEB-INF/views/inc/ad_sidebar.jsp">
	<jsp:param value="selected" name="memu_2"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
	<div class="ad_content_box">
    <div class="header">
      <span>편입뉴스 추가</span>
    </div>
		<div class="content">
			<div class="content_box">
				<form id="boardForm" name="boardForm" enctype="multipart/form-data" onsubmit="return false;" method="post">
				  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
				  <input type="hidden" id="BRD_CTG" name="BRD_CTG" value="3">
				  <input type="hidden" name="filenames" id="filenames">
					<div class="write_box">
					  <div class="write border">
					    <div class="title">상단 공지</div>
					    <div class="input_box">
					      <div class="txtbox_wrap">
						      <label class="hidden_label" for="TOP_NOTICE_YN">상단 공지여부 선택</label>
						      <input type="checkbox" id="TOP_NOTICE_YN" name="TOP_NOTICE_YN" style="margin:0px;">상단 공지
					      </div>
					    </div>
					  </div>
						<div class="write">
							<div class="title">제목</div>
							<div class="input_box">
							  <label class="hidden_label" for="title">제목 입력</label>
								<input type="text" placeholder="제목을 입력해주세요" name="TITLE" id="title" class="search_input2">
							</div>
						</div>
						<div class="txtarea" style="border:none;">
						  <label class="hidden_label" for="summernote"></label> 
							<textarea  id="summernote" name="CONTENT"></textarea>
						</div>
						<div class="write">
							<div class="title">사진</div>
							<div class="input_box2 cursor">
						 	  <div class="gray_round_btn" style="width:108px;"><label for="img_file">파일첨부</label></div>
	          		<!-- 첨부된 파일 목록 -->
	            	<div class="img_box"></div>
	            </div>
	            <label class="hidden_label" for=""></label>
            	<input type="file" id="img_file"  onchange="insertImage(this)" name="img_file" style="opacity:0; width:0px; height: 0px;">
						</div>
					</div>
					<div class="btn_box">
						<button class="base_btn" id="insert_p_btn">저장</button>
						<a class="gray_btn"  href="/admin/board/ad_univ_news">취소</a>				
					</div>
				</form>
			</div>
		</div>
	</div>
</section>
</body>
</html>