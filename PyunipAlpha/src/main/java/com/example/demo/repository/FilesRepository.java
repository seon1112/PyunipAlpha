package com.example.demo.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.FilesDto;
import com.example.demo.mapper.FilesMapper;

@Repository
public class FilesRepository {
	@Autowired
	private FilesMapper filesMapper;
	
	public Integer insertFiles(FilesDto dto) {
		return filesMapper.insertFiles(dto);
	}
	
	public List<FilesDto> findFilesByNum(HashMap<String, Object> map){
		return filesMapper.findFilesByNum(map);
	}
	
	public Integer download(HashMap<String, Object> map) {
		return filesMapper.download(map);
	}
	
	public Integer deleteFilesByName(HashMap<String, Object> map) {
		return filesMapper.deleteFilesByName(map);
	}
	
	public Integer getNextFileNum() {
		return filesMapper.getNextFileNum();
	}
}
