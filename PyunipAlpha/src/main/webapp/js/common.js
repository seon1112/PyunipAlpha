/**
 * 닉네임 글자 수 제한
 */
function checkId(id){
	var param={};
	//영문,숫자사용 4~10자
	var idExp= /^[A-Za-z0-9]{4,10}$/;
	
	if(!idExp.test(id)){
		param={"result" : false};
		return param;
	}
	
	param = {"result" : true};
	return param;
}

/**
 * 비밀번호 유효성 체크
 * 맞으면 true
 * 틀리면 false
 */
function checkPwd(nm) {
  var param = {};
  //1.숫자, 영대/소문자, 특수문자 혼합 
  ///^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[~,!,@,#,$,*,(,),=,+,_,.,|]).*$/;
  var pwdExp =  /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*\W).*$/; 
  // 영문, 숫자, 특수문자 조합
  var pwdRuleExp = /(?=.*\d)(?=.*[a-zA-Z])(?=.*\W).*$/;

  if(!pwdExp.test(nm)) {
    param = {"result" : false};
    return param;
  }
  
  if(!pwdRuleExp.test(nm)) { 
    param = {"result" : false};
    return param;
  }
  
  param = {"result" : true};     
  return param;
}

/**
 * 비밀번호 더블 체크
 * 맞으면 true
 * 틀리면 false
 */
function comparePwd(nm, nm2) {
  if(nm != nm2) {
    param = {"result" : false};
    return param;
  }
  else {
    param = {"result" : true};     
    return param;
  }
}

// myPage_form 비밀번호 함수 따로 작성되어 있음. 비밀번호 정규식 변경 시 함께 변경 필요