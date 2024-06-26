<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>      
<table class="info_table2 mobile_hidden">
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
	           <td><c:out value="${t.UNIV_NAME }"></c:out></td>
	           <td><c:out value="${t.REG_YY }년"></c:out></td>
	           <td><c:out value="${t.SERIES }"></c:out></td>
	           <td><c:out value="${t.DEPT_NM }"></c:out></td>
	           <td><c:out value="${t.MAJOR_NM }"></c:out></td>
	           <td><c:out value="${t.RECRU_SIZE }"></c:out></td>
	           <td><c:out value="${t.APY_SIZE }"></c:out></td>
	           <td><c:out value="${t.COMPETITION }"></c:out></td>
	           <td><c:out value="${t.SLT_WAY }"></c:out></td>
	         </tr>
	       </c:forEach>
	     </c:otherwise>
	   </c:choose>	   
	   </c:when>
	   <c:otherwise>
         <tr>
           <td colspan="9"><c:out value="${error }"></c:out></td>
         </tr>
	   </c:otherwise>
	</c:choose>  
  </tbody>
</table>
<div class="mobile_view mobile_box">
<c:choose>
	<c:when test="${nMaxRecordCnt eq 0}">
	<div class="mobile_info_wrap">자료 준비 중 입니다.</div>
	</c:when>
	<c:otherwise>
	<c:forEach var="t" items="${list }">
	<div class="mobile_info_wrap">
		<div class="mobile_info_box4">
			<div class="mobile_info_title">대학명</div>
			<div>:</div>
			<div class="mobile_info_content"><c:out value="${t.UNIV_NAME  }"></c:out></div>
			<div class="mobile_info_title">모집년도</div>
			<div>:</div>
			<div class="mobile_info_content"><c:out value="${t.REG_YY }년"></c:out></div>			
			<div class="mobile_info_title">계열</div>
			<div>:</div>
			<div class="mobile_info_content"><c:out value="${t.SERIES  }"></c:out></div>
			<div class="mobile_info_title">모집단위 </div>
			<div>:</div>
			<div class="mobile_info_content"><c:out value="${t.MAJOR_NM  }"></c:out></div>
			<div class="mobile_info_title">모집인원</div>
			<div>:</div>
			<div class="mobile_info_content"><c:out value="${t.RECRU_SIZE }명"></c:out></div>
			<div class="mobile_info_title">지원인원</div>
			<div>:</div>
			<div class="mobile_info_content"><c:out value="${t.APY_SIZE}명"></c:out></div>
			<div class="mobile_info_title">경쟁률</div>
			<div>:</div>
			<div class="mobile_info_content"><c:out value="${t.COMPETITION}"></c:out></div>
			<div class="mobile_info_title">전형</div>
			<div>:</div>
			<div class="mobile_info_content"><c:out value="${t.SLT_WAY}"></c:out></div>
		</div>
	</div>
	</c:forEach>	
	</c:otherwise>
</c:choose>
</div>
<div class="paging">
  <input type="hidden" id="nMaxRecordCnt" value="${nMaxRecordCnt}">
  <input type="hidden" id="nMaxVCnt" value="${nMaxVCnt}">
</div>