<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>모집 요강 상세</title>
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<link rel="stylesheet" href="/css/univ.css" type="text/css">
<script>
$(function(){
	goTab(1,0,4);
	$(".gray_tab").click(function(){
		$(this).siblings().removeClass("selected");
		$(this).addClass("selected");
	});
});

$(document).on("change","input:radio[name='slt']",function(){
	if($(this).is(":checked")){
		var slt = $(this).val();
		
		goTab(3,1,slt);
	}
});

function goTab(n,nSelect,slt){
	var num=n;
	var url;
	if(num==1){
		url='/univ/apply_dtm_form'
	}else if(num==2){
		url='/univ/apply_select_form'
	}else if(num==3){
		url='/univ/apply_size_form'
	}
	$.ajax({
    type:'get',
    url:url,
    data:{
    	nSelect:nSelect
    	,SLT:slt
    	,UNIV_NUM:$("#UNIV_NUM").val()
    },
    success:function(html){
      $(".apply_info").html(html);
      
      if(num == 3){
        var nMaxRecordCnt=$("#nMaxRecordCnt").val();
        var nMaxVCnt=$("#nMaxVCnt").val();
        paging(nSelect,nMaxRecordCnt,nMaxVCnt);   
      }
    }
    ,error: function(html) {
       alert("error:"+html);
     }    
  });
}

/**
 * 페이징
 */
function paging(nSelect,nMaxRecordCnt,nMaxVCnt){
  $.ajax({
    url:'/paging'
    ,data:{
      nSelect:nSelect
      ,nMaxRecordCnt:nMaxRecordCnt
      ,nMaxVCnt:nMaxVCnt
    },
    success:function(html){
      $(".paging").html(html);
    }
    ,error: function(html) {
      alert("error:"+html);
    }
  });
}

/**
 * 페이지 이동
 */
$(document).on("click", ".paging_ul li", function(event) {
    var nSelect;
    var li = $(event.target).closest("li");
    $(li).addClass("select_on");
    $(li).siblings("li").removeClass("select_on");
    if($(li).data("page")!=''){
        nSelect=$(li).data("page");     
    }
    if (nSelect.toString().indexOf(".") !== -1) {
        nSelect = nSelect.toString().substring(0, nSelect.toString().indexOf("."));
    }
    var slt=$("input:radio[name='slt']:checked").val();
    goTab('3',nSelect,slt);
});

</script>
</head>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp">
  <jsp:param value="on" name="on_1"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<jsp:include page="/WEB-INF/views/inc/title_box.jsp">
  <jsp:param value="banner_selected" name="menu_7"/>
</jsp:include>
<div class="content content_box1">
<c:choose>
   <c:when test="${error eq null }">
	  <div class="univ_info_wrap">
	     <img alt="학교 로고" src="/upload/image/logo/${s.LOGO }">
	     <div class="univ_info">
	     <input type="hidden" id="UNIV_NUM" value="${s.UNIV_NUM}">
	     <table class="info_table">
	       <caption>학교 이름, 주소, 홈페이지, 전화번호</caption>
	       <colgroup>
	         <col class="col1">
	         <col>
	       </colgroup>
	       <tbody>
	        <tr>
	          <td colspan="2"><c:out value="${s.UNIV_NAME }"></c:out></td>
	        </tr>
	        <tr>
	          <th scope="row">주소</th>
	          <td><c:out value="${s.ADDR }"></c:out></td>
	        </tr>
	        <tr>
	          <th scope="row">홈페이지</th>
	          <td><a href="${s.URL}"><c:out value="${s.URL}"></c:out></a></td>
	        </tr>
	        <tr>
	          <th scope="row">전화번호</th>
	          <td><c:out value="${s.PHONE}"></c:out></td>
	        </tr>
	       </tbody>
	      </table>
	     </div>
	  </div>
   </c:when>
   <c:otherwise>
    <div class="univ_info_wrap">${error }</div>
   </c:otherwise>
</c:choose>
  <div class="tab_box apply_info_btn_list">
    <button type="button" class="gray_tab selected" onclick="goTab(1,0,4)">전형일정</button>
    <button type="button" class="gray_tab" onclick="goTab(2,0,4)">전형방법</button>
    <button type="button" class="gray_tab" onclick="goTab(3,1,4)">모집인원</button>
  </div>
  <div class="apply_info"></div>
  <div class="caution">일정은 변동될 수 있으므로 반드시 입학처 홈페이지를 확인해주세요.</div>
</div>
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>