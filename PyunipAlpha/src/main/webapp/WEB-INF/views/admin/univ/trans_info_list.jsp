<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>      
<table class="table1">
  <caption>대학명, 모집년도, 계열, 학부명 ,모집단위, 모집인원, 지원인원, 경쟁률, 전형방법</caption>
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
      <th scope="col">모집년도</th>
      <th scope="col">계열</th>
      <th scope="col">학부명</th>
      <th scope="col">모집 단위</th>
      <th scope="col">모집 인원</th>
      <th scope="col">지원 인원</th>
      <th scope="col">경쟁률</th>
      <th scope="col">전형 방법</th>
    </tr>
  </thead>
  <tbody>
	<c:choose>
	   <c:when test="${error eq null }">
	   <c:choose>
	     <c:when test="${nMaxRecordCnt eq 0}">
	       <tr>
	         <td colspan="9">조회할 자료가 없습니다.</td>
	       </tr>
	     </c:when>
	     <c:otherwise>
	       <c:forEach var="t" items="${list }">
	         <tr>
	           <td><a href="javascript:goDetail('${t.TRANS_NUM }','${t.UNIV_NUM}')">${t.UNIV_NAME }</a></td>
	           <td>${t.REG_YY }년</td>
	           <td>${t.SERIES }</td>
	           <td>${t.DEPT_NM }</td>
	           <td>${t.MAJOR_NM }</td>
	           <td>${t.RECRU_SIZE }</td>
	           <td>${t.APY_SIZE }</td>
	           <td>${t.COMPETITION }</td>
	           <td>${t.SLT_WAY }</td>
	         </tr>
	       </c:forEach>
	     </c:otherwise>
	   </c:choose>	   
	   </c:when>
	   <c:otherwise>
         <tr>
           <td colspan="9">${error }</td>
         </tr>
	   </c:otherwise>
	</c:choose>  
  </tbody>
</table>
<div class="paging">
  <input type="hidden" id="nMaxRecordCnt" value="${nMaxRecordCnt}">
  <input type="hidden" id="nMaxVCnt" value="${nMaxVCnt}">
</div>