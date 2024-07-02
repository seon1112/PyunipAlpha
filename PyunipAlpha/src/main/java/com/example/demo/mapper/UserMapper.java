package com.example.demo.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.example.demo.dto.UserDto;

@Mapper
public interface UserMapper {
	 UserDto findUsersByEmail(String USER_NM);
	 UserDto findUsersByNum(String USER_NUM);
	 Integer insertUsers(UserDto dto);
	 Integer checkUsersNm(String USER_NM);
	 Integer checkHint(HashMap<String, String> map);
	 Integer updateUsers(UserDto dto);
	 Integer updateUsers2(UserDto dto);
	 Integer updatepwd(HashMap<String, Object> map);
	 Integer deleteUsers(HashMap<String, Object> map);
	 Integer updateAdmin(HashMap<String, Object> map);
	 Integer checkEmail(String EMAIL);
	 List<UserDto> findUsersBySch(HashMap<String, Object> map, RowBounds rowBounds);
	 UserDto findUserseByNum(HashMap<String, Object> map);
	 String getNextUserNum();
	 Integer checkPwd(HashMap<String, Object> map);
	 String findPwd(HashMap<String, Object> map);
	 UserDto findUsersByClientId(String CLIENT_ID);
	 Integer updatePwdFail(String USER_NUM);
	 Integer updatePwdFailZero(String USER_NUM);
	 int findPwdFail(String USER_NUM);
	 void saveRefreshToken(String CLIENT_ID, String REFRESH_TOKEN);
	 void updateRefreshToken(String CLIENT_ID, String REFRESH_TOKEN);
	 String getRefreshToekn(String CLIENT_ID);
}
