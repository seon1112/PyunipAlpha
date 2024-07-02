package com.example.demo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;


@Component
public class CustomOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private final DefaultAuthorizationCodeTokenResponseClient delegate = new DefaultAuthorizationCodeTokenResponseClient();

    @Autowired
    private OAuth2AuthorizedClientRepository authorizedClientRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response2;

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
    	// 기본 클라이언트를 통해 액세스 토큰 응답을 받음
    	OAuth2AccessTokenResponse response = delegate.getTokenResponse(authorizationGrantRequest);
        OAuth2AccessToken accessToken = response.getAccessToken();
        OAuth2RefreshToken refreshToken = response.getRefreshToken();
        String clientRegistrationId = authorizationGrantRequest.getClientRegistration().getRegistrationId();

        /*
         Principal principal = request.getUserPrincipal();
         String principalName = principal.getName();
         --> principal === null?
         
        `Principal` 및 `Authentication` 객체가 `null`인 이유는, OAuth2 인증 과정이 아직 완료되지 않았기 때문이다. 
         Spring Security는 OAuth2 인증이 완료되면 `OAuth2LoginAuthenticationFilter`에서 `Authentication` 객체를
 		`SecurityContextHolder`에 설정한다.
		`OAuth2AccessTokenResponseClient`에서 `Authentication` 객체를 사용할 수 없는 시점에서 호출되기 때문에 발생하는 문제.
		
		이러한 문제로 principalName을 clientRegistrationId로 설정하여 로직을 진행하고,
		CustomAuthSuccessHandler에서 `Authentication` 객체를 `SecurityContextHolder`에 설정
		(꼭 설정하지 않아도 되나 명시적으로 보여주기 위해 설정)
        */
        
        String principalName = clientRegistrationId;
        
        // OAuth2AuthorizedClient 생성
        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
                authorizationGrantRequest.getClientRegistration(),
                principalName,
                accessToken,
                refreshToken);
        
        // OAuth2AuthorizedClient 저장
        authorizedClientRepository.saveAuthorizedClient(authorizedClient, null, request, response2);

        return response;
    }
}
