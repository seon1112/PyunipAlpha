package com.example.demo.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.SltDetailInfoDto;
import com.example.demo.dto.TransInfoDto;
import com.example.demo.dto.UnivInfoDto;
import com.example.demo.mapper.UnivMapper;

@Repository
public class UnivRepository {
	@Autowired
	private UnivMapper univMapper;
	
	public List<UnivInfoDto> findApyDtm(){
		return univMapper.findApyDtm();
	}
	
	public List<UnivInfoDto> findUnivNameNum(HashMap<String, Object> map){
        if (map.get("nMaxVCnt") != null) {
            int pageSize = (int) map.get("nMaxVCnt");
            int pageNumber = (int) map.get("nSelectPage");
            int offset = (pageNumber - 1) * pageSize;
            RowBounds rowBounds = new RowBounds(offset, pageSize);
            return univMapper.findUnivNameNum(map, rowBounds);
        } else {
            return univMapper.findUnivNameNum(map, RowBounds.DEFAULT);
        }
	}
	
	public List<TransInfoDto> findDeptNm(){
		return univMapper.findDeptNm();
	}
	public List<TransInfoDto> findTransInfo(HashMap<String, Object> map){
        if (map.get("nMaxVCnt") != null) {
            int pageSize = (int) map.get("nMaxVCnt");
            int pageNumber = (int) map.get("nSelectPage");
            int offset = (pageNumber - 1) * pageSize;
            RowBounds rowBounds = new RowBounds(offset, pageSize);
            return univMapper.findTransInfo(map, rowBounds);
        } else {
            return univMapper.findTransInfo(map, RowBounds.DEFAULT);
        }
	}
	
	public UnivInfoDto findUnivInfo(HashMap<String, Object> map) {
		return univMapper.findUnivInfo(map);
	}
	
	public List<SltDetailInfoDto> findSltInfo(HashMap<String, Object> map) {
        if (map.get("nMaxVCnt") != null) {
            int pageSize = (int) map.get("nMaxVCnt");
            int pageNumber = (int) map.get("nSelectPage");
            int offset = (pageNumber - 1) * pageSize;
            RowBounds rowBounds = new RowBounds(offset, pageSize);
            return univMapper.findSltInfo(map, rowBounds);
        } else {
            return univMapper.findSltInfo(map, RowBounds.DEFAULT);
        }
	}
	
	public List<SltDetailInfoDto> findSltDetailInfo(HashMap<String, Object> map){
		return univMapper.findSltDetailInfo(map);
	}
	public Integer updateUniv(UnivInfoDto univ) {
		return univMapper.updateUniv(univ);
	}
	public Integer insertTransInfo(TransInfoDto dto) {
		return univMapper.insertTransInfo(dto);
	}
	public Integer updateTransInfo(TransInfoDto dto) {
		return univMapper.updateTransInfo(dto);
	}
	public Integer deleteTransInfo(HashMap<String, Object> map) {
		return univMapper.deleteTransInfo(map);
	}
	public Integer insertSltInfo(SltDetailInfoDto dto) {
		return univMapper.insertSltInfo(dto);
	}	
	public Integer insertSltDetailInfo(SltDetailInfoDto dto) {
		return univMapper.insertSltDetailInfo(dto);
	}	
	public Integer updateSltInfo(SltDetailInfoDto dto) {
		return univMapper.updateSltInfo(dto);
	}	
	public Integer updateSltDetailInfo(SltDetailInfoDto dto) {
		return univMapper.updateSltDetailInfo(dto);
	}	
	public Integer deleteSltInfo(HashMap<String, Object> map) {
		return univMapper.deleteSltInfo(map);
	}	
	public Integer deleteSltDetailInfo(HashMap<String, Object> map) {
		return univMapper.deleteSltDetailInfo(map);
	}	
	public TransInfoDto findTransInfoByNum(HashMap<String, Object> map) {
		return univMapper.findTransInfoByNum(map);
	}
	public Integer findNextSltNum() {
		return univMapper.findNextSltNum();
	}
	public Integer findNextSltDetailNum(int SLT_NUM) {
		return univMapper.findNextSltDetailNum(SLT_NUM);
	}
	public Integer getNextTransNum(int UNIV_NUM) {
		return univMapper.getNextTransNum(UNIV_NUM);
	}
}

