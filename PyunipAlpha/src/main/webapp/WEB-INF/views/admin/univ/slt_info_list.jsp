<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>      
<table class="table1">
  <caption>대학명, 전형방법, 모집년도, 지원자격(이수학점)</caption>
  <colgroup>
    <col>
    <col>
    <col>
    <col>
    <col>
    <col>
    <col>
  </colgroup>
  <thead>
    <tr>
      <th scope="col">대학명</th>
      <th scope="col">전형방법</th>
      <th scope="col">지원자격(이수학점)</th>
      <th scope="col">공개여부</th>
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
	           <td>${t.UNIV_NAME }</td>
	           <td><a href="javascript:goDetail('${t.SLT_NUM }')">${t.SLT_WAY }</a></td>
	           <td>${t.APY_QUAL }</td>
	           <td>${t.OPEN_YN }</td>
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