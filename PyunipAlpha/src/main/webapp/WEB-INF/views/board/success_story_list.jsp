<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html>
<c:choose>
   <c:when test="${error eq null }">
	<c:choose>
	  <c:when test="${nMaxRecordCnt eq 0}">
	   <div class="none">조회할 게시물이 없습니다.</div>
	  </c:when>
	  <c:otherwise>
	  <div class="study_box2">
	    <c:forEach var="a" items="${list}">
			<a class="list_item4" href="javascript:goDetail('${a.BRD_NUM}')">
			  <div class="item_content_box2">
			  	<div class="item_info_wrap2">
			  		<span>${a.SUC_UNIV }</span>
			  		<span>${a.SUC_YY }년</span>
			  		<span>합격수기</span>
			  	</div>
			  	<div class="item_writer"><c:out value="${a.USER_NM}"></c:out></div>
			    <div class="item_title"><c:out value="${a.TITLE}"></c:out></div>
			    <div class="item_txt">${a.CONTENT}</div>
			    <div class="item_middle_line"></div>
			    <div class="item_info_wrap">
			      <div class="item_info">
			        <img src="../image/icon/view.png" alt=""><c:out value="${a.VIEW_CNT}"></c:out>
			      </div>
		          <div class="item_info" aria-label="좋아요수">
		            <c:choose>
		              <c:when test="${a.LIKE_YN eq 'Y'}">
		                <img src="../image/icon/like_on.png" alt="꽉찬하트">
		              </c:when>
		              <c:otherwise>
		                <img src="../image/icon/like.png" alt="빈하트">
		              </c:otherwise>
		            </c:choose>       
		            <c:out value="${a.LIKE_CNT }"></c:out>
		          </div>
			    </div>
			  </div>
			</a>   
	    </c:forEach>  
	   </div>
	  </c:otherwise>
	</c:choose>
	<div class="paging">
	 <input type="hidden" id="nMaxRecordCnt" value="${nMaxRecordCnt}">
	 <input type="hidden" id="nMaxVCnt" value="${nMaxVCnt}">
	</div>
   </c:when>
   <c:otherwise>
   <div class="none"><c:out value="${error }"></c:out></div>
   </c:otherwise>
</c:choose>