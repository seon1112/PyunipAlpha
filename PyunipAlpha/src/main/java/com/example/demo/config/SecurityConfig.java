package com.example.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.UserDto;
import com.example.demo.security.CustomOAuth2AccessTokenResponseClient;
import com.example.demo.security.CustomOAuth2AuthorizedClientRepository;
import com.example.demo.security.SecurityUserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Environment env;
	private final SimpleUrlAuthenticationFailureHandler customAuthFailureHandler;
	private final SimpleUrlAuthenticationSuccessHandler customAuthSuccessHandler;
	private final SecurityUserService userService;
	
    @Autowired
    private CustomOAuth2AuthorizedClientRepository authorizedClientRepository;

    @Autowired
    private CustomOAuth2AccessTokenResponseClient accessTokenResponseClient;
	
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    	
    	http
        .authorizeHttpRequests(auth -> auth
				.requestMatchers(
						new AntPathRequestMatcher("/user/**")
						,new AntPathRequestMatcher("/board/talk_insert_form")
						,new AntPathRequestMatcher("/board/study_update_form")
						,new AntPathRequestMatcher("/board/talk_update_form")
						,new AntPathRequestMatcher("/board/study_insert_form")
						).authenticated()                
				.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")     		
                .anyRequest().permitAll()
                );
    	
        http.csrf().ignoringRequestMatchers(
        		"/uploadSummernoteImageFile"
        		,"/loginError"
        		);
        
		 http.formLogin((formLogin) -> formLogin 
		 .loginPage("/main/login_form").permitAll()
		 .failureHandler(customAuthFailureHandler)
		 .successHandler(customAuthSuccessHandler)
		 );
			
		http
        .oauth2Login((oauth2) -> oauth2
        		.authorizedClientRepository(authorizedClientRepository)
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                    .accessTokenResponseClient(accessTokenResponseClient)
                )
                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint  
                		.userService(userService)
                ) 
        		.successHandler(customAuthSuccessHandler)
        		.failureHandler(customAuthFailureHandler)
		);     
				 
                
        http
        .logout((logout) -> logout
                .logoutUrl("/login/logout")
                .logoutSuccessUrl("/main/main_form")
                .addLogoutHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                    	//소셜로그인 로그아웃
                    	if(session.getAttribute("m") != null) {
                			if(((UserDto)session.getAttribute("m")).getSOCIAL_NM().equals("KAKAO")) {
                				String clientId = env.getProperty("spring.security.oauth2.client.registration.kakao.client-id");
                				logoutFromKakao(clientId);
                			}
                    	}
                        session.removeAttribute("m");
                        session.invalidate(); 
                    }
                })   
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        // 로그아웃 성공 시, 클라이언트 캐시를 방지하기 위한 헤더를 추가
                        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                        response.setHeader("Pragma", "no-cache");
                        response.setHeader("Expires", "0");
                    	response.sendRedirect("/main/main_form"); 
                    }
                })
                .deleteCookies("remember-me","JSESSIONID")
        )
        //로그아웃 처리 후에 서버로부터 반환되는 모든 페이지가 캐시되지 않도록 HTTP 헤더를 설정
        .headers(headers -> headers
                .cacheControl(cacheControl -> cacheControl.disable())
        );
        
        return http.build();
    }
    

	private void logoutFromKakao(String clientId) {
	    String uri = String.format("https://kauth.kakao.com/oauth/logout?client_id=%s&logout_redirect_uri=%s",
	    		clientId, "http://pyunipalpha.shop/logout");
	    
	    try {
	        restTemplate.getForEntity(uri, null);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }	    
	}
	
}
