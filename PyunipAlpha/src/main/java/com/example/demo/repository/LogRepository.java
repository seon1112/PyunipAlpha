package com.example.demo.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.AcssLogDto;
import com.example.demo.dto.ErrorLogDto;
import com.example.demo.mapper.LogMapper;


@Repository
public class LogRepository {
	@Autowired
	private LogMapper logMapper;
	
	public Integer insertAcssLog(HashMap<String, Object> map) {
		return logMapper.insertAcssLog(map);
	}
	
	public Integer insertAcssFailLog(HashMap<String, Object> map) {
		return logMapper.insertAcssFailLog(map);
	}
	
	public List<AcssLogDto> findAcssLog(HashMap<String, Object> map){
        if (map.get("nMaxVCnt") != null) {
            int pageSize = (int) map.get("nMaxVCnt");
            int pageNumber = (int) map.get("nSelectPage");
            int offset = (pageNumber - 1) * pageSize;
            RowBounds rowBounds = new RowBounds(offset, pageSize);
            return logMapper.findAcssLog(map, rowBounds);
        } else {
            return logMapper.findAcssLog(map, RowBounds.DEFAULT);
        }
	}
	
	public Integer getNextAcssFailNum() {
		return logMapper.getNextAcssFailNum();
	}
	
	public Integer getNextAcssLogNum() {
		return logMapper.getNextAcssLogNum();
	}
	
	public Integer insertErrorLog(ErrorLogDto dto) {
		return logMapper.insertErrorLog(dto);
	}
	
	public Integer getNextErrorNum() {
		return logMapper.getNextErrorNum();
	}
}
