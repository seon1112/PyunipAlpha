package com.example.demo.security;

import java.util.Map;
import java.util.UUID;

import com.example.demo.dto.UserDto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class OAuthAttributes {
	private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String email;
    
    public static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
    	Map<String, Object> response = (Map<String, Object>) attributes.get("response");
    	return OAuthAttributes.builder()
    			.email((String) response.get("email"))
    			.attributes(attributes)
    			.nameAttributeKey(userNameAttributeName)
    			.build();
    }
    
    public static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
    	// kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
    	return OAuthAttributes.builder()
    			.attributes(attributes)
    			.email((String) kakaoAccount.get("email"))
    			.nameAttributeKey(userNameAttributeName)
                .build();
    }
    
    public UserDto toEntity() {
    	UserDto m=new UserDto();
    	//닉네임에 랜덤 문자 부여
    	String random=UUID.randomUUID().toString();
    	m.setEMAIL(email);
    	m.setUSER_NM("편입알파"+random.substring(0, 5));
    	m.setROLE("ROLE_USER");
    	return m;
    }
}
