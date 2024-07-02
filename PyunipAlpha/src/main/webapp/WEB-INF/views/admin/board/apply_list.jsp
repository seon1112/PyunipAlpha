<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<table class="table1">
   <colgroup>
    <col width="10%"></col>
    <col></col>
    <col width="20%"></col>
    <col width="10%"></col>
    <col width="10%"></col>
   </colgroup>
   <thead>
    <tr>
      <th scope="col">번호</th>
      <th scope="col">제목</th>
      <th scope="col">등록일</th>
      <th scope="col">상단공지여부</th>
      <th scope="col">첨부파일여부</th>
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
         <td>${nMaxRecordCnt - vs.index - (nSelectPage-1)*nMaxVCnt}</td>
         <td class="left"><a href="javascript:goDetail('${a.BRD_NUM}')">${a.TITLE}</a></td>
         <td>${a.REG_DTM}</td>
         <td>${a.TOP_NOTICE_YN }</td>
         <td>${a.FILE_YN}</td>
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
