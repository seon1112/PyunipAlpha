package com.example.demo.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.example.demo.dto.AcssLogDto;
import com.example.demo.dto.ErrorLogDto;

@Mapper
public interface LogMapper {
	Integer insertAcssLog(HashMap<String, Object> map);
	Integer insertAcssFailLog(HashMap<String, Object> map);
	List<AcssLogDto> findAcssLog(HashMap<String, Object> map, RowBounds rowBounds);
	Integer getNextAcssFailNum();
	Integer getNextAcssLogNum();
	Integer insertErrorLog(ErrorLogDto dto);
	Integer getNextErrorNum();
}
