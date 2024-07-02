<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/header_meta.jsp"></jsp:include>
<title>편입알파</title>
<jsp:include page="/WEB-INF/jsp/import.jsp"></jsp:include>
<link rel="stylesheet" href="/css/main.css" type="text/css">
<style type="text/css">
.fc-event-title-container {
  font-size: 15px;
  color:var(--Gray-90);
}
.fc-daygrid-block-event .fc-event-time, .fc-daygrid-block-event .fc-event-title{
  padding:2.5px;
}
.fc .fc-button-primary{
  background-color: var(--Main-40);
  border-color: var(--Main-40);
  color:var(--White);
}
.fc .fc-button-primary:hover{
  background-color: var(--Main-60);
  border-color: var(--Main-60);
}
.fc-theme-standard td, .fc-theme-standard th{
  background-color: var(--White);
}

.fc .fc-col-header-cell-cushion{
  padding:15px 4px;
  font-size: 16px;
  font-weight: 500;
}

.main_box .main_list .list_item .item_content > p {
  line-height: 17px !important;
}

.main_box .main_list .list_item .item_content > p > span{
  font-family: "Pretendard" !important;	
  font-size: 14px !important;
  font-weight: 400 !important;
  color: var(--Gray-60) !important;
  line-height: 17px !important;	
}
.main_box .main_list .list_item .item_content > p > img{
	display: none !important;
}
</style>
</head>
<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js'></script>
<script>
document.addEventListener('DOMContentLoaded', function() {
   var calendarEl = document.getElementById('calendar');
   var calendar = new FullCalendar.Calendar(calendarEl, {
     initialView: 'dayGridMonth',
     selectable: true,
     dayMaxEvents: true, 
     events: '/calendarEvent', // event data
     eventClick: function(info) {
       var UNIV_NUM = info.event.extendedProps.no; //no부여
       goPage(UNIV_NUM); //해당 event로 이동
     }
   });
   calendar.render();
});

$(function(){
  $(".univ_ul li").click(function(event){
    var no=$(event.target).closest("li").data("no");
    if(no != '' && no != null){
      goPage(no);
    }
  });
  
  $("#main_search").keypress(function(e){
    if(e.which==13){
      var MAJOR_NM=$("#main_search").val().trim();
      location.href="/univ/univ_major?MAJOR_NM="+MAJOR_NM;
    }
  });
  
  $("#move_univ").click(function(){
  	var univ_num = $(".dropdown2 .optionList > li.selected").data("value");
 	if(univ_num !== undefined && univ_num !== null && univ_num !== ''){
	  window.location.href='/univ/univ_apply_detail?UNIV_NUM=' + univ_num;
  	}else{
  		alert("대학을 선택해주세요");
  	}
  });
});

//메인 베너
var bIndex=0;
var slides=document.getElementsByClassName("banner_slide");

startCarousel();

function startCarousel(){
  carousel();
}

function carousel(){
  for(var i=0;i<slides.length;i++){
    if(slides[i]){
      slides[i].style.display="none";
    }
  }
  
  bIndex++;
  if(bIndex > slides.length){
    bIndex=1;
  }
  
  if(slides[bIndex-1]){
    slides[bIndex-1].style.display="block";
  }
  
  $(".ban_st").html(bIndex);
  setTimeout(carousel,3000);
};
	
function prevSlide(){
  bIndex--;
  if(bIndex < 1){
    bIndex=slides.length;
  }
  
  $(".ban_st").html(bIndex);
  showSlide(bIndex);
}

function nextSlide(){
  bIndex++;
  if(bIndex > slides.length){
    bIndex=1;
  }
  
  $(".ban_st").html(bIndex);
  showSlide(bIndex);
}

function showSlide(bIndex) {
  for (var i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
  }
  
  slides[bIndex - 1].style.display = "block";
}

function pageMove(no,ctg){
	var url="";
	if(ctg == '0'){
		url="/board/talk_detail?BRD_NUM="+no;
	}else if(ctg == '1'){
		url="/board/study_detail?BRD_NUM="+no;
	}else if(ctg =='3'){
		url = '/univ/univ_apply_detail?UNIV_NUM=' + no;
	}
	document.location.href=url;
}

