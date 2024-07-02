package com.example.demo.dto;

import lombok.Data;

@Data
public class AcssLogDto {
	private int ACSS_LOG_NUM;
	private String ACSS_DTM;
	private String USER_NUM;		
	private String ACSS_IP;
	private String LOG;    //접근내용
	
	private String USER_NM;
	private String ROLE;
	
}
