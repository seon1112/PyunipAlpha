<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
<title>[관리자페이지]편입이야기</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript" src="/js/ad_board.js"></script> 
<script>
const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

$(function(){
	goTab(1);
	$(".big_gray_tab").click(function(){
		$(this).siblings().removeClass("selected");
		$(this).addClass("selected");
	});
});

function deleteBoard(){
  if(!confirm('정말로 삭제하시겠습니까?')){
    return;
  }
  $.ajax({
    url:'/user/board',
    type:'delete',
    data:{
       BRD_NUM :$("#BRD_NUM").val()  
      ,BRD_CTG :$("#BRD_CTG").val()
    },
    cache : false,
    beforeSend : function(xhr) {
        xhr.setRequestHeader(header, token);
    },
    success:function(msg){
      if(msg == ""){
        location.href='/admin/board/ad_talk';
      }else{
        alert(msg);       
      }
    },
    error:function(html){
      alert("error:"+html);
    }
  });
}
function goTab(num){
	if(num == 1){
		$("#content_info").css("display","flex");
		$("#rpy_info").css("display","none");
	}else if(num == 2){
		$("#content_info").css("display","none");
		$("#rpy_info").css("display","flex");		
	}
}

/**
 * 댓글 삭제
 */
function deleteRpy(RPY_NUM){
  if(!confirm("댓글을 삭제하시겠습니까?")){
	  return;
  }	
  
  $.ajax({
    url:'/user/rpy',
    type:'delete',
    data:{
      RPY_NUM:RPY_NUM
    },
    cache : false,
    beforeSend : function(xhr) {
        xhr.setRequestHeader(header, token);
    },    
    success:function(msg){
      if(msg != ""){
          alert(msg);
      }else{
    	  location.reload();  
      }      
    },
    error:function(html){
      alert("error:"+html);
    }
  });
}

/**
 * 대댓글 삭제
 */
function deleteReRpy(RPY_NUM, RE_RPY_NUM){
  if(!confirm("댓글을 삭제하시겠습니까?")){
	  return;
  }
  
  $.ajax({
      url:'/user/reRpy',
      type:'delete',
      data:{
        RPY_NUM:RPY_NUM
        ,RE_RPY_NUM:RE_RPY_NUM
      },
      cache : false,
      beforeSend : function(xhr) {
          xhr.setRequestHeader(header, token);
      },      
      success:function(msg){
        if(msg != ""){
           alert(msg);
       }else{
    	   location.reload();
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
<!-- top header -->
<jsp:include page="/WEB-INF/views/inc/ad_header.jsp">
	<jsp:param value="selected" name="menu_2"/>
</jsp:include>
<!-- sidebar -->
<jsp:include page="/WEB-INF/views/inc/ad_sidebar.jsp">
	<jsp:param value="selected" name="memu_3"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>편입이야기</span>
    </div>
	<div class="content">
	  <div class="tab_box">
	    <button type="button" class="big_gray_tab selected" onclick="goTab(1)">내용</button>
	    <button type="button" class="big_gray_tab" onclick="goTab(2)">댓글</button>
	  </div>	
	<!--  -->
	  <div class="content_box" id="content_info">
	   <c:choose>
	   <c:when test="${error eq null }">
	      <div class="write_box">
            <div class="write border">
              <div class="title">구분</div>
		      <c:choose>
		        <c:when test="${t.BRD_DV eq 'F'}">
		         <div class="input_box">자유주제</div>
		        </c:when>
 			        <c:otherwise>
                 <div class="input_box">질문하기</div>
		        </c:otherwise>
		      </c:choose>
            </div>		      
            <div class="write border">
              <div class="title">제목</div>
              <div class="input_box">${t.TITLE}</div>
            </div>		      
            <div class="write border">
              <div class="title">작성자</div>
              <div class="input_box">${t.USER_NM }</div>
            </div>		      
            <div class="write border">
              <div class="title">등록일</div>
              <div class="input_box">${t.REG_DTM}</div>
            </div>		      
            <div class="write border">
              <div class="title">조회수</div>
              <div class="input_box">${t.VIEW_CNT }</div>
            </div>		      
            <div class="write border">
              <div class="title">좋아요수</div>
              <div class="input_box">${t.LIKE_CNT }</div>
            </div>
            <div class="board_content">
              <div class="content_txt">${t.CONTENT}</div>
            </div>		      
          </div>  
	   </c:when>
	   <c:otherwise>
	    <div>${error }</div>
	   </c:otherwise>
	   </c:choose>
	   <!-- 목록버튼 -->  
	   <div class="btn_box">
         <a class="base_btn" href="/admin/board/talk_update?BRD_NUM=${t.BRD_NUM}">수정</a>      
         <button type="button" class="base_btn" onclick="deleteBoard()">삭제</button>      
	     <a class="gray_btn"  href="/admin/board/ad_talk">목록</a>  
	   </div>
	  </div>
	 <!--  -->
	 <div class="content_box" id="rpy_info" style="margin-bottom: 10px;">
		<table class="table1 main30">
		   <colgroup>
		    <col style="width:170px"></col>
		    <col style="width:170px"></col>
		    <col></col>
		    <col style="width:170px"></col>
		   </colgroup>
		   <thead>
		    <tr>
		      <th scope="col">작성자</th>
		      <th scope="col">작성일</th>
		      <th scope="col">내용</th>
		      <th scope="col"></th>
		    </tr>
		   </thead>
		   <tbody>
		   <c:choose>
		   	<c:when test="${error2 eq null }">
			   <c:choose>
			    <c:when  test="${size eq 0}">
			      <tr>
			        <td colspan="4">조회할 게시물이 없습니다.</td>
			      </tr>
			    </c:when>
			    <c:otherwise>
			    <c:forEach var ="r" items="${list}">
			    	<c:choose>
			    	<c:when test="${r.RE_RPY_NUM == 0}">
				       <tr>
				         <td>${r.USER_NM}</td>
				         <td>${r.REG_DTM}</td>
				         <td class="left">${r.CONTENT}</td>
				         <td class="del_icon"><button class="del_icon" onclick="deleteRpy('${r.RPY_NUM}')"></button></td>
				       </tr>			    	
			    	</c:when>
			    	<c:otherwise>
				       <tr>
				         <td>${r.USER_NM}</td>
				         <td>${r.REG_DTM}</td>
				         <td class="dleft"> →${r.CONTENT}</td>
				         <td class="del_icon"><button class="del_icon" onclick="deleteReRpy('${r.RPY_NUM}','${r.RE_RPY_NUM}')"></button></td>
				       </tr>				    	
			    	</c:otherwise>
			    	</c:choose>
			    </c:forEach>
			    </c:otherwise>
			   </c:choose>   	
		   	</c:when>
		   	<c:otherwise>
		      <tr>
		        <td colspan="4">${error2 }</td>
		      </tr>   	
		   	</c:otherwise>
		   </c:choose>
		   </tbody>
		</table>
	</div>
	<input type="hidden" id="BRD_CTG" value="0">
	<input type="hidden" id="BRD_NUM" value="${t.BRD_NUM}">
	</div>
  </div>
</section>
</body>
</html>