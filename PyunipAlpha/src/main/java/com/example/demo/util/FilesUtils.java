package com.example.demo.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.FilesDto;
import com.example.demo.repository.FilesRepository;

@Component
@Service
public class FilesUtils {
	@Value("${file.upload-dir}")
	private String uploadPath;
	@Value("${logo.upload-dir}")
	private String logoUploadPath;
	@Value("${proof.upload-dir}")
	private String proofUploadPath;
	
	/** 
	 * 파일 등록 
	 * @param dv "1" file , "2" 인증샷
	 * **/
	public FilesDto getAttachByMultipart(MultipartFile multipart, String dv) throws IOException {
		if (!multipart.isEmpty()) {
			String fileName = UUID.randomUUID().toString();
			FilesDto dto = new FilesDto();
			String originalName=multipart.getOriginalFilename();
			String accept=originalName.substring(originalName.lastIndexOf("."));
			fileName+=accept;
			
			dto.setORIGIN_NAME(originalName);
			dto.setFANCY_SIZE(fancySize(multipart.getSize()));
			dto.setFILE_NAME(fileName);
			dto.setFILE_SIZE(multipart.getSize());
			dto.setDOWN_NUM(0);
//			dto.setFILE_NUM(filesRept.getNextFileNum());
			
			String filePath="";
			if(dv.equals("1")) {
				filePath = uploadPath+File.separatorChar;		
			}else if(dv.equals("2")) {
				filePath = proofUploadPath+File.separatorChar;		
				
			}
			FileUtils.copyInputStreamToFile(multipart.getInputStream(), new File(filePath, fileName));
			return dto;
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * 파일 삭제
	 * @param dv "1" file , "2" 인증샷
	 * **/
	public int deleteAttachByBoard(String fileName, String dv) throws IOException{
		int re=0;
		try {
			String filePath="";
			if(dv.equals("1")) {
				filePath=uploadPath+ File.separatorChar;
			}else if(dv.equals("2")) {
				filePath=proofUploadPath+ File.separatorChar;
			}
			File file=new File(filePath,fileName);
			file.delete();			
		} catch (Exception e) {
			re = 1;
		}

		return re;
	}

	private DecimalFormat df = new DecimalFormat("#,###.0");

	private String fancySize(long size) {
		if (size < 1024) { // 1k 미만
			return size + " Bytes";
		} else if (size < (1024 * 1024)) { // 1M 미만
			return df.format(size / 1024.0) + " KB";
		} else if (size < (1024 * 1024 * 1024)) { // 1G 미만
			return df.format(size / (1024.0 * 1024.0)) + " MB";
		} else {
			return df.format(size / (1024.0 * 1024.0 * 1024.0)) + " GB";
		}
	}
	
	/**이미지 파일 삭제**/
	public int deleteImage(String fileName) throws IOException{
		int re=0;
		try {
			String filePath=logoUploadPath+ File.separatorChar;
			File file=new File(filePath,fileName);
			file.delete();			
		} catch (Exception e) {
			re = 1;
		}

		return re;
	}	
	
	/**
	 * 이미지 파일 등록 
	 * @param dv : "1" 로고 등록
	 * **/
	public String getAttachByMultipart2(MultipartFile multipart,String dv) throws IOException {
		if (!multipart.isEmpty()) {
			
			String originalName=multipart.getOriginalFilename();
			String filePath="";
			if(dv.equals("1")) {
				filePath = logoUploadPath+File.separatorChar;		
			}
			FileUtils.copyInputStreamToFile(multipart.getInputStream(), new File(filePath, originalName));
			return originalName;
			
		} else {
			return null;
		}
	}	
	
}
