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
    searchList(nSelect);
});

