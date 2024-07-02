package com.example.demo.dto;

import lombok.Data;

@Data
public class UnivInfoDto {
	private int UNIV_NUM;
	private String UNIV_NAME;
	private String ADDR;	
	private String URL;
	private String PHONE;
	private String REG_YY;		
	private String APY_ST_DTM;		
	private String APY_ED_DTM;		
	private String APY_NOTE;		
	private String SLT_DATE;		
	private String SLT_NOTE;		
	private String SUC_APY_DTM;		/*2023.11.04(화) 11:00 + 2023.11.05(수) 12:00*/
	private String SUC_APY_NOTE;		
	private String DOC_SUB_DTM;		
	private String DOC_SUB_NOTE;		
	private String INTV_DTM;		/*2023.11.04(화) 11:00 + 2023.11.05(수) 12:00*/
	private String INTV_NOTE;		
	private String LOGO;
}
