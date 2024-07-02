<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<c:choose>
   <c:when test="${error eq null }">
    <c:choose>
      <c:when test="${nMaxRecordCnt eq 0}">
      <div class="study_box"><div>조회할 게시물이 없습니다.</div></div>
      </c:when>
      <c:otherwise>
	    <div class="study_box">
	       <c:forEach var="s" items="${list}">
	        <a class="list_item3" href="javascript:goDetail('${s.BRD_NUM}')">
	          <div class="title_wrap">
	            <div class="item_writer">
	            <c:out value="${s.USER_NM}"></c:out>
	            </div>
	            <c:if test="${s.ED_YN eq 'Y'}">
	              <div class="siren_box">마감 임박</div>   
	            </c:if>       
	          </div>      
	          <div class="item_title">${s.TITLE }</div>
	          <div class="item_sub_wrap">
	            <div>모집마감일 : <span><c:out value="${s.APPLY_ED_DTM }"></c:out></span></div>
	            <div>모집인원 : <span><c:out value="${s.APY_SIZE }명"></c:out></span></div>
	            <div>진행방법 : <span><c:out value="${s.PCD_WAY_ST}"></c:out></span></div>
	            <div>진행기간 : <span><c:out value="${s.ST_DTM} ~ ${s.ED_DTM}"></c:out></span></div>
	          </div>
	          <div class="item_footer">
	            <div class="item_info_wrap">
	              <div class="item_info" aria-label="조회수"> 
	                <img src="../image/icon/view.png" alt="">
	                <c:out value="${s.VIEW_CNT }"></c:out>
	              </div>
	           <div class="item_info" aria-label="좋아요수">
	             <c:choose>
	               <c:when test="${s.LIKE_YN eq 'Y'}">
	                 <img src="../image/icon/like_on.png" alt="꽉찬하트">
	               </c:when>
	               <c:otherwise>
	                 <img src="../image/icon/like.png" alt="빈하트">
	               </c:otherwise>
	             </c:choose>       
	             <c:out value="${s.LIKE_CNT }"></c:out>
	           </div>
	            </div>
	            <div><c:out value="${s.REG_DTM }"></c:out></div>
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
    <div>${error }</div>
   </c:otherwise>
</c:choose>
