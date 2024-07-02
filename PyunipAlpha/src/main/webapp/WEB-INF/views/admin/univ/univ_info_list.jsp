<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<table class="table1">
   <colgroup>
    <col width="10%"></col>
    <col></col>
    <col width="10%"></col>
    <col width="20%"></col>
    <col width="20%"></col>
   </colgroup>
   <thead>
    <tr>
      <th scope="col">일련번호</th>
      <th scope="col">대학명</th>
      <th scope="col">모집년도</th>
      <th scope="col">원서시작일</th>
      <th scope="col">원서종료일</th>
    </tr>
   </thead>
   <tbody>
   <c:choose>
    <c:when  test="${nMaxRecordCnt eq 0}">
      <tr>
        <td colspan="5">조회할 게시물이 없습니다.</td>
      </tr>
    </c:when>
    <c:otherwise>
    <c:forEach  var="a" items="${list}" varStatus="vs">
       <tr>
         <td>${a.UNIV_NUM}</td>
         <td class="left"><a href="javascript:goDetail('${a.UNIV_NUM}')">${a.UNIV_NAME}</a></td>
         <td>${a.REG_YY}년</td>
         <td>${a.APY_ST_DTM }</td>
         <td>${a.APY_ED_DTM}</td>
       </tr>
     </c:forEach>       
    </c:otherwise>
   </c:choose>
   </tbody>
</table>
<div class="paging">
 <input type="hidden" id="nMaxRecordCnt" value="${nMaxRecordCnt}">
 <input type="hidden" id="nMaxVCnt" value="${nMaxVCnt}">
</div>
