<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul class="paging_ul">
	<li class="prev" data-page='1'><img src="/image/icon/paging_first.png" style="height: 24px; width:24px;" alt="처음 목록으로"></li>
	<c:choose>
		<c:when test="${(nCurStartPage - nMaxHCnt + 1) > 1}">
			<li class="prev" data-page='${(nCurStartPage-nMaxHCnt)}'><img src="/image/icon/paging_prev.png" style="height: 24px; width:24px;" alt="이전 5페이지 보기"></li>
		</c:when>
		<c:otherwise>
			<li class="prev" data-page='1'><img src="/image/icon/paging_prev.png" style="height: 24px; width:24px;" alt="처음 목록으로"></li>			
		</c:otherwise>
	</c:choose>
		<c:forEach begin="0" end="${nRealMaxHPageCnt - 1}" var="i">
		    <c:set var="currentPage" value="${nCurStartPage + i}" />
		    <c:choose>
		        <c:when test="${currentPage eq nSelectPage}">
		            <li class="select_on" data-page='${currentPage}'>${currentPage}</li>
		        </c:when>
		        <c:otherwise>
		            <li data-page='${currentPage}'>${currentPage}</li>
		        </c:otherwise>
		    </c:choose>
		</c:forEach>
	<c:choose>
		<c:when test="${(nCurStartPage + nMaxHCnt - 1) < nMaxPage}">
		    <li class="next" data-page='${nCurStartPage + nMaxHCnt}'><img src="/image/icon/paging_next.png" style="height: 24px; width:24px;" alt="다음 5페이지 보기"></li>
			<li class="next" data-page='${nextPage}'><img src="/image/icon/paging_last.png" style="height: 24px; width:24px;" alt="마지막 목록으로"></li>
		</c:when>
		<c:otherwise>
		    <li class="next" data-page='${nMaxPage }'><img src="/image/icon/paging_next.png" style="height: 24px; width:24px;" alt="다음 5페이지 보기"></li>
			<li class="next" data-page='${nMaxPage }'><img src="/image/icon/paging_last.png" style="height: 24px; width:24px;" alt="마지막 목록으로"></li>
		</c:otherwise>
	</c:choose>
</ul>
