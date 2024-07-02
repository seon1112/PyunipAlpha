package com.example.demo.dto;

import lombok.Data;

@Data
public class TransInfoDto {
	private int UNIV_NUM;
	private int TRANS_NUM;
	private String SERIES;	// N 자연 / H 인문
	private String DEPT_NM;
	private String MAJOR_NM;
	private String REG_YY;
	private int RECRU_SIZE;			
	private int APY_SIZE;			
	private String COMPETITION;
	private String SLT_WAY;		
	
	private String UNIV_NAME; 
}
