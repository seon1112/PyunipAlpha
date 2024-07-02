package com.example.demo.dto;

import lombok.Data;

@Data
public class SuccessDto {
	private String BRD_NUM;
	private String BRD_CTG;		// 0 편입이야기 / 1 편입스터디 / 2 모집요강 / 3 편입뉴스 / 4 합격수기 / 5 합격인증샷
	private String TITLE;
	private int VIEW_CNT;			
	private int LIKE_CNT;			
	private String REG_USER_NUM;
	private String REG_DTM;		
	private String REG_IP;		
	private String UPT_USER_NUM;		
	private String UPT_DTM;		
	private String UPT_IP;		
	
	private String USER_NM; 
	private String LIKE_YN;		//좋아요여부
	private String FILE_YN; 	//파일 보유 여부
	private int FILE_NUM;
	private String ORIGIN_NAME;	
	private String FILE_NAME;
	private long FILE_SIZE;			
	private String FANCY_SIZE;			
}
