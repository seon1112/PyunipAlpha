package com.example.demo.security;

import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.demo.dto.UserDto;
import com.example.demo.repository.LogRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LogService;
import com.example.demo.util.IpUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Autowired
	private UserRepository usersRept;
	@Autowired
	private LogService logService;
    private String errorUrl = "/loginError"; // 기본 에러 URL

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "";
        String error = "";
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oAuth2Ex = (OAuth2AuthenticationException) exception;
            if ("access_denied".equals(oAuth2Ex.getError().getErrorCode())) {
                response.sendRedirect("/main/login_form");
                return;
            }
        } else if (exception instanceof BadCredentialsException) {
            String msg = handleBadCredentialsException(request);
            String arr[]=msg.split("\\+");
            errorMessage=arr[0];
            error = arr[1];
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
            error = "내부문제";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
            error = "계정없음";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
            error = "인증거부";
        } else {
            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요.";
            error = "이유모름";
        }

        // 실패 로그 저장 및 사용자 정보 기록
        logFailure(request, error);

        // 로그인 세션 파기
        request.getSession().invalidate();

        // 에러 페이지로 포워딩
        request.getRequestDispatcher(errorUrl + "?error=true&errorM=" + errorMessage).forward(request, response);
    }

	/**
	 * 실패 로그 저장
	 * */ 
    private void logFailure(HttpServletRequest request, String error) {
		String id="";
		if(request.getSession().getAttribute("m") != null) {
			id=((UserDto)request.getSession().getAttribute("m")).getUSER_NM();
		}else if(request.getSession().getAttribute("id") != null){
			id=(String)request.getSession().getAttribute("id");
		}else if(request.getSession().getAttribute("fail") != null){
			id=((UserDto)request.getSession().getAttribute("fail")).getUSER_NM();
		}else {
			id="오류발생";
		}
		saveFailLog(request, id, error);
    }
    
 /****************************자체 로그인 구현시 필요했던 코드***************************************************/
    
	/**
	 * BadCredentialsException 처리 로직
	 * */    
    private String handleBadCredentialsException(HttpServletRequest request) {
    	String errorMessage="";
    	String error="";
    	
		if(request.getSession().getAttribute("m") != null) {
			String USER_NUM=((UserDto)request.getSession().getAttribute("m")).getUSER_NUM();
			//비밀번호 실패횟수 추가
			updatePwdFail(USER_NUM);
			//비밀번호 실패횟수 조회
			int pwd_fail=findPwdFail(USER_NUM);
			
			errorMessage = "비밀번호가 맞지 않습니다. 다시 확인해 주세요.(실패 횟수 : "+pwd_fail+"회 / 5회)";
			error="id,pwd 실패";				
		}else if(request.getSession().getAttribute("fail") != null) { 
			//비밀번호 시도 횟수 초과
			errorMessage = "비밀번호 실패 횟수를 초과했습니다. 관리자에게 문의해주세요.";
			error="비밀번호 실패 횟수 초과";				
		}else {
			//존재하지 않는 계정
			errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
			error="계정없음";					
		}
		
        return errorMessage+"+"+error;
    }
    
	/**
	 * 실패 로그 
	 * @param id 로그인 시도한 아이디
	 * @param error 에러
	 * */
	private void saveFailLog(HttpServletRequest req,String id, String error) {
		String remoteIp=IpUtils.getIpFromRequest(req);
		
		HashMap<String, Object> map=new HashMap<>();
		map.put("ACSS_IP", remoteIp);
		map.put("USER_NM",id);        //로그인 시도한 아이디
		map.put("FAIL_ROUTE",error);
		
		logService.insertAcssFailLog(map);
	}
	
	/**
	 * 실패 횟수 추가
	 * @param USER_NUM 회원번호
	 * */	
	private void updatePwdFail(String USER_NUM) {
		usersRept.updatePwdFail(USER_NUM);
	}
	
	/**
	 * 실패 횟수 추가
	 * @param USER_NUM 회원번호
	 * @return 실패횟수
	 * */	
	private int findPwdFail(String USER_NUM) {
		return usersRept.findPwdFail(USER_NUM);
	}    
}