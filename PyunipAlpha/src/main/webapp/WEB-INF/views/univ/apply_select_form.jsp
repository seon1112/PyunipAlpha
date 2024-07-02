<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>      
<table class="info_table2 mobile_hidden">
  <caption>전형방법, 지원자격, 진행 요소 별 반영 비율 및 배점</caption>
  <colgroup>
    <col style="width:40%">
    <col style="width:8%;">
    <col>
  </colgroup>
  <thead>
    <tr>
      <th scope="col" rowspan="2">전형방법</th>
      <th scope="col" rowspan="2">지원자격<br/>(이수학점)</th>
      <th scope="col" colspan="9">진행 요소 별 반영 비율 및 배점</th>
    </tr>
    <tr>
      <th scope="col">사정모형</th>
      <th scope="col">영어</th>
      <th scope="col">수학</th>
      <th scope="col">전공/계열기초</th>
      <th scope="col">전전대 성적</th>
      <th scope="col">면접</th>
      <th scope="col">공인영어</th>
      <th scope="col">기타</th>
      <th scope="col" style="border-right : none;">비고</th>
    </tr>
  </thead>
  <tbody>
	<c:choose>
	   <c:when test="${error eq null }">
	    <c:choose>
	     <c:when test="${size eq 0}">
	      <tr>
	        <td colspan="11">자료 준비 중 입니다.</td>
	      </tr>
	     </c:when>
	     <c:otherwise>
	      <c:forEach var="s" items="${list}">
	        <c:choose>
	          <c:when test="${s.size eq 1}">
	            <tr>
	              <td class="left"><c:out value="${s.SLT_WAY }"></c:out></td>
	              <td><c:out value="${s.APY_QUAL}"></c:out></td>
	              <td><c:out value="${s.SLT_STEP}"></c:out></td>
	              <td><c:out value="${s.ENG_PC }"></c:out></td>
	              <td><c:out value="${s.MATH_PC }"></c:out></td>
	              <td><c:out value="${s.MAJOR_PC }"></c:out></td>
	              <td><c:out value="${s.PREV_GRADE }"></c:out></td>
	              <td><c:out value="${s.INTV_PC }"></c:out></td>
	              <td><c:out value="${s.RECOG_ENG_PC }"></c:out></td>
	              <td><c:out value="${s.WHATEVER }"></c:out></td>
	              <td><c:out value="${s.WHATEVER_NOTE }"></c:out></td>
	            </tr>        
	          </c:when>
	          <c:otherwise>
	            <tr>
	              <td  rowspan="2" class="left"><c:out value="${s.SLT_WAY }"></c:out></td>
	              <td  rowspan="2"><c:out value="${s.APY_QUAL}"></c:out></td>	            
	              <td><c:out value="${s.SLT_STEP}"></c:out></td>
	              <td><c:out value="${s.ENG_PC }"></c:out></td>
	              <td><c:out value="${s.MATH_PC }"></c:out></td>
	              <td><c:out value="${s.MAJOR_PC }"></c:out></td>
	              <td><c:out value="${s.PREV_GRADE }"></c:out></td>
	              <td><c:out value="${s.INTV_PC }"></c:out></td>
	              <td><c:out value="${s.RECOG_ENG_PC }"></c:out></td>
	              <td><c:out value="${s.WHATEVER }"></c:out></td>
	              <td><c:out value="${s.WHATEVER_NOTE }"></c:out></td>
	            </tr>
	            <tr>
	              <td><c:out value="${s.SLT_STEP2}"></c:out></td>
	              <td><c:out value="${s.ENG_PC2 }"></c:out></td>
	              <td><c:out value="${s.MATH_PC2 }"></c:out></td>
	              <td><c:out value="${s.MAJOR_PC2 }"></c:out></td>
	              <td><c:out value="${s.PREV_GRADE2 }"></c:out></td>
	              <td><c:out value="${s.INTV_PC2 }"></c:out></td>
	              <td><c:out value="${s.RECOG_ENG_PC2 }"></c:out></td>
	              <td><c:out value="${s.WHATEVER2 }"></c:out></td>
	              <td><c:out value="${s.WHATEVER_NOTE2 }"></c:out></td>
	            </tr>      
	          </c:otherwise>
	        </c:choose>
	       </c:forEach>
	      </c:otherwise>    
	    </c:choose>    	   
	   </c:when>
	   <c:otherwise>
        <tr>
          <td colspan="11"><c:out value="${error }"></c:out></td>
        </tr>	    
	   </c:otherwise>
	</c:choose> 
  </tbody>
</table>
<div class="mobile_view mobile_box">
<c:forEach var="s" items="${list}">
	<div class="mobile_info_wrap">
		<div class="mobile_info_box">
			<div class="mobile_info_title">전형방법</div>
			<div class="mobile_info_content">${s.SLT_WAY }</div>
		</div>
		<div class="mobile_info_box3">
			<div class="mobile_info_title">지원자격(이수 학점)</div>
			<div class="mobile_info_content">${s.APY_QUAL }</div>
		</div>
		<div class="mobile_info_box2">진행 요소 별 반영 비율 및 배점</div>
		<div class="mobile_info_box3">
			<div class="mobile_info_title">사정모형</div>
			<div class="mobile_info_content">${s.SLT_STEP }</div>
		</div>		
		<div class="mobile_info_table">
			<div class="mobile_info_table_title_box">
				<div class="info_table_title">영어</div>
				<div class="info_table_title">수학</div>
				<div class="info_table_title">전공/계열기초</div>
				<div class="info_table_title">전전대 성적</div>
				<div class="info_table_title">면접</div>
				<div class="info_table_title">공인 영어</div>
				<div class="info_table_title">기타</div>
				<div class="info_table_title">비고</div>
			</div>
			<div class="mobile_info_table_content_box">
				<div class="info_table_content"><c:out value="${s.ENG_PC  }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.MATH_PC }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.MAJOR_PC }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.PREV_GRADE }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.INTV_PC }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.RECOG_ENG_PC }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.WHATEVER }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.WHATEVER_NOTE }"></c:out></div>
			</div>
		</div>		
		<c:if test="${s.size eq 2}">
		<div class="mobile_info_box3">
			<div class="mobile_info_title">사정모형</div>
			<div class="mobile_info_content">${s.SLT_STEP2 }</div>
		</div>		
		<div class="mobile_info_table">
			<div class="mobile_info_table_title_box">
				<div class="info_table_title">영어</div>
				<div class="info_table_title">수학</div>
				<div class="info_table_title">전공/계열기초</div>
				<div class="info_table_title">전전대 성적</div>
				<div class="info_table_title">면접</div>
				<div class="info_table_title">공인 영어</div>
				<div class="info_table_title">기타</div>
				<div class="info_table_title">비고</div>
			</div>
			<div class="mobile_info_table_content_box">
				<div class="info_table_content"><c:out value="${s.ENG_PC2  }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.MATH_PC2 }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.MAJOR_PC2 }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.PREV_GRADE2 }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.INTV_PC2 }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.RECOG_ENG_PC2 }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.WHATEVER2 }"></c:out></div>
				<div class="info_table_content"><c:out value="${s.WHATEVER_NOTE2 }"></c:out></div>
			</div>
		</div>				
		</c:if>
	</div>
</c:forEach>
</div>