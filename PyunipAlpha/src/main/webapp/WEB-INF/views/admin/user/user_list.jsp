<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>      
<table class="table1">
  <caption>회원번호,닉네임,이메일,가입일,최근접속일,권한</caption>
  <colgroup>
    <col>
    <col>
    <col>
    <col>
  </colgroup>
  <thead>
    <tr>
      <th scope="col">아이디</th>
      <th scope="col">이메일</th>
      <th scope="col">가입일</th>
      <th scope="col">최근 접속일</th>
      <th scope="col">권한</th>
    </tr>
  </thead>
  <tbody>
	<c:choose>
	   <c:when test="${error eq null }">
	   <c:choose>
	     <c:when test="${nMaxRecordCnt eq 0}">
	       <tr>
	         <td colspan="6">조회할 자료가 없습니다.</td>
	       </tr>
	     </c:when>
	     <c:otherwise>
	       <c:forEach var="t" items="${list }">
	         <tr>
	           <td><a href="javascript:goDetail('${t.USER_NUM }')">${t.USER_NM }</a></td>
	           <td>${t.EMAIL }</td>
	           <td>${t.REG_DTM }</td>
	           <td>${t.RCT_REG_DTM }</td>
	           <td>${t.ROLE }</td>
	         </tr>
	       </c:forEach>
	     </c:otherwise>
	   </c:choose>	   
	   </c:when>
	   <c:otherwise>
         <tr>
           <td colspan="6">${error }</td>
         </tr>
	   </c:otherwise>
	</c:choose>  
  </tbody>
</table>
<div class="paging">
  <input type="hidden" id="nMaxRecordCnt" value="${nMaxRecordCnt}">
  <input type="hidden" id="nMaxVCnt" value="${nMaxVCnt}">
</div>