package com.example.demo.dto;

import lombok.Data;

@Data
public class FilesDto {
	private int FILE_NUM;
	private String BRD_NUM;
	private String ORIGIN_NAME;	
	private String FILE_NAME;
	private long FILE_SIZE;			
	private String FANCY_SIZE;			
	private int DOWN_NUM;			
	private String REG_USER_NUM;
	private String REG_DTM;		
	private String REG_USER_IP;		
}
