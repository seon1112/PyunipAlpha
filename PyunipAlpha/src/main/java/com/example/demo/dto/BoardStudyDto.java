package com.example.demo.dto;

import lombok.Data;

@Data
public class BoardStudyDto {
	private String BRD_NUM;		//게시판의 게시글_일련번호와 동일
	private int APY_SIZE;		//모집인원
	private String PCD_WAY;		// 0 온라인 / 1 오프라인 / 2 온라인+오프라인
	private String APPLY_ED_DTM; //스터디모집마감일
	private String ST_DTM;		//시작예정일
	private String ED_DTM;		//종료일
	
	private String TITLE;
	private String CONTENT;
	private int VIEW_CNT;
	private int LIKE_CNT;
	private String REG_DTM;
	private String USER_NM;
	private String BRD_CTG;
	private String ED_YN;   //마감임박
	private String REG_USER_NUM;
	private String REG_IP;		
	private String UPT_USER_NUM;		
	private String UPT_DTM;		
	private String UPT_IP;		
}
