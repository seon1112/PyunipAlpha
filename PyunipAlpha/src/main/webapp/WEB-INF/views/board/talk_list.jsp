<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
 <input type="hidden" id="size" value="${size}">
 <c:choose>
  <c:when test="${error eq null }">
    <c:choose>
      <c:when test="${nMaxRecordCnt eq 0}">
        <div class="talk_item"><div>조회할 게시물이 없습니다.</div></div>
      </c:when>
      <c:otherwise>
	     <c:forEach var="t" items="${talkList}">
	     <div class="talk_item" onclick="goTalkDetail('${t.BRD_NUM}')">
	       <div class="talk_header">
	         <c:choose>
	           <c:when test="${t.BRD_DV eq 'F'}">
	              <div class="badge1">자유주제</div>
	           </c:when>
	           <c:otherwise>
	              <div class="badge2">질문하기</div>
	           </c:otherwise>
	         </c:choose>
	         <div class="talk_writer"><c:out value="${t.USER_NM }"></c:out></div>
	       </div>
	       <div class="talk_title"><c:out value="${t.TITLE}"></c:out></div>
	       <div class="talk_content">${t.CONTENT }</div>
	       <div class="item_info_box">
	         <div class="item_info_wrap">
	           <div class="item_info" aria-label="조회수">
	             <img src="../image/icon/view.png" alt="">
	             <c:out value="${t.VIEW_CNT }"></c:out>
	           </div>
	           <div class="item_info" aria-label="좋아요수">
	             <c:choose>
	               <c:when test="${t.LIKE_YN eq 'Y'}">
	                 <img src="../image/icon/like_on.png" alt="꽉찬하트">
	               </c:when>
	               <c:otherwise>
	                 <img src="../image/icon/like.png" alt="빈하트">
	               </c:otherwise>
	             </c:choose>       
	             <c:out value="${t.LIKE_CNT }"></c:out>
	           </div>
	         </div>    
	         <div class="item_date"><c:out value="${t.REG_DTM}"></c:out></div>    
	       </div>
	     </div>
	     </c:forEach>      
      </c:otherwise>
    </c:choose>
  </c:when>
  <c:otherwise>
    <div><c:out value="${error }"></c:out></div>
  </c:otherwise>
 </c:choose>
 <div class="paging">
    <input type="hidden" id="nMaxRecordCnt" value="${nMaxRecordCnt}">
    <input type="hidden" id="nMaxVCnt" value="${nMaxVCnt}">
 </div> 