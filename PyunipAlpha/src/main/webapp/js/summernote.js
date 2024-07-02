/** 
 * summernote load
*/
function loadSummernote(){
  $('#summernote').summernote({
     toolbar: [
         ['fontname', ['fontname']],
         ['fontsize', ['fontsize']],
         ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
         ['color', ['forecolor','color']],
         ['table', ['table']],
         ['para', ['ul', 'ol', 'paragraph']],
         ['height', ['height']],
         ['insert',['picture']],
       ],
     height: 420,                 // 에디터 높이
     minHeight: 300,             // 최소 높이
     maxHeight: null,             // 최대 높이
     focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
     lang: "ko-KR",          // 한글 설정
     placeholder: '최대 2048자까지 쓸 수 있습니다',
     callbacks: {  //여기 부분이 이미지를 첨부하는 부분
       onImageUpload : function(files) {
         uploadSummernoteImageFile(files[0],this);
       },
       onPaste: function (e) {
         var clipboardData = e.originalEvent.clipboardData;
         if (clipboardData && clipboardData.items && clipboardData.items.length) {
           var item = clipboardData.items[0];
           if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
             e.preventDefault();
           }
         }
       }
     }
   });  
}

var savedFileName="";
/**
 *파일 업로드 
 */ 
function uploadSummernoteImageFile(file, editor) {
  data = new FormData();
  data.append("file", file);
  $.ajax({
    data : data,
    type : "POST",
    url : "/uploadSummernoteImageFile",
    enctype: 'multipart/form-data',
    contentType : false,
    processData : false,
    cache : false,
    success : function(response) {
      // JSON 문자열을 JSON 객체로 파싱
      var jsonResponse = JSON.parse(response);
      var url=jsonResponse.url;
      savedFileName=savedFileName+jsonResponse.savedFileName+",";
      // url에 접근하여 이미지 삽입
      $(editor).summernote('insertImage', jsonResponse.url);
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

$("div.note-editable").on('drop',function(e){
   for(i=0; i< e.originalEvent.dataTransfer.files.length; i++){
    uploadSummernoteImageFile(e.originalEvent.dataTransfer.files[i],$("#summernote")[0]);
   }
   e.preventDefault();
}); 