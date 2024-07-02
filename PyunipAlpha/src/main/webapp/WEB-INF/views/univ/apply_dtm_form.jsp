<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>        
<table class="info_table2">
  <caption>편입 일정 및 비고</caption>
  <colgroup>
    <col style="width:20%">
    <col style="width:40%;">
    <col style="width:40%">
  </colgroup>
  <thead>
    <tr>
      <th scope="col"></th>
      <th scope="col">일시</th>
      <th scope="col">비고</th>
    </tr>
  </thead>
	<c:choose>
	   <c:when test="${error eq null }">
		  <tbody>
		    <tr>
		      <td>원서접수</td>
		      <td><c:out value="${s.APY_ST_DTM} ~ ${s.APY_ED_DTM }"></c:out></td>
		      <td><c:out value="${s.APY_NOTE}"></c:out></td>
		    </tr>
		    <tr>
		      <td>전형일</td>
		      <td><c:out value="${s.SLT_DATE}"></c:out></td>
		      <td><c:out value="${s.SLT_NOTE}"></c:out></td>
		    </tr>
		    <tr>
		      <td>합격자발표</td>
		      <c:choose>
		        <c:when test="${size eq 2 }">
		        <td>
		          <c:forEach var="c" items="${suc }" varStatus="vs">
		            <div><c:out value="${vs.index +1}차 발표 : ${c}"></c:out></div>
		          </c:forEach>
		        </td>        
		        </c:when>
		        <c:otherwise>
		         <td>
             <c:forEach var="c" items="${suc }" varStatus="vs">
               <div><c:out value="${c}"></c:out></div>
             </c:forEach>		         
		         </td>
		        </c:otherwise>
		      </c:choose>
		      <td><c:out value="${s.SUC_APY_NOTE}"></c:out></td>
		    </tr>
		    <tr>
		      <td>서류제출</td>
		      <td><c:out value="${doc[0]} ~ ${doc[1] }"></c:out></td>
		      <td><c:out value="${s.DOC_SUB_NOTE }"></c:out></td>
		    </tr>
		    <tr>
		      <td>면접일정</td>
		      <c:choose>
		        <c:when test="${size2 eq 2 }">
		        <td>
		          <c:forEach var="t" items="${intv }" varStatus="vs">
		            <div><c:out value="${vs.index +1}차 면접 : ${t}"></c:out></div>
		          </c:forEach>
		        </td>       
		        </c:when>
		        <c:otherwise>
             <td>
              <c:forEach var="t" items="${intv }" varStatus="vs">
                <div><c:out value="${t}"></c:out></div>
              </c:forEach>
            </td>     		         
		        </c:otherwise>
		      </c:choose>
		      <td><c:out value="${s.INTV_NOTE }"></c:out></td>
		    </tr>
		  </tbody>
	   </c:when>
	   <c:otherwise>
	    <tbody>
	     <tr>
	       <td colspan="3"><c:out value="${error }"></c:out></td>
	     </tr>
	    </tbody>
	   </c:otherwise>
	</c:choose>
</table>