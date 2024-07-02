package com.example.demo.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.example.demo.dto.SltDetailInfoDto;
import com.example.demo.dto.TransInfoDto;
import com.example.demo.dto.UnivInfoDto;

@Mapper
public interface UnivMapper {
	List<UnivInfoDto> findApyDtm();
	List<UnivInfoDto> findUnivNameNum(HashMap<String, Object> map, RowBounds rowBounds);
	UnivInfoDto findUnivInfo(HashMap<String, Object> map);
	List<TransInfoDto> findDeptNm();
	List<TransInfoDto> findTransInfo(HashMap<String, Object> map, RowBounds rowBounds);
	List<SltDetailInfoDto> findSltInfo(HashMap<String, Object> map, RowBounds rowBounds);
	List<SltDetailInfoDto> findSltDetailInfo(HashMap<String, Object> map);
	Integer updateUniv(UnivInfoDto univ);
	Integer insertTransInfo(TransInfoDto dto);
	Integer updateTransInfo(TransInfoDto dto);
	Integer deleteTransInfo(HashMap<String, Object> map);
	Integer insertSltInfo(SltDetailInfoDto dto);
	Integer insertSltDetailInfo(SltDetailInfoDto dto);
	Integer updateSltInfo(SltDetailInfoDto dto) ;
	Integer updateSltDetailInfo(SltDetailInfoDto dto);
	Integer deleteSltInfo(HashMap<String, Object> map);
	Integer deleteSltDetailInfo(HashMap<String, Object> map);
	TransInfoDto findTransInfoByNum(HashMap<String, Object> map);
	Integer findNextSltNum();
	Integer findNextSltDetailNum(int SLT_NUM);
	Integer getNextTransNum(int UNIV_NUM);
}
