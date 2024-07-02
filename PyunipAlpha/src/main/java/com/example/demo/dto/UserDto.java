package com.example.demo.dto;

import lombok.Data;

@Data
public class UserDto {
	private String USER_NUM;
	private String USER_NM;
	private String EMAIL;
	private String REG_DTM;
	private String RCT_REG_DTM;  //최근접속시간
	private String ROLE;		//USER / ADMIN
	private String CLIENT_ID;  //소셜 pk 아이디
	private String SOCIAL_NM;  //소셜이름
	private String REFRESH_TOKEN;
}
