package com.example.demo.dto;

import lombok.Data;

@Data
public class ErrorLogDto {
	private int ERROR_NUM;
	private String ERROR_TIME;
	private String ERROR_URL;
	private String ERROR;    
	private String USER_NUM;
}