function notOpen(){
  alert("오픈 준비 중 입니다.");
}

window.onclick = function(event) {
  if (!event.target.matches('.dropdown2') && !event.target.matches('.dropdown2 > label')) {
	  $(".dropdown2").removeClass("active");
  }
}

$(document).on("click",".dropdown2",function(){
	if($(this).hasClass("active")){
		$(this).removeClass("active");
	}else{
		$(this).addClass("active");
	}	
});

$(document).on("click", ".dropdown2 > label", function(event){
    event.stopPropagation(); //상위로 이벤트 전파x
    $(this).parent().toggleClass("active");
});

$(document).on("click",".optionItem",function(){
	var selectOption = $(this).text();
	$(this).siblings().removeClass("selected");
	$(this).addClass("selected");
	$(this).closest(".dropdown2").find(".label").text(selectOption);
	$(this).closest(".dropdown2").removeClass("active");	
});
</script>
<body>
<!--상단 헤더 -->
<jsp:include page="/WEB-INF/views/inc/header.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/inc/mobile_menu.jsp"></jsp:include>
<!--메인 영역 -->
<div class="content_main">
  <div class="main_banner_section mobile_hidden">
    <div class="banner_slide" style="display:block; background-color:#F7EAC8; background-image: url('../image/banner/main/main_banner3.png');"><div class="banner_box"></div></div>
    <div class="banner_slide" style="background-color:#F7D1C8;background-image: url('../image/banner/main/main_banner2.png');"><div class="banner_box"></div></div>
    <div class="banner_btn">
      <button type="button" class="left_btn" onclick="prevSlide()"></button>
      <div class="ban_st">1</div>
      <div class="mid_btn_line"></div>
      <div>2</div>
      <button type="button" class="right_btn" onclick="nextSlide()"></button>
    </div>
  </div>
  <div class="main_section">
    <div class="sch_box">
      <label for="main_search" class="hidden_label">학과 검색</label>
      <input class="main_sch_input" placeholder="학과를 검색해주세요" id="main_search">
    </div>
    <div class="icon_box">
      <a class="icon_item" href='/univ/univ_apply'>
        <div class="icon_image" style="background-image:url('/image/icon/apply3.png')"></div>
        <span class="icon_name">모집 요강</span>
      </a>
      <a class="icon_item" href='/board/univ_news_form'>
        <div class="icon_image" style="background-image:url('/image/icon/news3.png')"></div>
        <span class="icon_name">편입 뉴스</span>
      </a>
      <a class="icon_item" href='/board/talk_form'>
        <div class="icon_image" style="background-image:url('/image/icon/board3.png')"></div>
        <span class="icon_name">편입 이야기</span>
      </a>
      <a class="icon_item" href='/board/study_form'>
        <div class="icon_image" style="background-image:url('/image/icon/study3.png')"></div>
        <span class="icon_name">편입 스터디</span>
      </a>
      <a class="icon_item" href="/board/success_story_form">
        <div class="icon_image" style="background-image:url('/image/icon/success3.png')"></div>
        <span class="icon_name">합격 수기</span>
      </a>
      <a class="icon_item" href='/board/proof_shot_form'>
        <div class="icon_image" style="background-image:url('/image/icon/camera.png')"></div>
        <span class="icon_name">합격 인증샷</span>
      </a>
    </div>

    <div class="main_box" >
      <div>
        <div class="title_box">
          <div class="main_title">커뮤니티 인기글<img src="/image/icon/fire.png" alt=""></div>
          <a class="more_btn" href='/board/talk_form'>더보기</a>    
        </div>
        <div class="main_sub_title">편입에 대한 공통사로 사람들과 소통해보세요</div>
      </div>
      <div class="main_list">
        <c:forEach var="t" items="${talkList}">
        <a class="list_item" href="javascript:pageMove('${t.BRD_NUM}','0')">
          <c:choose>
            <c:when test="${t.BRD_DV eq 'F'}">
               <div class="badge1">자유주제</div>
            </c:when>
            <c:otherwise>
               <div class="badge2">질문하기</div>
            </c:otherwise>
          </c:choose>
          <div class="item_title" aria-label="제목">${t.TITLE}</div>
          <div class="item_content">${t.CONTENT }</div>
          <div class="item_footer">
            <div class="item_info_wrap">
              <div class="item_info"> 
                <img src="../image/icon/view.png">
                <span class="hidden_label">조회수</span>
                ${t.VIEW_CNT }
              </div>
              <div class="item_info">
                <img src="../image/icon/like_on.png">
                <span class="hidden_label">좋아요 수</span>
                ${t.LIKE_CNT }
              </div>
            </div>
            <div aria-label="작성일" class="reg_dtm_box">${t.REG_DTM}</div>
          </div>
        </a>
        </c:forEach> 
      </div>
    </div>
    <div class="main_box">
      <div>
        <div class="title_box">
          <div class="main_title">능률 2배, 편입 스터디<img src="/image/icon/sparkles.png" alt=""></div>
          <a class="more_btn" href='/board/study_form'>더보기</a>    
        </div>
        <div class="main_sub_title">함께 공부할 동료를 찾아보세요. 함께하면 즐거움이 2배!!</div>
      </div>
      <div class="main_list">
      <c:forEach var="s" items="${studyList}">
        <a class="list_item2" href="javascript:pageMove('${s.BRD_NUM}','1')">
          <div class="title_wrap">
            <div class="item_date" style="display:flex; gap:4px;"><p>마감일</p><strong>${s.APPLY_ED_DTM}</strong></div>
            <c:if test="${s.ED_YN eq 'Y'}">
              <div class="siren_box">마감 임박</div>          
            </c:if>
          </div>
          <div class="item_title">${s.TITLE}</div>
          <div class="item_content">
            <div style="margin-bottom: 5px;" class="item"><p>진행방법 :</p>${s.PCD_WAY }</div>
            <div class="item"><p>시작예정일 :</p>${s.ST_DTM }</div>
          </div>
          <div class="item_footer">
            <div class="item_info_wrap">
              <div class="item_info">
                <span class="hidden_label">조회수</span>
                <img src="../image/icon/view.png" alt="">
                ${s.VIEW_CNT}
              </div>
              <div class="item_info">
                <span class="hidden_label">좋아요수</span>
                <img src="../image/icon/like_on.png" alt="">${s.LIKE_CNT}
              </div>
            </div>
            <div aria-label="작성자" class="reg_dtm_box">${s.USER_NM}</div>
          </div>
        </a>
     </c:forEach> 
      </div>
    </div>
  </div>
