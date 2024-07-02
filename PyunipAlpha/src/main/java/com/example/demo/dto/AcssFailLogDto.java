package com.example.demo.dto;

import lombok.Data;

@Data
public class AcssFailLogDto {
	private int ACSS_FAIL_NUM;
	private String FAIL_DTM;
	private String USER_NM;
	private String ACSS_IP;
	private String FAIL_ROUTE;
}
