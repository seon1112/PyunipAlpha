/**
 * 리스트 노드
 */
function searchList(ctg,nSelect){
  var url='';
  if(ctg=='2'){
    url="/admin/board/apply_list";
  }else if(ctg=='3'){
    url="/admin/board/univ_news_list"
  }else if(ctg == '0'){
	url ="/admin/board/talk_list"
  }else if(ctg =='1'){
	url ="/admin/board/study_list"
  }else if(ctg == '4'){
	url="/admin/board/success_story_list"
  }else if(ctg =='5'){
	url="/admin/board/proof_shot_list"
  }
  
  if(ctg == '0'){
	  $.ajax({
	    url:url
	    ,type:'get'
	    ,data:{
	      BRD_CTG  :ctg 
	      ,nSelect :nSelect     
	      ,SCH     :$(".search_input2").val().trim()
	      ,DV      :$("#dv>option:selected").val()
	    }
	    ,success:function(html){
	      $(".content_list").html(html);
	      var size=$("#nMaxRecordCnt").val();
	      $(".total_cnt > span").html(size);
	      /*paging*/
	      var nMaxRecordCnt=$("#nMaxRecordCnt").val();
	      var nMaxVCnt=$("#nMaxVCnt").val();
	      paging(nSelect,nMaxRecordCnt,nMaxVCnt);      
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
          console.log(error);
        }	    

   });
  }else if(ctg =="1"){
	  $.ajax({
	    url:url
	    ,type:'get'
	    ,data:{
	      BRD_CTG  : ctg 
	      ,nSelect : nSelect     
	      ,SCH     : $(".search_input2").val().trim()
	      ,ING     : $("#dv>option:selected").val()
	    }
	    ,success:function(html){
	      $(".content_list").html(html);
	      var size=$("#nMaxRecordCnt").val();
	      $(".total_cnt > span").html(size);
	      /*paging*/
	      var nMaxRecordCnt=$("#nMaxRecordCnt").val();
	      var nMaxVCnt=$("#nMaxVCnt").val();
	      paging(nSelect,nMaxRecordCnt,nMaxVCnt);      
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
          console.log(error);
        }	    
	  });
  }else{
	  $.ajax({
	    url:url
	    ,type:'get'
	    ,data:{
	      BRD_CTG  : ctg 
	      ,nSelect : nSelect     
	      ,SCH     : $(".search_input2").val().trim()
	    }
	    ,success:function(html){
	      $(".content_list").html(html);
	      var size=$("#nMaxRecordCnt").val();
	      $(".total_cnt > span").html(size);
	      /*paging*/
	      var nMaxRecordCnt=$("#nMaxRecordCnt").val();
	      var nMaxVCnt=$("#nMaxVCnt").val();
	      paging(nSelect,nMaxRecordCnt,nMaxVCnt);      
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
          console.log(error);
        }	    
	  });	
  }
}


/**
 * 상세 페이지 이동
 */
function goDetail(BRD_NUM){
   var ctg=$("#ctg").val();
   
   if(ctg == '2'){
     location.href="/admin/board/apply_detail?BRD_NUM="+BRD_NUM;
   }else if(ctg == '3'){
     location.href="/admin/board/univ_news_detail?BRD_NUM="+BRD_NUM;
   }else if(ctg == '4'){
     location.href="/admin/board/success_story_detail?BRD_NUM="+BRD_NUM;
   }else if(ctg =='0'){
	 location.href="/admin/board/talk_detail?BRD_NUM="+BRD_NUM;
   }else if(ctg =='1'){
	 location.href="/admin/board/study_detail?BRD_NUM="+BRD_NUM;
   }else if(ctg =='5'){
	 location.href="/admin/board/proof_shot_detail?BRD_NUM="+BRD_NUM;
   }
}


/**
 * 페이징
 */
function paging(nSelect,nMaxRecordCnt,nMaxVCnt){
  $.ajax({
    url:'/paging'
    ,data:{
      nSelect:nSelect
      ,nMaxRecordCnt:nMaxRecordCnt
      ,nMaxVCnt:nMaxVCnt
    },
    success:function(html){
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
    var ctg=$("#ctg").val();
    searchList(ctg,nSelect);
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

  var filenames=$("#filenames").val();
  filenames = filenames + savedFileName;
  $("#filenames").val(filenames);
     
  var formData=new FormData($("#boardForm")[0]);
  
  if(inputFileList.length >0 ){
    for (var i = 0; i < inputFileList.length; i++) {
      formData.append("images", inputFileList[i]);
    }
  }
  
  $.ajax({
    type : 'post'
    ,enctype : "multipart/form-data"
    ,url:'/admin/board'
    ,data : formData
    ,processData : false
    ,contentType : false
    ,success : function(msg){
      if(msg != ""){
        alert(msg);
      }else{
        alert("성공적으로 등록되었습니다.");        
        goList();
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
      console.log(error);
    }
  }); 
}

/**
 * 목록이동
 */
function goList(){
   var ctg=$("#BRD_CTG").val();
   
   if(ctg == '2'){
     location.href='/admin/board/ad_apply';
   }else if(ctg == '3'){
     location.href='/admin/board/ad_univ_news';
   }else if(ctg == '4'){
     location.href='/admin/board/ad_success_story';
   }else if(ctg =='5'){
	 location.href='/admin/board/ad_proof_shot';
   }
}

