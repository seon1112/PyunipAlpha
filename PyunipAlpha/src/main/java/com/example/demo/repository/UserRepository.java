package com.example.demo.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.UserDto;
import com.example.demo.mapper.UserMapper;

@Repository
public class UserRepository {
	@Autowired
	private UserMapper usersMapper;
	
	public UserDto findUsersByEmail(String USER_NM) {
		return usersMapper.findUsersByEmail(USER_NM);
	}
	public UserDto findUsersByNum(String USER_NUM) {
		return usersMapper.findUsersByNum(USER_NUM);
	}
	public Integer insertUsers(UserDto dto) {
		return usersMapper.insertUsers(dto);
	}
	public Integer checkUsersNm(String USER_NM) {
		return usersMapper.checkUsersNm(USER_NM);
	}
	public Integer updateUsers(UserDto dto) {
		return usersMapper.updateUsers(dto);
	}
	public Integer updateUsers2(UserDto dto) {
		return usersMapper.updateUsers2(dto);
	}
	public Integer updatepwd(HashMap<String, Object> map) {
		return usersMapper.updatepwd(map);
	}
	public Integer checkHint(HashMap<String, String> map) {
		return usersMapper.checkHint(map);
	}
	public Integer deleteUsers(HashMap<String, Object> map) {
		return usersMapper.deleteUsers(map);
	}
	public Integer updateAdmin(HashMap<String, Object> map) {
		return usersMapper.updateAdmin(map);
	}
	public Integer checkEmail(String EMAIL) {
		return usersMapper.checkEmail(EMAIL);
	}
	public List<UserDto> findUsersBySch(HashMap<String, Object> map){
        if (map.get("nMaxVCnt") != null) {
            int pageSize = (int) map.get("nMaxVCnt");
            int pageNumber = (int) map.get("nSelectPage");
            int offset = (pageNumber - 1) * pageSize;
            RowBounds rowBounds = new RowBounds(offset, pageSize);
            return usersMapper.findUsersBySch(map, rowBounds);
        } else {
            return usersMapper.findUsersBySch(map, RowBounds.DEFAULT);
        }
	}
	public UserDto findUserseByNum(HashMap<String, Object> map) {
		return usersMapper.findUserseByNum(map);
	}
	public String getNextUserNum() {
		return usersMapper.getNextUserNum();
	}
	public Integer checkPwd(HashMap<String, Object> map) {
		return usersMapper.checkPwd(map);
	}
	public String findPwd(HashMap<String, Object> map) {
		return usersMapper.findPwd(map);
	}
	public UserDto findUsersByClientId(String CLIENT_ID) {
		return usersMapper.findUsersByClientId(CLIENT_ID);
	}
	public Integer updatePwdFail(String USER_NUM) {	
		return usersMapper.updatePwdFail(USER_NUM);
	}
	public Integer updatePwdFailZero(String USER_NUM) {	
		return usersMapper.updatePwdFailZero(USER_NUM);
	}
	public int findPwdFail(String USER_NUM) {
		return usersMapper.findPwdFail(USER_NUM);
	}
	public void saveRefreshToken(String CLIENT_ID, String REFRESH_TOKEN) {
		usersMapper.saveRefreshToken(CLIENT_ID, REFRESH_TOKEN);
	}
	public void updateRefreshToken(String CLIENT_ID, String REFRESH_TOKEN) {
		usersMapper.updateRefreshToken(CLIENT_ID, REFRESH_TOKEN);
	}
	public String getRefreshToekn(String CLIENT_ID) {
		return usersMapper.getRefreshToekn(CLIENT_ID);
	}
}
