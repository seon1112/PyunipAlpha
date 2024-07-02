/**
 * 댓글 로드
 */
function loadRpy(){
  $.ajax({
    url:'/board/rpy_form',
    type:'get',
    data: {
      BRD_NUM:$("#BRD_NUM").val()
      },
    async  : false,
    success:function(html){
      $(".rpy_list").html(html);
      $(".rpy_count span").html($("#size").val());
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
  })
}

/**
 * 댓글 메뉴
 */
function openMenu(obj){
  var menu=obj.next();
  if(!menu.hasClass("open_menu")){
    menu.addClass("open_menu");
    
    $(document).one("mouseup", function(){
      menu.removeClass("open_menu");
    });     
  }
}

/**
 * 댓글 textarea
 */
function openRpy(obj){
  var form=obj.parents(".rpy_form").next(".rpy_rpy_form");
  form.toggleClass("open_textarea");
  form.siblings().removeClass("open_textarea");
}

/**
 * 대댓글 쓰기
 */
$(document).on("keypress","[id*=re_rpy_content]",function(e){
	if(e.which == 13){
		var RPY_NUM=$(this).next().find("#insert_re_rpy").data('rpynum');
		insertReRpy(RPY_NUM);
	}	
})

function insertReRpy(RPY_NUM){
  var USER_NUM=$("#USER_NUM").val();
  if(USER_NUM =='' || USER_NUM == null){
    alert("로그인이 필요한 서비스입니다.");
    return;  
  }
    
  var content=$("#re_rpy_content"+RPY_NUM).val().trim();
  if(content == ''){
	alert("내용을 입력 해주세요.");
	$("#re_rpy_content"+RPY_NUM).focus();
	return;
  }
  
  $("#re_rpy_content"+RPY_NUM).val('');
  $.ajax({
    url:'/user/reRpy',
    type:'post',
    data:{
      content:content
      ,RPY_NUM:RPY_NUM
      ,BRD_NUM:$("#BRD_NUM").val() 
    },
    cache : false,
    async  : false,
    beforeSend : function(xhr) {
        xhr.setRequestHeader(header, token);
    },   
    success:function(response){
        if (response.success) {
            loadRpy();
        } else {
            alert(response.message);
        }
    },
    error : function(xhr, error) {
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
 * 댓글 쓰기
 */
$(document).on("keypress","#rpy_content",function(e){
	if(e.which == 13){
		insertRpy();
	}	
});

function insertRpy(){
  var USER_NUM=$("#USER_NUM").val();
  if(USER_NUM =='' || USER_NUM == null){
    alert("로그인이 필요한 서비스입니다.");
    return;  
  }
  
  var content=$("#rpy_content").val().trim();
  $("#rpy_content").val("");
  
  if(content == ''){
	alert("내용을 입력 해주세요.");
	$("#rpy_content").focus();
	return;
  }  
  
  $.ajax({
    url:'/user/rpy',
    type:'post',
    data:{
      content:content
     ,BRD_NUM:$("#BRD_NUM").val() 
    },
    cache : false,
    async  : false,
    beforeSend : function(xhr) {
        xhr.setRequestHeader(header, token);
    },    
    success:function(response){
        if (response.success) {
            loadRpy();
        } else {
            alert(response.message);
        }
    },
    error : function(xhr, error) {
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
  })
}

/**
 * 댓글 삭제
 */
function deleteRpy(RPY_NUM){
  if(!confirm("댓글을 삭제하시겠습니까?")){
	  return;
  }
  	
  $.ajax({
    url:'/user/rpy',
    type:'delete',
    data:{
      RPY_NUM:RPY_NUM
    },
    cache : false,
    async  : false,  
    beforeSend : function(xhr) {
        xhr.setRequestHeader(header, token);
    },    
    success:function(msg){
      if(msg != ""){
       alert(msg);
      }else{
        loadRpy();
      }      
    },
    error : function(xhr, error) {
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
 * 대댓글 삭제
 */
function deleteReRpy(RPY_NUM, RE_RPY_NUM){
  if(!confirm("댓글을 삭제하시겠습니까?")){
	  return;
  }	
  
  $.ajax({
      url:'/user/reRpy',
      type:'delete',
      data:{
        RPY_NUM:RPY_NUM
        ,RE_RPY_NUM:RE_RPY_NUM
      },
      cache : false,
      async  : false,
      beforeSend : function(xhr) {
          xhr.setRequestHeader(header, token);
      },      
      success:function(msg){
        if(msg != ""){
         alert(msg);
        }else{
          loadRpy();
        }      
      },
     error : function(xhr, error) {
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

