package com.example.demo.dto;

import lombok.Data;

@Data
public class BoardDto {
	private String BRD_NUM;
	private String BRD_CTG;		       // 0 편입이야기 / 1 편입스터디 / 2 모집요강 / 3 편입뉴스 / 4 합격수기 / 5 합격인증샷
	private String BRD_DV;		       // F 자유주제 / Q 질문하기
	private String TITLE;
	private String CONTENT;
	private int VIEW_CNT;			
	private int LIKE_CNT;			
	private String REG_USER_NUM;
	private String REG_DTM;		
	private String REG_IP;		
	private String UPT_USER_NUM;		
	private String UPT_DTM;		
	private String UPT_IP;		
	private String RPY_YN;		        //답변 Y사용 / N미사용
	private String TOP_NOTICE_YN;		//상위공지 Y사용 / N미사용
	
	private String USER_NM; 
	private String LIKE_YN;		        //좋아요여부
	private int APY_SIZE;		        //모집인원
	private String PCD_WAY;		        // 0 온라인 / 1 오프라인 / 2 온라인+오프라인
	private String PCD_WAY_ST;		    // 0 온라인 / 1 오프라인 / 2 온라인+오프라인
	private String APPLY_ED_DTM;        //스터디모집마감일
	private String ST_DTM;		        //시작예정일
	private String ED_DTM;		        //종료일
	private String ED_YN;   	        //마감임박
	private String FILE_YN;         	//파일 보유 여부
	private String SUC_UNIV;			//성공후기 : 편입대학교
	private String SUC_YY;				//성공후기 : 편입년도		
}
