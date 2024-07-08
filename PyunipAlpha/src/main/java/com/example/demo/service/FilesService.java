package com.example.demo.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.FilesDto;
import com.example.demo.repository.FilesRepository;
import com.example.demo.util.EncryptionUtil;

@Service
public class FilesService {
	@Autowired
	private FilesRepository filesRpt;
    @Value("${encryption.key}")
    private String encryptionKey;
	
	@Transactional
	public Integer insertFiles(FilesDto dto) {
		dto.setFILE_NUM(filesRpt.getNextFileNum());
		return filesRpt.insertFiles(dto);
	}
	
	public List<FilesDto> findFilesByNum(HashMap<String, Object> map) throws Exception{
    	String BRD_NUM=(String)map.get("BRD_NUM");
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		String ENCRY_BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		map.put("BRD_NUM", ENCRY_BRD_NUM);
    	}
    	map.put("BRD_NUM", BRD_NUM);
    	
    	List<FilesDto> list=filesRpt.findFilesByNum(map);
    	for(FilesDto dto : list) {
    		dto.setBRD_NUM(EncryptionUtil.encrypt(dto.getBRD_NUM(), encryptionKey));
    	}
		return list;
	}
	
	public Integer download(HashMap<String, Object> map) {
		return filesRpt.download(map);
	}
	
	public Integer deleteFilesByName(HashMap<String, Object> map) {
		return filesRpt.deleteFilesByName(map);
	}
	
//	public Integer getNextFileNum() {
//		return filesRpt.getNextFileNum();
//	}
	
}
