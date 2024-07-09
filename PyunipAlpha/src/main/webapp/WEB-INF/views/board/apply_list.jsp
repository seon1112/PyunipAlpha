<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<c:choose>
   <c:when test="${error eq null }">
		<c:choose>
		  <c:when test="${nMaxRecordCnt eq 0}">
		    <div class="content_item">
		      <div class="title">조회할 게시물이 없습니다.</div>
		    </div>
		  </c:when>
		  <c:otherwise>
		    <c:forEach var="a" items="${list}" varStatus="vs">
		      <c:choose>
		        <c:when test="${a.TOP_NOTICE_YN eq 'Y'}">
		          <a class="content_item notice" href="javascript:goDetail('${a.BRD_NUM}')">
		            <!-- 상단공지 -->
		            <div class="notice_icon" aria-label="상단공지"></div>
		            <div class="title"><c:out value="${a.TITLE}"></c:out>
		             <c:if test="${a.FILE_YN eq 'Y'}">
		               <img src="../image/icon/file.png" alt="파일 아이콘">
		             </c:if>
		            </div>
		            <div class="date" aria-label="등록일"><c:out value="${a.REG_DTM}"></c:out></div>
		          </a>    
		        </c:when>
		        <c:otherwise>
		          <a class="content_item" href="javascript:goDetail('${a.BRD_NUM}')">
		            <div class="number"><c:out value="${nMaxRecordCnt - vs.index - (nSelectPage-1)*nMaxVCnt}"></c:out></div>
		            <div class="title"><c:out value="${a.TITLE}"></c:out>
		             <c:if test="${a.FILE_YN eq 'Y'}">
		               <img src="../image/icon/file.png" alt="파일 아이콘">
		             </c:if>        
		            </div>
		            <div class="date"  aria-label="등록일"><c:out value="${a.REG_DTM}"></c:out></div>
		          </a>    
		        </c:otherwise>
		      </c:choose>
		    </c:forEach>  
		  </c:otherwise>
		</c:choose>
		<div class="paging">
		 <input type="hidden" id="nMaxRecordCnt" value="${nMaxRecordCnt}">
		 <input type="hidden" id="nMaxVCnt" value="${nMaxVCnt}">
		</div>
   </c:when>
   <c:otherwise>
    <div class="content_item">
      <div class="title"><c:out value="${error }"></c:out></div>
    </div>
   </c:otherwise>
</c:choose>

