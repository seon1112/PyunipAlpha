package com.example.demo.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.OAuthUserService;
import com.example.demo.util.EncryptionUtil;

@Service
public class UserService {
	@Autowired
	private UserRepository usersRpt;
	@Autowired
	private OAuthUserService oAuthUserService;
	
    @Value("${encryption.key}")
    private String encryptionKey;
	
	@Transactional
	public void deleteUser(HashMap<String, Object>map, String CLIENT_ID,String social) throws Exception {
		if(social.equals("KAKAO")) {
			try {
				oAuthUserService.unlinkedFromKakao(CLIENT_ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(social.equals("NAVER")) {
			try {
				oAuthUserService.unlinkedFromNaver(CLIENT_ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String USER_NUM=(String)map.get("USER_NUM");
		if(USER_NUM!=null  && !USER_NUM.equals("")) {
			map.put("USER_NUM", EncryptionUtil.decrypt(USER_NUM, encryptionKey));
		}
		usersRpt.deleteUsers(map);
	}
	
	public UserDto findUsersByNum(String USER_NUM) throws Exception {
		if(USER_NUM!=null  && !USER_NUM.equals("")) {
			USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
		}
		UserDto dto=usersRpt.findUsersByNum(USER_NUM);
		return dto;
	}
	
	@Transactional
	public Integer insertUsers(UserDto dto) {
		String nextNum=usersRpt.getNextUserNum();
		dto.setUSER_NUM(nextNum);
		return usersRpt.insertUsers(dto);
	}
	
	public Integer checkUsersNm(String USER_NM) {
		return usersRpt.checkUsersNm(USER_NM);
	}
	
	public Integer updateUsers(UserDto user) throws Exception {
		if(user.getUSER_NUM()!=null) {
			user.setUSER_NUM(EncryptionUtil.decrypt(String.valueOf(user.getUSER_NUM()), encryptionKey));
		}
		return usersRpt.updateUsers(user);
	}
	
	public Integer updateAdmin(HashMap<String, Object> map) {
		return usersRpt.updateAdmin(map);
	}
	
	public List<UserDto> findUsersBySch(HashMap<String, Object> map) throws Exception{
		List<UserDto> list=usersRpt.findUsersBySch(map);
		for(UserDto user:list) {
			if (user != null) {
				String encryptedUserNum = EncryptionUtil.encrypt(String.valueOf(user.getUSER_NUM()), encryptionKey);
				user.setUSER_NUM(encryptedUserNum);
			}
			
		}
		return list;
	}
	
	public UserDto findUserseByNum(HashMap<String, Object> map) throws Exception {
		String USER_NUM=(String)map.get("USER_NUM");
		if(USER_NUM!=null  && !USER_NUM.equals("")) {
			map.put("USER_NUM", EncryptionUtil.decrypt(USER_NUM, encryptionKey));
		}
		
		UserDto user=usersRpt.findUserseByNum(map);
		if (user != null) {
			String encryptedUserNum = EncryptionUtil.encrypt(String.valueOf(user.getUSER_NUM()), encryptionKey);
			user.setUSER_NUM(encryptedUserNum);
		}
		return user;
	}
	
	public UserDto findUsersByClientId(String CLIENT_ID) throws Exception {
		UserDto user=usersRpt.findUsersByClientId(CLIENT_ID);
		if (user != null) {
			String encryptedUserNum = EncryptionUtil.encrypt(String.valueOf(user.getUSER_NUM()), encryptionKey);
			user.setUSER_NUM(encryptedUserNum);
		}
		return user;
	}
	
	/****************************자체 로그인 구현시 필요했던 코드***************************************************/	
	public Integer checkPwd(HashMap<String, Object> map) {
		return usersRpt.checkPwd(map);
	}
	public String findPwd(HashMap<String, Object> map) {
		return usersRpt.findPwd(map);
	}
	public Integer updatePwdFail(String USER_NUM) {	
		return usersRpt.updatePwdFail(USER_NUM);
	}
	public Integer updatePwdFailZero(String USER_NUM) {	
		return usersRpt.updatePwdFailZero(USER_NUM);
	}
	public int findPwdFail(String USER_NUM) {
		return usersRpt.findPwdFail(USER_NUM);
	}
	public UserDto findUsersByEmail(String USER_NM) {
		return usersRpt.findUsersByEmail(USER_NM);
	}	
	public Integer updateUsers2(UserDto dto) {
		return usersRpt.updateUsers2(dto);
	}
	public Integer updatepwd(HashMap<String, Object> map) {
		return usersRpt.updatepwd(map);
	}
	public Integer checkHint(HashMap<String, String> map) {
		return usersRpt.checkHint(map);
	}	
	public Integer checkEmail(String EMAIL) {
		return usersRpt.checkEmail(EMAIL);
	}	
}
