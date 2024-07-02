package com.example.demo.dto;

import lombok.Data;

@Data
public class SltDetailInfoDto {
	private int UNIV_NUM;
	private int SLT_NUM;
	private String SLT_WAY;	/*일반평입 , 학사편입*/
	private String OPEN_YN; //공개여부 Y공개 N비공개
	private String APY_QUAL;
	private String SLT_STEP;    /*일괄합산 , 1단계 + 2단계*/
	private String ENG_PC;			
	private String MATH_PC;			
	private String MAJOR_PC;			
	private String PREV_GRADE;			
	private String INTV_PC;			
	private String RECOG_ENG_PC;			
	private String WHATEVER;			
	private String WHATEVER_NOTE;
	
	private int SLT_DETAIL_NUM;
	private int SLT_DETAIL_NUM2;
	private int size; /*사정모형 개수*/
	private String SLT_STEP2;    /*일괄합산 , 1단계 + 2단계*/
	private String ENG_PC2;			
	private String MATH_PC2;			
	private String MAJOR_PC2;			
	private String PREV_GRADE2;			
	private String INTV_PC2;			
	private String RECOG_ENG_PC2;			
	private String WHATEVER2;			
	private String WHATEVER_NOTE2;
	private String UNIV_NAME;
}
