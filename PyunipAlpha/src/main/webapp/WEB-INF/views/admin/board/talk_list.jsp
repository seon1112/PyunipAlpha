<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<table class="table1">
   <colgroup>
    <col width="10%"></col>
    <col width="10%"></col>
    <col></col>
    <col width="10%"></col>
    <col width="10%"></col>
    <col width="10%"></col>
    <col width="10%"></col>
   </colgroup>
   <thead>
    <tr>
      <th scope="col">번호</th>
      <th scope="col">구분</th>
      <th scope="col">제목</th>
      <th scope="col">작성자</th>
      <th scope="col">등록일</th>
      <th scope="col">조회수</th>
      <th scope="col">좋아요수</th>
    </tr>
   </thead>
   <tbody>
   <c:choose>
    <c:when  test="${nMaxRecordCnt eq 0}">
      <tr>
        <td colspan="7">조회할 게시물이 없습니다.</td>
      </tr>
    </c:when>
    <c:otherwise>
    <c:forEach var="t" items="${list}" varStatus="vs">
       <tr>
         <td>${nMaxRecordCnt - vs.index - nSelectPage + 1}</td>
         <c:choose>
           <c:when test="${t.BRD_DV eq 'F'}">
              <td>자유주제</td>
           </c:when>
           <c:otherwise>
              <td>질문하기</td>
           </c:otherwise>
         </c:choose>         
         <td class="left"><a href="javascript:goDetail('${t.BRD_NUM}')">${t.TITLE}</a></td>
         <td>${t.USER_NM }</td>
         <td>${t.REG_DTM}</td>
         <td>${t.VIEW_CNT }</td>
         <td>${t.LIKE_CNT }</td>
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
