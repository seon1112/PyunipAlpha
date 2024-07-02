package com.example.demo.security;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.dto.UserDto;
import com.example.demo.repository.LogRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LogService;
import com.example.demo.util.IpUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
    @Autowired
    private final LogService logService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
    	UserDto dto=(UserDto)request.getSession().getAttribute("m");
    	//로그
    	try {
			logService.saveLog(dto,"login",request);
		} catch (Exception e) {
			e.printStackTrace();
		}

//    	//로그인 실패 횟수 0으로 초기화
//    	if(dto.getPWD_FAIL() != 0) {
//    		int USER_NUM=dto.getUSER_NUM();
//    		UsersRepository user=new UsersRepository();
//    		user.updatePwdFailZero(USER_NUM);
//    	}
    	
    	// 이전 url로 redirect
    	String prevPage = (String) request.getSession().getAttribute("prevPage");
    	redirectStrategy.sendRedirect(request, response, prevPage);
    }
    
}