<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>최신 모집 요강</title>
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<link rel="stylesheet" href="/css/main.css" type="text/css">
<link rel="stylesheet" href="/css/univ.css" type="text/css">
<script>
function pageMove(no,ctg){
  var url="";
  if(ctg == '0'){
    url="/board/talk_detail?BRD_NUM="+no;
  }else if(ctg == '1'){
    url="/board/study_detail?BRD_NUM="+no;
  }else if(ctg =='3'){
    url = '/univ/univ_apply_detail?UNIV_NUM=' + no;
  }
  window.location.href=url;
}
</script>
</head>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp">
  <jsp:param value="on" name="on_1"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!-- 메인 영역 -->
<jsp:include page="/WEB-INF/views/inc/title_box.jsp">
  <jsp:param value="banner_selected" name="menu_8"/>
</jsp:include>
<div class="content">
  <div class="univ_mobile">
    <div class="univ_list">
      <a href="javascript:pageMove('1','3')">가천대</a>
      <a href="javascript:pageMove('2','3')">가톨릭대</a>
      <a href="javascript:pageMove('3','3')">건국대</a>
      <div class="univ_sub_ul">
        <div>경기대</div>
        <a class="univ_area"  href="javascript:pageMove('4','3')">서울</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('5','3')">수원</a>
      </div>
      <div class="univ_sub_ul">
        <div>경희대</div>
        <a class="univ_area" href="javascript:pageMove('6','3')">서울</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('7','3')">국제</a>
      </div>
      <div class="univ_sub_ul">
        <div>고려대</div>
        <a class="univ_area" href="javascript:pageMove('8','3')">안암</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('9','3')">세종</a>
      </div>
      <a href="javascript:pageMove('10','3')">광운대</a>
      <a href="javascript:pageMove('11','3')">국민대</a>
    </div>
    <div class="midle_line"></div>
    <div class="univ_list">
      <div class="univ_sub_ul">
        <div>단국대</div>
        <a class="univ_area" href="javascript:pageMove('12','3')">죽전</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('13','3')">천안</a>
      </div>
      <a href="javascript:pageMove('14','3')">덕성여대</a>
      <a href="javascript:pageMove('15','3')">동국대</a>
      <a href="javascript:pageMove('16','3')">동덕여대</a>
      <div class="univ_sub_ul">
        <div>명지대</div>
        <a class="univ_area" href="javascript:pageMove('17','3')">인문</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('18','3')">자연</a>
      </div>
      <div class="univ_sub_ul">
        <div>상명대</div>
        <a class="univ_area" href="javascript:pageMove('19','3')">서울</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('20','3')">천안</a>
      </div>
      <a href="javascript:pageMove('21','3')">서강대</a>
      <a href="javascript:pageMove('22','3')">서울과기대</a>
    </div>
    <div class="midle_line"></div>
    <div class="univ_list">
      <a href="javascript:pageMove('23','3')">서울시립대</a>
      <a href="javascript:pageMove('24','3')">서울여대</a>
      <div class="univ_sub_ul">
        <div>성균관대</div>
        <a class="univ_area" href="javascript:pageMove('25','3')">인문</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('26','3')">자연</a>
      </div>
      <a href="javascript:pageMove('27','3')">성신여대</a>
      <a href="javascript:pageMove('28','3')">세종대</a>
      <a href="javascript:pageMove('29','3')">숙명여대</a>
      <a href="javascript:pageMove('30','3')">숭실대</a>
      <a href="javascript:pageMove('31','3')">아주대</a>
    </div>
    <div class="midle_line"></div>
    <div class="univ_list">
      <div class="univ_sub_ul">
        <div>연세대</div>
        <a class="univ_area" href="javascript:pageMove('32','3')">서울</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('33','3')">미래</a>
      </div>
      <a href="javascript:pageMove('34','3')">이화여대</a>
      <a href="javascript:pageMove('35','3')">인하대</a>
      <div class="univ_sub_ul">
        <div>중앙대</div>
        <a class="univ_area" href="javascript:pageMove('36','3')">서울</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('37','3')">다빈치</a>
      </div>
      <div class="univ_sub_ul">
        <div>한국외대</div>
        <a class="univ_area" href="javascript:pageMove('38','3')">서울</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('39','3')">글로벌</a>
      </div>
      <a href="javascript:pageMove('40','3')">한국항공대</a>
      <div class="univ_sub_ul">
        <div>한양대</div>
        <a class="univ_area" href="javascript:pageMove('41','3')">서울</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('42','3')">ERICA</a>
      </div>
      <div class="univ_sub_ul">
        <div>홍익대</div>
        <a class="univ_area" href="javascript:pageMove('43','3')">서울</a>
        <div class="li_midle_line"></div>
        <a class="univ_area" href="javascript:pageMove('44','3')">세종</a>
      </div>
    </div>
  </div>
</div>
<!--하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>