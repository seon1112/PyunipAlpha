package com.example.demo.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ErrorLogDto;
import com.example.demo.dto.UserDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LogService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	@Autowired
	private UserService userService;
	@Autowired
	private final HttpServletRequest request;
	@Autowired
	private final OAuthUserService oauthUserService;
	@Autowired
	private LogService logService;
    @Autowired
    private CustomOAuth2AuthorizedClientRepository authorizedClientRepository;
    private final HttpSession session;
	 
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
         OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

         String registrationId = userRequest.getClientRegistration().getRegistrationId();
//         String principalName = oAuth2User.getName();
         String principalName = registrationId;
         String refreshToken = authorizedClientRepository.getRefreshToken(registrationId, principalName).getTokenValue();
         UserDto m=new UserDto();
         
         /*********************naver******************************/
         if(registrationId.equals("naver")) {
        	 // OAuth2 로그인 진행 시 키가 되는 필드 값(PK) // id
        	 String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                     .getUserInfoEndpoint().getUserNameAttributeName();
        	 
        	 String response = oAuth2User.getAttributes().get("response").toString();
        	 
        	 response = response.replace("{", "").replace("}", "");
        	 String[] parts = response.split("=");
        	 String idValue = parts[1]; 
        	 
        	 //회원가입 혹은 로그인
             OAuthAttributes attributes = OAuthAttributes.ofNaver(registrationId, userNameAttributeName, oAuth2User.getAttributes());
             try {
				if(userService.findUsersByClientId(idValue)!=null) {
					 //기존 회원이라면 
					 m=userService.findUsersByClientId(idValue);
					 updateRefreshTokenIfNeeded(m, refreshToken);
					 //refreshToken 세션에서 삭제
					 m.setREFRESH_TOKEN("");
					 //세션 저장
					 request.getSession().setAttribute("m", m);
				 }else {
					 //새로운 회원이라면
					 //회원정보 저장
					 saveUser(attributes,"NAVER",idValue);
					 m=userService.findUsersByClientId(idValue);
					 request.getSession().setAttribute("m", m);
				     try {
				    	 oauthUserService.saveRefreshToken(idValue, refreshToken);
				     } catch (Exception e) {
				         throw new OAuth2AuthenticationException("Failed to save refresh token");
				     } 
				 }
			} catch (Exception e) {
				ErrorLogDto dto=new ErrorLogDto();
				String error = e.getMessage();
	        	dto.setERROR(error);
	        	dto.setERROR_URL("LoginFromNaver");
	        	logService.insertErrorLog2(dto);
			}
             
             return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(m.getROLE())),
                     attributes.getAttributes(),
                     attributes.getNameAttributeKey());
             
         /*********************kakao******************************/    
         }else if(registrationId.equals("kakao")) {
        	 String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                     .getUserInfoEndpoint().getUserNameAttributeName();
        	 
             OAuthAttributes attributes = OAuthAttributes.ofKakao(registrationId, userNameAttributeName, oAuth2User.getAttributes());
             String idValue = oAuth2User.getAttributes().get("id").toString();
             
             //회원가입 혹은 로그인
             try {
				if(userService.findUsersByClientId(idValue)!=null) {
					 //기존 회원이라면
					 m=userService.findUsersByClientId(idValue);
					 updateRefreshTokenIfNeeded(m, refreshToken);
					 //refreshToken 세션에서 삭제
					 m.setREFRESH_TOKEN("");
					 //세션 저장
					 request.getSession().setAttribute("m", m);
				 }else {
					 //새로운 회원이라면
					 saveUser(attributes,"KAKAO",idValue);
					 m=userService.findUsersByClientId(idValue);
					 request.getSession().setAttribute("m", m);
				     try {
				    	 oauthUserService.saveRefreshToken(idValue, refreshToken);
				     } catch (Exception e) {
				    	 e.printStackTrace();
				         throw new OAuth2AuthenticationException("Failed to save refresh token");
				     } 
				 }
			} catch (Exception e) {
				ErrorLogDto dto=new ErrorLogDto();
				String error = e.getMessage();
	        	dto.setERROR(error);
	        	dto.setERROR_URL("LoginFromKakao");
	        	logService.insertErrorLog2(dto);
				
			}
             
             return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(m.getROLE())),
                     attributes.getAttributes(),
                     attributes.getNameAttributeKey());
             
         }
         
    	return null;
    }
    
    /**
     * 유저 생성 로직
     * */
    private void saveUser(OAuthAttributes attributes, String social, String idValue) {
    	UserDto m = attributes.toEntity();
    	m.setCLIENT_ID(idValue);
    	m.setSOCIAL_NM(social); 
    	
    	userService.insertUsers(m);
    }
    

    /**
     * Refresh Token이 변경되었는지 확인하고, 변경되었다면 업데이트
     */
    private void updateRefreshTokenIfNeeded(UserDto user, String newRefreshToken) {
        if (!user.getREFRESH_TOKEN().equals(newRefreshToken)) {
            try {
            	oauthUserService.saveRefreshToken(user.getCLIENT_ID(), newRefreshToken);
                user.setREFRESH_TOKEN(newRefreshToken);
            } catch (Exception e) {
				ErrorLogDto dto=new ErrorLogDto();
				String error = e.getMessage();
	        	dto.setERROR(error);
	        	dto.setERROR_URL("FailToUpdateRefreshToken");
	        	logService.insertErrorLog2(dto);
            	throw new OAuth2AuthenticationException("Failed to update refresh token");
            }
        }
    }    
    
    
}
