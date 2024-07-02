<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>[관리자페이지]대학관리</title>
<jsp:include page="/WEB-INF/jsp/ad_import.jsp"></jsp:include>
<script type="text/javascript">
</script>
</head>
<body>
<!-- top header -->
<jsp:include page="/WEB-INF/views/inc/ad_header.jsp">
  <jsp:param value="selected" name="menu_1"/>
</jsp:include>
<!-- sidebar -->
<jsp:include page="/WEB-INF/views/inc/ad_sidebar2.jsp">
  <jsp:param value="selected" name="memu_1"/>
</jsp:include>
<!-- main section -->
<section class="ad_main">
  <div class="ad_content_box">
    <div class="header">
      <span>대학 정보 상세</span>
    </div>
    <div class="content">
    <c:choose>
       <c:when test="${error eq null }">
        <div class="content_box">
          <div class="write_box">
            <div class="write border">
              <div class="title">대학 일련번호</div>
              <div class="input_box">${s.UNIV_NUM}</div>
            </div>          
            <div class="write border">
              <div class="title">대학명</div>
              <div class="input_box">${s.UNIV_NAME }</div>
            </div>          
            <div class="write border">
              <div class="title">대학로고</div>
              <div class="input_box">
              <c:if test="${s.LOGO ne null}">
	              <img alt="학교 로고" src="/upload/image/logo/${s.LOGO}">
              </c:if>
              </div>
            </div>          
            <div class="write border">
              <div class="title">주소</div>
              <div class="input_box">${s.ADDR }</div>
            </div>          
            <div class="write border">
              <div class="title">홈페이지 URL</div>
              <div class="input_box">${s.URL}</div>
            </div> 
            <div class="write border">
              <div class="title">전화번호</div>
              <div class="input_box">${s.PHONE}</div>
            </div> 
            <div class="write border">
              <div class="title">모집년도</div>
              <div class="input_box">${s.REG_YY}년</div>
            </div> 
            <div class="write border">
              <div class="title">원서접수</div>
              <div class="input_box">${s.APY_ST_DTM} ~ ${s.APY_ED_DTM }</div>
            </div> 
            <div class="write border">
              <div class="title">원서접수 비고</div>
              <div class="input_box">${s.APY_NOTE}</div>
            </div> 
            <div class="write border">
              <div class="title">전형일</div>
              <div class="input_box">${s.SLT_DATE}</div>
            </div> 
            <div class="write border">
              <div class="title">전형일 비고</div>
              <div class="input_box">${s.SLT_NOTE}</div>
            </div> 
            <div class="write border">
              <div class="title">합격자발료</div>
              <div class="input_box">
		          <c:choose>
		            <c:when test="${size eq 2 }">
		              <c:forEach var="c" items="${suc }" varStatus="vs">
		                <div><strong>${vs.index +1}차 발표</strong> : ${c}</div>
		              </c:forEach>
		            </c:when>
		            <c:otherwise>
                  <c:forEach var="c" items="${suc }" varStatus="vs">
                    <div>${c}</div>
                  </c:forEach>
		            </c:otherwise>
		          </c:choose>              
              </div>
            </div> 
            <div class="write border">
              <div class="title">합격자발표 비고</div>
              <div class="input_box">${s.SUC_APY_NOTE}</div>
            </div> 
            <div class="write border">
              <div class="title">서류제출</div>
              <div class="input_box">${doc[0]} ~ ${doc[1] }</div>
            </div> 
            <div class="write border">
              <div class="title">서류제출 비고</div>
              <div class="input_box">${s.DOC_SUB_NOTE}</div>
            </div> 
            <div class="write border">
              <div class="title">면접일정</div>
              <div class="input_box">
		          <c:choose>
		            <c:when test="${size2 eq 2 }">
                <c:forEach var="t" items="${intv }" varStatus="vs">
                  <div><strong>${vs.index +1}차 면접 </strong>: ${t}</div>
                </c:forEach>
		            </c:when>
		            <c:otherwise>
                <c:forEach var="t" items="${intv }" varStatus="vs">
                  <div>${t}</div>
                </c:forEach>
		            </c:otherwise>
		          </c:choose>             
              </div>
            </div> 
            <div class="write border">
              <div class="title">면접일정 비고</div>
              <div class="input_box">${s.INTV_NOTE}</div>
            </div>                      
          </div>  
          <!-- 목록버튼 -->  
          <div class="btn_box">
            <a class="base_btn" href="/admin/univ/univ_info_update?UNIV_NUM=${s.UNIV_NUM }">수정</a>      
            <a class="gray_btn"  href="/admin/univ/ad_univ_info">목록</a>  
          </div>
        </div>
       </c:when>
       <c:otherwise>
        <div class="content">${error }</div>
       </c:otherwise>
    </c:choose>
    </div>
  </div>
</section>
</body>
</html>