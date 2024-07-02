<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>      
<table class="table1">
  <caption>접속시간,아이디,권한,접근IP</caption>
  <colgroup>
    <col>
    <col>
    <col>
    <col>
    <col>
  </colgroup>
  <thead>
    <tr>
      <th scope="col">접속시간</th>
      <th scope="col">아이디</th>
      <th scope="col">권한</th>
      <th scope="col">접근IP</th>
    </tr>
  </thead>
  <tbody>
	<c:choose>
	   <c:when test="${error eq null }">
	   <c:choose>
	     <c:when test="${nMaxRecordCnt eq 0}">
	       <tr>
	         <td colspan="4">조회할 자료가 없습니다.</td>
	       </tr>
	     </c:when>
	     <c:otherwise>
	       <c:forEach var="t" items="${list }">
	         <tr>
	           <td>${t.ACSS_DTM }</td>
	           <td>${t.USER_NM }</td>
	           <td>${t.ROLE }</td>
	           <td>${t.ACSS_IP }</td>
	         </tr>
	       </c:forEach>
	     </c:otherwise>
	   </c:choose>	   
	   </c:when>
	   <c:otherwise>
         <tr>
           <td colspan="4">${error }</td>
         </tr>
	   </c:otherwise>
	</c:choose>  
  </tbody>
</table>
<div class="paging">
  <input type="hidden" id="nMaxRecordCnt" value="${nMaxRecordCnt}">
  <input type="hidden" id="nMaxVCnt" value="${nMaxVCnt}">
</div>