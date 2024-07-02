package com.example.demo.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class SummerUtils {
	/**
	 * 삭제된 게시물 파일 삭제
	 * @param filenames 파일
	 */			
    public static void deleteFilenames2(String filenames) {
    	String[] list=filenames.split(",");
    	for(String img : list) {
    		if(!img.equals("")) {
    			String fileRoot =  "/vosej2241/tomcat/webapps/ROOT/upload/image/summer/";	//저장된 외부 파일 경로
    			File targetFile = new File(fileRoot + img);	
    			FileUtils.deleteQuietly(targetFile);	
    		}
    	}
    }	
    
    /**
     * 수정된 파일 삭제
     * @param content 내용
     * @param filenames 수정 전 이미지
     */			
    public static void deleteFilenames(String content,String filenames) {
    	String[] list=filenames.split(",");
    	for(String img : list) {
    		if(!img.equals("")) {
    			if(content.indexOf(img) == -1) {
    				String fileRoot =  "/vosej2241/tomcat/webapps/ROOT/upload/image/summer/";	//저장된 외부 파일 경로
    				File targetFile = new File(fileRoot + img);	
    				FileUtils.deleteQuietly(targetFile);	
    			}
    		}
    	}
    }	
    
    /**
     * 파일 이름 확인
     * @param content 내용
     */			
    public static String extractFilenames(String content) {
    	StringBuffer filenames = new StringBuffer();
    	Pattern pattern = Pattern.compile("/summernoteImage/([^\"']+)"); // "/summernoteImage/" 이후의 파일 이름을 추출
    	
    	Matcher matcher = pattern.matcher(content);
    	while (matcher.find()) {
    		filenames.append(matcher.group(1)+",");
    	}
    	
    	return filenames.toString();
    }	
}
