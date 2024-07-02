package com.example.demo.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.FilesDto;

@Mapper
public interface FilesMapper {
	Integer insertFiles(FilesDto dto);
	List<FilesDto> findFilesByNum(HashMap<String, Object> map);
	Integer download(HashMap<String, Object> map);
	Integer deleteFilesByName(HashMap<String, Object> map);
	Integer getNextFileNum();
}
