package com.example.demo.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.UserDto;
import com.example.demo.service.LogService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
    PasswordEncoder passwordEncoder;
	@Autowired
    private LogService logService;
	
	/**
	 * 마이페이지 닉네임 중복 체크
	 * @param USER_NM  회원이름
	 */		
	@PostMapping("/user/checkNickname")
	@ResponseBody
	public String checkNickname(@RequestParam String USER_NM) {
		String flag="fales";
		int re=userService.checkUsersNm(USER_NM);
		if(re==0) {
			flag="true";
		}
		return flag;
	}
	
	/**
	 * 회원 탈퇴
	 * @param USER_NUM 회원번호
	 * @throws Exception 
	 */		
	@DeleteMapping("/user/users")
	@ResponseBody
	public String deleteUsers(HttpSession session,HttpServletRequest req) throws Exception {
		String msg="";
		if(session.getAttribute("m") != null) {
			try {
				String USER_NUM=((UserDto) session.getAttribute("m")).getUSER_NUM();
				HashMap<String, Object> map=new HashMap<>();
				map.put("USER_NUM", USER_NUM);
				
				String social=((UserDto) session.getAttribute("m")).getSOCIAL_NM();
				String CLIENT_ID =((UserDto) session.getAttribute("m")).getCLIENT_ID();
				userService.deleteUser(map, CLIENT_ID, social);
				
				//탈퇴로그
				logService.saveLog((UserDto) session.getAttribute("m"),"탈퇴",req);
				
			} catch (Exception e) {
				String error=e.getMessage();
				logService.insertErrorLog(req,session,error);
				msg="로그아웃 에러가 발생했습니다. 관리자에게 문의해주세요";
			}
		}else {
			msg="로그인이 필요한 서비스입니다.";
		}
		
		return msg;
	}
	
	/**
	 * 회원정보 변경
	 * @param USER_NM  회원이름
	 * @throws Exception 
	 */		
	@PostMapping("/user/updateUsers")
	@ResponseBody
	public String updateUsers(@RequestParam String USER_NM,
							  HttpSession session,HttpServletRequest request) throws Exception {
		String msg="";
		if(session.getAttribute("m") != null) {
			try {
				String USER_NUM=((UserDto) session.getAttribute("m")).getUSER_NUM();
				
				//비밀번호 변경
				UserDto dto=new UserDto();
				dto.setUSER_NUM(USER_NUM);
				dto.setUSER_NM(USER_NM);
				
				userService.updateUsers(dto);		
				
			} catch (Exception e) {
				String error=e.getMessage();
				logService.insertErrorLog(request,session,error);
				msg="에러가 발생했습니다. 관리자에게 문의해주세요";
			}
		}else {
			msg="로그인이 필요한 서비스입니다.";
		}

		return msg;
	}	
	
	/**
	 * 마이 페이지
	 * @throws Exception 
	 */		
	@GetMapping("/user/myPage_form")
	public void myPageForm(HttpSession session,HttpServletRequest request,Model model) throws Exception {
		String uri=request.getHeader("Referer");
		if(uri == null) {
			uri = "http://pyunipalpha.shop/main/main_form";
		}
		request.getSession().setAttribute("prevPage", uri);
		
		if(session.getAttribute("m")!=null) {
			try {
				String USER_NUM=((UserDto) session.getAttribute("m")).getUSER_NUM();
				UserDto dto=userService.findUsersByNum(USER_NUM);
				model.addAttribute("dto", dto);
			} catch (Exception e) {
				e.printStackTrace();
				String error=e.getMessage();
				logService.insertErrorLog(request,session,error);
				model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요");
			}

		}else {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
		}
	}
	
	@GetMapping("/user/myPage/goBack")
	public String myPageGoBack(HttpServletRequest request) {
		String prevPage = "redirect:"+(String)request.getSession().getAttribute("prevPage");
		return prevPage;
	}

	
