<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<c:choose>
   <c:when test="${error eq null }">
		<c:forEach var ="r" items="${list}">
		  <c:choose>
		  <c:when test="${r.RE_RPY_NUM == 0}">
		    <div class="study_info_midle_line"></div>
		    <div class="rpy_form">
		      <div class="rpy_header">
		        <div class="rpy_writer"><c:out value="${r.USER_NM }"></c:out></div>
		        <div class="rpy_date"><c:out value="${r.REG_DTM}"></c:out></div>
		        <div class="rpy_middle_line"></div>
		        <button type="button" class="rpy_btn" onclick="openRpy($(this))">댓글쓰기</button>
		        <c:if test="${r.REG_USER_NUM eq USER_NUM }" >
		          <div class="rpy_menu">
		            <button type="button" class="rpy_menu_btn" onclick="openMenu($(this))"></button>
		            <div class="rpy_menu_box">
		              <button type="button" class="rpy_del_btn" onclick="deleteRpy(this.getAttribute('data-rpynum'))" data-rpynum="${r.RPY_NUM}">댓글삭제</button>
		            </div>
		          </div>
		        </c:if>
		      </div>
		      <div class="rpy_content"><c:out value="${r.CONTENT}"></c:out></div>
		    </div>
		    <!-- 대댓글 -->
		    <div class="rpy_rpy_form">
		      <label class="hidden_label" for="re_rpy_content${r.RPY_NUM}">댓글입력</label>
		      <textarea class="rpy_textarea" placeholder="댓글을 입력해주세요" id="re_rpy_content${r.RPY_NUM}"></textarea>
		      <div class="rpy_search_btn">
		        <button id="insert_re_rpy" type="button" class="org_btn" onclick="insertReRpy(this.getAttribute('data-rpynum'))" data-rpynum="${r.RPY_NUM}">댓글 등록</button>
		      </div>   
		    </div>
		  </c:when>
		  <c:otherwise>
		  <!-- midle line -->     
		  <div class="study_info_midle_line"></div>
		  <div class="rpy_form rpy_rpy">
		    <div class="rpy_header">
		      <div class="rpy_writer"><c:out value="${r.USER_NM }"></c:out></div>
		      <div class="rpy_date"><c:out value="${r.REG_DTM}"></c:out></div>
		      <div class="rpy_middle_line"></div>
		      <c:if test="${r.REG_USER_NUM eq USER_NUM }" >
		        <div class="rpy_menu">
		          <button type="button" class="rpy_menu_btn" onclick="openMenu($(this))"></button>
		          <div class="rpy_menu_box">
		            <button type="button" class="rpy_del_btn" onclick="deleteReRpy(this.getAttribute('data-rpynum'),this.getAttribute('data-rerpynum'))" data-rpynum="${r.RPY_NUM}" data-rerpynum="${r.RE_RPY_NUM}">댓글삭제</button>
		          </div>
		        </div>
		      </c:if>
		    </div>
		    <div class="rpy_content"><c:out value="${r.CONTENT}"></c:out></div>
		  </div>
		  </c:otherwise>
		  </c:choose>
    </c:forEach>   
   </c:when>
   <c:otherwise>
    <div class="study_info_midle_line"></div>
    <div class="rpy_form">${error }</div>
   </c:otherwise>
</c:choose>

<input type="hidden" id="size" value="${size}"> 