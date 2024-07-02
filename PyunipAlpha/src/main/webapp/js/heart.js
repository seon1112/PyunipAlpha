/**
 * 좋아요 추가 및 삭제
 */
$(document).on("click",".like_icon",function(){
  var USER_NUM=$("#USER_NUM").val();
  if(USER_NUM =='' || USER_NUM == null){
    alert("로그인이 필요한 서비스입니다.");
    return;  
  }
    
  if($(".like_icon").hasClass("full")){
    $(".like_icon").removeClass("full");
    
    $.ajax({
        url:'/user/boardLike',
        type:'delete',
        data:{
          BRD_NUM:$("#BRD_NUM").val()
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
		        var size = parseInt($("#like_cnt").text());
		        size = size - 1;
		        $("#like_cnt").text(size);            
			}
        },
        error:function(html){
            alert("error:"+html);
        }
        ,timeout:3000        
      });
  }else{
    $(".like_icon").addClass("full");
    
    $.ajax({
     url:'/user/boardLike',
     type:'post',
     data:{
       BRD_NUM:$("#BRD_NUM").val()
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
	         var size = parseInt($("#like_cnt").text());
	         size = size + 1;
	         $("#like_cnt").text(size);
		}
     },
     error:function(html){
       alert("error:"+html);
     }
     ,timeout:3000   
    });
  }
});
