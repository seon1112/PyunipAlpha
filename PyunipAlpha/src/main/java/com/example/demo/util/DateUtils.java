package com.example.demo.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
@Service
public class DateUtils {
	
	//yyyy.MM.dd (E)
	public String DateConversion(String inputDate) {
		String formattedDate = "";
		try {
			if(inputDate != null) {
				LocalDate date = LocalDate.parse(inputDate.trim(), DateTimeFormatter.BASIC_ISO_DATE);
				
				// 출력 형식 지정
				DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)");
				
				// 변환된 날짜를 원하는 형식으로 출력
				formattedDate = date.format(outputFormatter);
				
			}else {
				formattedDate="";
			}
		} catch (Exception e) {
			formattedDate="";
		}

		return formattedDate;
	}

	//yyyy-MM-dd
	public String DateConversion2(String inputDate) {
		String formattedDate="";
		try {
			if(inputDate != null) {
				LocalDate date = LocalDate.parse(inputDate.trim(), DateTimeFormatter.BASIC_ISO_DATE);
				
				// 출력 형식 지정
				DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				
				// 변환된 날짜를 원하는 형식으로 출력
				formattedDate = date.format(outputFormatter);			
			}else {
				formattedDate="";
			}
		} catch (Exception e) {
			formattedDate="";
		}
		return formattedDate;
	}
	
	//yyyy.MM.dd (E) hh:mm
	public String DateConversion3(String inputDate) {
		StringBuffer formattedDate = new StringBuffer();
		try {
			if(inputDate != null) {
				LocalDate date = LocalDate.parse(inputDate.trim().substring(0, 8), DateTimeFormatter.BASIC_ISO_DATE);
				
				// 출력 형식 지정
				DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)");
				
				// 변환된 날짜를 원하는 형식으로 출력
				formattedDate.append(date.format(outputFormatter)).append(" ").append(inputDate.substring(8, 10)).append(":").append(inputDate.substring(10, 12));
				
			}else {
				formattedDate.append("");
			}
		} catch (Exception e) {
			formattedDate.append("");
		}
		return formattedDate.toString();
	}
	
	//date conversion list yyyy-MM-dd
	public List<String> GetDateList(String dtm) {
		String arr[] = dtm.split("\\^");
		List<String> list=new ArrayList<>();
		
		for(String date : arr) {
			if(!date.equals("")) {
				list.add(DateConversion2(date));
			}
		}
		
		return list;
	}
		
	//date conversion list yyyy.MM.dd (E)
	public List<String> GetDateList2(String dtm) {
		String arr[] = dtm.split("\\^");
		List<String> list=new ArrayList<>();
		
		for(String date : arr) {
			if(!date.equals("")) {
				list.add(DateConversion(date));
			}
		}
		
		return list;
	}
	
	/**
	 * 현재 년도를 반환하는 메소드
	 * @return 현재 년도
	 */
	public static String getYear() {
		Calendar today=Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		return year+"";
	}		
	
}