</div>
<div class="content_main" style="background-color:var(--Main-5); min-height:0px; margin-bottom:0px;">
  <div class="main_section" style="background-color:var(--Main-5);">
    <div class="main_box univ_box">
      <div>
        <div class="title_box">
          <div class="main_title">실시간 정보! 대학별 편입요강<img src="/image/icon/music.png" alt="" style="margin-left:3px;"></div>
        </div>
        <div class="main_sub_title">원하시는 대학의 편입요강 및 경쟁률을 확인해보세요</div>
      </div>    
      <div class="mobile_view mobile_univ_content">
        <label class="hidden_label" for="univ_drop">대학리스트</label>
      	<div class="dropdown2" id="univ_drop" style="width:100%;">
      		<label class="label">대학리스트</label>
      		<ul class="optionList">
	          <c:forEach var="u" items="${univList }">
      		  <li class="optionItem" data-value="${u.UNIV_NUM }">${u.UNIV_NAME }</li>
	          </c:forEach>      		
      		</ul>
      	</div>      
        <button class="org_btn" id="move_univ">검색</button>
      </div>
      <div class="univ_content mobile_hidden">
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
  </div>
</div>  
<div class="content_main main_calendar back mobile_hidden">
  <div class="main_section back">
    <div class="main_box back">
      <div>
	      <div class="title_box">
	        <div class="main_title">한번에 확인하는 편입 일정<img src="/image/icon/eyes2.png" alt=""></div>
	      </div>    
	      <div class="main_sub_title">대학의 편입 일정을 한 번에 확인해보세요!!</div>
      </div>
      <div id="calendar" class="cal"></div>
    </div>      
  </div>
</div>  
<!-- 하단 footer -->
<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
</body>
</html>