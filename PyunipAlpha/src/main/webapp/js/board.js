/**
 * 리스트 노드
 */
function searchList(nSelect){
  var url='';
  var ctg = $("#BRD_CTG").val();
  
  if(ctg=='2'){
    url="/board/apply_list";
  }else if(ctg=='3'){
    url="/board/univ_news_list"
  }
  
  $.ajax({
    url:url
    ,type:'get'
    ,data:{
      BRD_CTG  : ctg,
      nSelect  : nSelect   ,  
      SCH      : $(".search_input").val().trim()
    }
    ,async  : false
    ,success:function(html){
      $(".content_list").html(html);
      var size=$("#nMaxRecordCnt").val();
      $("#total_cnt > span").html(size);
      /*paging*/
      var nMaxRecordCnt=$("#nMaxRecordCnt").val();
      var nMaxVCnt=$("#nMaxVCnt").val();
      paging(nSelect,nMaxRecordCnt,nMaxVCnt);      
      
      // 상위 목록 focus 및 스크롤 이동
      var firstItem = $(".header_nav");
      firstItem.focus();
      if (firstItem.length) {
          firstItem[0].scrollIntoView({ behavior: 'auto', block: 'start' });
      }          
    }
    ,error : function(xhr, error) {
      var msg = '';
      if (xhr.status == 404) {
      	  msg = '[404] Requested page not found';
      } else if (xhr.status == 500) {
          msg = '[500] Internal Server Error';
      } else if (error=== 'parsererror') {
          msg = 'Requested JSON parse failed';
      }  else if (error=== 'timeout') {
          msg = 'Time out error';
      } else if (exception === 'abort') {
          msg = 'Ajax request aborted';
      } else {
          msg = xhr.responseText;
      }
      alert(msg);
      console.log(xhr);
    }    
  });
}

function searchList2(nSelect){
  var url='';
  var ctg = $("#BRD_CTG").val();
  var mine= "N"; //내 게시물
  var heart="N"; //관심
 
  if($("#my_item").hasClass("selected")){
    mine ="Y";
  }
  
  if($("#my_heart").hasClass("selected")){
	heart="Y";  
  }  
  
  if(ctg == '4'){
    url="/board/success_story_list";
  }else if(ctg == '5'){
	url="/board/proof_shot_list"
  }
  
  $.ajax({
    url:url
    ,type:'get'
    ,data:{
      BRD_CTG  : ctg,
      nSelect  : nSelect,   
      SCH      : $(".search_input").val().trim(),
      MINE     : mine,
      HEART    : heart
    }
    ,async  : false
    ,success:function(html){
      $(".study_list").html(html);
      var size=$("#nMaxRecordCnt").val();
      $("#total_cnt > span").html(size);
      /*paging*/
      var nMaxRecordCnt=$("#nMaxRecordCnt").val();
      var nMaxVCnt=$("#nMaxVCnt").val();
      paging(nSelect,nMaxRecordCnt,nMaxVCnt);      
      
      // 상위 목록 focus 및 스크롤 이동
      var firstItem = $(".header_nav");
      firstItem.focus();
      if (firstItem.length) {
          firstItem[0].scrollIntoView({ behavior: 'auto', block: 'start' });
      }          
    }
    ,error : function(xhr, error) {
      var msg = '';
      if (xhr.status == 404) {
      	  msg = '[404] Requested page not found';
      } else if (xhr.status == 500) {
          msg = '[500] Internal Server Error';
      } else if (error=== 'parsererror') {
          msg = 'Requested JSON parse failed';
      }  else if (error=== 'timeout') {
          msg = 'Time out error';
      } else if (exception === 'abort') {
          msg = 'Ajax request aborted';
      } else {
          msg = xhr.responseText;
      }
      alert(msg);
      console.log(xhr);
    }    
  });  
}

/**
 * 상세 페이지 이동
 */
function goDetail(BRD_NUM){
   var ctg = $("#BRD_CTG").val();
   var li=$(".paging").find("li.select_on");
 	
   if($(li).data("page") != ''){
		nSelect=$(li).data("page");
   }else{
		nSelect="1";
   }   
   
   if(ctg == '2'){
     location.href="/board/apply_detail?BRD_NUM="+BRD_NUM+"&nSelect="+nSelect;
   }else if(ctg == '3'){
     location.href="/board/univ_news_detail?BRD_NUM="+BRD_NUM+"&nSelect="+nSelect;
   }else if(ctg == '4'){
     location.href="/board/success_story_detail?BRD_NUM="+BRD_NUM+"&nSelect="+nSelect;
   }else if(ctg == '5'){
     location.href="/board/proof_shot_detail?BRD_NUM="+BRD_NUM+"&nSelect="+nSelect;
   }
}


