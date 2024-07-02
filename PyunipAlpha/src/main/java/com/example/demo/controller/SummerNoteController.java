package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Controller
public class SummerNoteController {
	
	@PostMapping(value="/uploadSummernoteImageFile")
	@ResponseBody
	public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {
//		JsonObject jsonObject = new JsonObject();
		
//		String fileRoot =  "C:\\summer\\";	//저장될 외부 파일 경로
		String fileRoot =  "/vosej2241/tomcat/webapps/ROOT/upload/image/summer/";	//저장될 외부 파일 경로
		String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
				
		String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명
		File targetFile = new File(fileRoot + savedFileName);	
		HashMap<String, String> jsonResponse = new HashMap<>();
		
		try {
			InputStream fileStream = multipartFile.getInputStream();
			FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
			jsonResponse.put("url", "/summernoteImage/" + savedFileName);
			jsonResponse.put("responseCode", "success");
			jsonResponse.put("savedFileName", savedFileName);

		} catch (IOException e) {
			FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
			jsonResponse.put("responseCode", "error");
			e.printStackTrace();
		}
		 // Gson 객체를 사용하여 HashMap을 JSON 문자열로 변환
	    Gson gson = new Gson();
	    String jsonString = gson.toJson(jsonResponse);
	    
		
		return jsonString;
	}
	
}
