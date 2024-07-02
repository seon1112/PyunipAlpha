package com.example.demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class SignUpUtils {
	
	/** 
	 * 비밀번호 유효성 체크 
	 * @param 
	 * @return boolean 맞으면 true / 틀리면 false
	 * **/
	public static boolean getCheckPwd(String pwd){
		boolean result=false;
//		String patternString=" ^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*\\W).*$";
		String patternString="(?=.*\\d)(?=.*[a-zA-Z])(?=.*\\W).*$";
		
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher=pattern.matcher(pwd);
		
		result=matcher.find();
		
		return result;
	}
	
	
}