/**
 * 페이징
 */
function paging(nSelect,nMaxRecordCnt,nMaxVCnt){
  $.ajax({
    url:'/paging'
    ,data:{
      nSelect       : nSelect,
      nMaxRecordCnt : nMaxRecordCnt,
      nMaxVCnt      : nMaxVCnt
    }
    ,async  : false
    ,success:function(html){
      $(".paging").html(html);
    }
  });
}

/**
 * 페이지 이동
 */
$(document).on("click", ".paging_ul li", function(event) {
    var nSelect;
    var li = $(event.target).closest("li");
    $(li).addClass("select_on");
    $(li).siblings("li").removeClass("select_on");
    if($(li).data("page")!=''){
        nSelect=$(li).data("page");     
    }
    if (nSelect.toString().indexOf(".") !== -1) {
        nSelect = nSelect.toString().substring(0, nSelect.toString().indexOf("."));
    }
    searchList(nSelect);
});

var inputFileList = new Array();
/**
 * 이미지 파일 추가
 */
function insertImage(input) {
  if (!input.files || input.files.length === 0) {
      alert("파일이 선택되지 않았습니다");
      return;
  }
  if ($("#img_file").val() == '') return;
  
  var uploadFile = input.files;
  var filesArr = Array.prototype.slice.call(uploadFile);
  filesArr.forEach(function (f) {
      inputFileList.push(f);
  });

  updateFileList();
}

/**
 * 파일 리스트 추가
 */
function updateFileList() {
  $(".img_box").empty();

  inputFileList.forEach(function (file, index) {
      var img_div = $("<div></div>").addClass("write_r_img");
      $(img_div).append($("<div></div>").html(file.name));
      $(img_div).append($("<div></div>").addClass("delete_btn").addClass("cursor"));
      $(".img_box").append(img_div);
  });
}

/**
 * 파일 삭제
 */
$(document).on("click", ".delete_btn", function () {
    var parentDiv = $(this).parent(".write_r_img");
    var fileName = parentDiv.find("div:first-child").text();

    inputFileList = inputFileList.filter(function (file) {
        return file.name !== fileName;
    });
    parentDiv.remove();
});


/**
 * 게시물 추가
 */
function insertBoard(){
  if($("#title").val()==''){
    alert("제목을 입력해주세요");
    return;
  }
  
  var formData=new FormData($("#boardForm")[0]);
  
  if(inputFileList.length >0 ){
    for (var i = 0; i < inputFileList.length; i++) {
      formData.append("images", inputFileList[i]);
    }
  }
  
  $.ajax({
    type : 'post'
    ,enctype : "multipart/form-data"
    ,url:'/admin/insertBoard'
    ,data : formData
    ,processData : false
    ,contentType : false
    ,success : function(msg){
      if(msg != ""){
        alert(msg);
      }else{
        location.reload();
        alert("성공적으로 등록되었습니다.");        
      }
    }
    ,error : function(xhr, error) {
      var msg = '';
      if (xhr.status == 404) {
      	  msg = '[404] Requested page not found';
      } else if (xhr.status == 500) {
          msg = '[500] Internal Server Error';
      } else if (error=== 'parsererror') {
          msg = 'Requested JSON parse failed';
      }  else if (error=== 'timeout') {
          msg = 'Time out error';
      } else if (exception === 'abort') {
          msg = 'Ajax request aborted';
      } else {
          msg = xhr.responseText;
      }
      alert(msg);
      console.log(xhr);
    }
  }); 
}

/**
 * 목록이동
 */
function goList(nSelect){
   var ctg = $("#BRD_CTG").val();
   
   if(ctg == '2'){
     location.href='/board/apply_form?nSelect='+nSelect;
   }else if(ctg == '3'){
     location.href='/board/univ_news_form?nSelect='+nSelect;
   }else if(ctg == '4'){
     location.href='/board/success_story_form?nSelect='+nSelect;
   }else if(ctg == '5'){
     location.href='/board/proof_shot_form?nSelect='+nSelect;
   }
}