/****************************자체 로그인 구현시 필요했던 코드***************************************************/	
//	/**
//	 * 비밀번호 재설정
//	 */		
//	@GetMapping("/etc/update_pwd")
//	public void UpdatePwdForm() {
//	}	
//	/**
//	 * 회원가입 닉네임 중복 체크
//	 * @param USER_NM  회원이름
//	 */		
//	@GetMapping("/checkNickname")
//	@ResponseBody
//	public String checkNickname2(@RequestParam String USER_NM) {
//		String flag="fales";
//		int re=userService.checkUsersNm(USER_NM);
//		if(re==0) {
//			flag="true";
//		}
//		return flag;
//	}
//		
//	/**
//	 * 회원가입 페이지
//	 */		
//	@GetMapping("/etc/signUp_form")
//	public String SingUpForm() {
//		return "forward:/WEB-INF/views/main/signUp_form.jsp";
//	}	
//	
//	@GetMapping("/tec/login_form")
//	public String LoginForm() {
//		return "forward:/WEB-INF/views/tec/login_form.jsp";
//	}	
//	
//	/**
//	 * 존재하는 이메일인지 확인
//	 * @param email  이메일
//	 */		
//	@PostMapping("/checkEmail")
//	@ResponseBody
//	public Integer checkEmail(String EMAIL){
//		int re=userService.checkEmail(EMAIL);
//		return re;
//	}	
//	
//	
//	/**
//	 * 회원정보 변경
//	 * @param USER_NM  회원이름
//	 * @param PASSWORD 변경할 비밀번호
//	 * @param NOW_PASSWORD 현재 비밀번호
//	 * @param SOCIAL_YN 소셜로그인 여부
//	 */		
//	@PostMapping("/user/updateUsers")
//	@ResponseBody
//	public String updateUsers(@RequestParam String USER_NM,
//							  @RequestParam String PASSWORD,
//							  @RequestParam String NOW_PASSWORD,
//							  @RequestParam String SOCIAL_YN,
//							  HttpSession session,HttpServletRequest request) {
//		String msg="";
//		if(session.getAttribute("m") != null) {
//			try {
//				int USER_NUM=((UsersDto) session.getAttribute("m")).getUSER_NUM();
//				
//				if(SOCIAL_YN.equals("N")) {
//					//현재 비밀번호 일치여부 확인
//					HashMap<String, Object> map=new HashMap<String, Object>();
//					map.put("USER_NUM",USER_NUM);
//					
//					String dbpwd=userService.findPwd(map);
//					if(!passwordEncoder.matches(NOW_PASSWORD, dbpwd)){
//						return msg="비밀번호가 틀렸습니다.";
//					}				
//				}
//				
//				//비밀번호 변경
//				UsersDto dto=new UsersDto();
//				dto.setUSER_NUM(USER_NUM);
//				dto.setUSER_NM(USER_NM);
//				
//				if(SOCIAL_YN.equals("N")) {
//					dto.setPASSWORD(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(PASSWORD));
//				}
//				
//				userService.updateUsers(dto);		
//				
//			} catch (Exception e) {
//				String error=e.getMessage();
//				logService.insertErrorLog(request,session,error);
//				msg="에러가 발생했습니다. 관리자에게 문의해주세요";
//			}
//		}else {
//			msg="로그인이 필요한 서비스입니다.";
//		}
//
//		return msg;
//	}
//	
//	/**
//	 * 비밀번호 힌트 체크
//	 * @param USER_NM  회원이름
//	 * @param HINT  비밀번호 힌트
//	 */		
//	@GetMapping("/checkHint")
//	@ResponseBody
//	public String checHint(@RequestParam String USER_NM,
//						   @RequestParam String HINT,
//						   HttpServletRequest req) {
//		String flag="fales";
//		HashMap<String, String> map=new HashMap<String, String>();
//		map.put("USER_NM", USER_NM);
//		map.put("HINT",HINT.replaceAll("-", ""));
//		
//		int re=userService.checkHint(map);
//		if(re==1) {
//			flag="true";
//		}else {
//			//실패로그 
//			HashMap<String, Object> map2=new HashMap<>();
//			map2.put("ACSS_IP", IpUtils.getIpFromRequest(req));
//			map2.put("USER_NM",USER_NM);        //로그인 시도한 아이디
//			map2.put("ACSS_FAIL_NUM",logService.getNextAcssFailNum());
//			map2.put("FAIL_ROUTE", "PWD HINT:"+HINT);
//			
//			logService.insertAcssFailLog(map2);			
//		}
//		return flag;
//	}	
//	
//	/**
//	 * 비밀번호 초기화 및 변경
//	 * @param USER_NM  회원아이디
//	 * @param PASSWORD 비밀번호
//	 */		
//	@PostMapping("/updateUsers")
//	@ResponseBody
//	public String updateUsers2(@RequestParam String USER_NM, 
//							   @RequestParam String PASSWORD,
//							   @RequestParam String HINT,
//							   HttpSession session, HttpServletRequest request) {
//		String msg="";
//		try {
//			//힌트 유효성 더블 체크
//			HashMap<String, String> map=new HashMap<String, String>();
//			map.put("USER_NM", USER_NM);
//			map.put("HINT",HINT.replaceAll("-", ""));
//			
//			int re=userService.checkHint(map);
//			if(re!=1) {
//				return "변경에 실패했습니다. 정보를 다시 확인해주세요.";
//			}			
//			
//			//비밀번호 유효성 더블 체크
//			boolean result = SignUpUtils.getCheckPwd(PASSWORD);
//			if(result != true) {
//				return "변경에 실패했습니다. 정보를 다시 확인해주세요.";
//			}			
//			
//			UsersDto dto=new UsersDto();
//			dto.setUSER_NM(USER_NM);
//			dto.setPASSWORD(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(PASSWORD));
//			
//			userService.updateUsers2(dto);		
//		} catch (Exception e) {
//			String error=e.getMessage();
//			logService.insertErrorLog(request,session,error);
//			msg="에러가 발생했습니다. 관리자에게 문의해주세요";
//		}
//		return msg;
//	}
//	
//	/**
//	 * 회원 추가
//	 * @param dto  
//	 */		
//	@PostMapping("/insertUsers")
//	@ResponseBody
//	public String insertUsers(@ModelAttribute UsersDto dto,HttpServletRequest req,HttpSession session) {
//		String msg="";
//		String pwd=dto.getPASSWORD();
//		//비밀번호 유효성 더블 체크
//		boolean result = SignUpUtils.getCheckPwd(pwd);
//		if(result != true) {
//			return "회원가입에 실패했습니다. 정보를 다시 확인해주세요";
//		}
//		
//		//아이디 유효성 더블 체크
//		String USER_NM=dto.getUSER_NM();
//		if(USER_NM.length()<4 || USER_NM.length()>10) {
//			return "회원가입에 실패했습니다. 정보를 다시 확인해주세요";
//		}
//		int re2=userService.checkUsersNm(USER_NM);
//		if(re2 == 1) {
//			return "회원가입에 실패했습니다. 정보를 다시 확인해주세요";
//		}
//		
//		dto.setPASSWORD(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(pwd));
//		
//    	int nextNum=userService.getNextUserNum();
//    	dto.setUSER_NUM(nextNum);		
//    	
//    	try {
//    		int re=userService.insertUsers(dto);
//    		
//    		if(re!=1) {
//    			msg="회원가입에 실패했습니다. 관리자에게 문의해주세요";
//    		}else {
//    			UsersDto dto2=new UsersDto();
//    			dto2=userService.findUsersByEmail(USER_NM);
//    			logService.saveLog(dto2,"회원가입",req);
//    		}
//		} catch (Exception e) {
//			String error=e.getMessage();
//			logService.insertErrorLog(req,session,error);
//			msg="오류가 발생했습니다. 관리자에게 문의해주세요";
//		}
//
//		return msg;
//	}	
//	
//	/**
//	 * 입력한 비밀번호가 맞는지 체크
//	 * @param pwd 입력한 비밀번호 
//	 * @return flag
//	 */		
//	@PostMapping("/user/checkPwd")
//	@ResponseBody
//	public boolean checkPwd(@RequestParam String pwd,HttpSession session) {
//		boolean flag=false;
//		
//		if(session.getAttribute("m") != null) {
//			int USER_NUM=((UsersDto) session.getAttribute("m")).getUSER_NUM();
//			HashMap<String, Object> map=new HashMap<String, Object>();
//			map.put("USER_NUM",USER_NUM);
//			
//			String dbpwd=userService.findPwd(map);
//			if(passwordEncoder.matches(pwd, dbpwd)){
//				flag=true;
//			}
//		}
//		
//		return flag;
//	}	
//	
//	/**
//	 * 이메일 유효성 확인
//	 * 현재 사용 안함
//	 * @param email  이메일
//	 */		
//	@PostMapping("/vaildEmail")
//	@ResponseBody
//	public String validEmail(@RequestParam String email) {
//		SimpleMailMessage mailMessage = new SimpleMailMessage();
//		
//		Random r = new Random();
//		int a = r.nextInt(10);		
//		int b = r.nextInt(10);		
//		int c = r.nextInt(10);
//		int d = r.nextInt(10);
//		int e = r.nextInt(10);
//		int f = r.nextInt(10);
//		String data = a+""+b+""+c+""+d+""+e+""+f;
//		
//		mailMessage.setFrom("");
//		mailMessage.setTo(email);
//		mailMessage.setSubject("회원가입 이메일 인증번호 입니다."); // 제목
//		mailMessage.setText("인증번호: "+data); // 내용
//		
//		try {
//			mailSender.send(mailMessage);
//		}catch (Exception ex) {
//			System.out.println("이메일 유효성 예외발생: "+ex.getMessage());
//		}
//		return data;
//	}		
//	
		
}
