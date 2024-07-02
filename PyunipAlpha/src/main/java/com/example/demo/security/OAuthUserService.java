package com.example.demo.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.ErrorLogDto;
import com.example.demo.dto.UserDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LogService;
import com.example.demo.util.EncryptionUtil;

@Component
@Service
public class OAuthUserService {
	private RestTemplate restTemplate;
	private Environment env;
    private UserRepository usersRpy;
    private String encryptionKey; 
    private LogService logService;
    
    @Autowired
    public OAuthUserService(RestTemplate restTemplate, Environment env, UserRepository usersRpy, @Value("${encryption.key}") String encryptionKey,LogService logService) {
        this.restTemplate = restTemplate;
        this.env = env;
        this.usersRpy = usersRpy;
        this.encryptionKey = encryptionKey;
        this.logService=logService;
    }    
	
    // 카카오 탈퇴
    public void unlinkedFromKakao(String CLIENT_ID) throws Exception {
        String url = "https://kapi.kakao.com/v1/user/unlink";
        
        // Refresh Token 복호화
        String refreshToken = getDecryptedRefreshToken(CLIENT_ID);
        // 새로운 Access Token 발급
        String accessToken = getNewAccessTokenFromKakao(refreshToken);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        // HTTP 요청 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(new LinkedMultiValueMap<>(), headers);

        try {
        	// 사용자 언링크 요청
        	ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			
        	if (response.getStatusCode() == HttpStatus.OK) {
        		System.out.println("User unlink successful: " + response.getBody());
        	} else {
        		String error="Failed to unlink user: " + response.getBody();
        		ErrorLogDto dto=new ErrorLogDto();
        		dto.setERROR(error);
        		dto.setERROR_URL("unlinkedFromKakao");
        		logService.insertErrorLog2(dto);
        	}        
		} catch (Exception e) {
        	String error="Failed to unlink from kakao: " + e.getMessage();
        	ErrorLogDto dto=new ErrorLogDto();
        	dto.setERROR(error);
        	dto.setERROR_URL("unlinkedFromKakao");
        	logService.insertErrorLog2(dto);
		}
        
    }

    // 카카오에서 새로운 Access Token 발급
    private String getNewAccessTokenFromKakao(String refreshToken) {
        String url = "https://kauth.kakao.com/oauth/token";
        
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP 바디 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", env.getProperty("spring.security.oauth2.client.registration.kakao.client-id"));
        body.add("client_secret", env.getProperty("spring.security.oauth2.client.registration.kakao.client-secret"));
        body.add("refresh_token", refreshToken);

        // HTTP 요청 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // 새로운 Access Token 발급 요청
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            return (String) responseBody.get("access_token");
        } else {
        	String error="Failed to get new access token from Kakao: " + response.getBody();
        	ErrorLogDto dto=new ErrorLogDto();
        	dto.setERROR(error);
        	dto.setERROR_URL("getNewAccessTokenFromKakao");
        	logService.insertErrorLog2(dto);
        	throw new RuntimeException(error);
        }
    }    
	
	//네이버 탈퇴
	public void unlinkedFromNaver(String CLIENT_ID) throws Exception {
        // Refresh Token 복호화
        String refreshToken = getDecryptedRefreshToken(CLIENT_ID);
        // 새로운 Access Token 발급
        String accessToken = getNewAccessTokenFromNaver(refreshToken);

		String clientId = env.getProperty("spring.security.oauth2.client.registration.naver.client-id");
		String clientSecret = env.getProperty("spring.security.oauth2.client.registration.naver.client-secret");
	    String uri = String.format("https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=%s&client_secret=%s&access_token=%s&service_provider=NAVER",
                clientId, clientSecret, accessToken);
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    
        // HTTP 요청 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(new LinkedMultiValueMap<>(), headers);

	    try {
	    	// 사용자 언링크 요청
	    	ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
	        if (response.getStatusCode() == HttpStatus.OK) {
	        	System.out.println(response.getBody());
	            System.out.println("네이버 탈퇴가 성공적으로 처리되었습니다.");
	        } else {
	        	String error="Failed to unlink from Naver: " + response.getStatusCode();
	        	ErrorLogDto dto=new ErrorLogDto();
	        	dto.setERROR(error);
	        	dto.setERROR_URL("unlinkedFromNaver");
	        	logService.insertErrorLog2(dto);
	        }
	    } catch (Exception e) {
        	String error="Failed to unlink from Naver: " + e.getMessage();
        	ErrorLogDto dto=new ErrorLogDto();
        	dto.setERROR(error);
        	dto.setERROR_URL("unlinkedFromNaver");
        	logService.insertErrorLog2(dto);
	    }	
	}	    
	
	
    // 네이버에서 새로운 Access Token 발급
    private String getNewAccessTokenFromNaver(String refreshToken) {
        String url = "https://nid.naver.com/oauth2.0/token";
        
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP 바디 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", env.getProperty("spring.security.oauth2.client.registration.naver.client-id"));
        body.add("client_secret", env.getProperty("spring.security.oauth2.client.registration.naver.client-secret"));
        body.add("refresh_token", refreshToken);

        // HTTP 요청 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // 새로운 Access Token 발급 요청
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            return (String) responseBody.get("access_token");
        } else {
        	String error="Failed to get new access token from naver: " + response.getBody();
        	ErrorLogDto dto=new ErrorLogDto();
        	dto.setERROR(error);
        	dto.setERROR_URL("getNewAccessTokenFromNaver");
        	logService.insertErrorLog2(dto);
        	throw new RuntimeException(error);
        }
    }    	
	
	//토큰 암호화하여 저장
    public void saveRefreshToken(String CLIENT_ID, String refreshToken) throws Exception {
        String encryptedToken = EncryptionUtil.encrypt(refreshToken, encryptionKey); // Refresh Token 암호화
        usersRpy.saveRefreshToken(CLIENT_ID, encryptedToken);
    }

    //토큰 복호화
    public String getDecryptedRefreshToken(String CLIENT_ID) throws Exception {
        UserDto user = usersRpy.findUsersByClientId(CLIENT_ID);
        String encryptedToken = user.getREFRESH_TOKEN();
        return EncryptionUtil.decrypt(encryptedToken, encryptionKey); // Refresh Token 복호화
    }
}
