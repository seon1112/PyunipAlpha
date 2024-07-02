package com.example.demo.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SltDetailInfoDto;
import com.example.demo.dto.TransInfoDto;
import com.example.demo.dto.UnivInfoDto;
import com.example.demo.repository.UnivRepository;

@Service
public class UnivService {
	@Autowired
	private UnivRepository univRpt;
	
	public List<UnivInfoDto> findApyDtm(){
		return univRpt.findApyDtm();
	}
	
	public List<UnivInfoDto> findUnivNameNum(HashMap<String, Object> map){
		 return univRpt.findUnivNameNum(map);
	}
	
	public List<TransInfoDto> findDeptNm(){
		return univRpt.findDeptNm();
	}
	public List<TransInfoDto> findTransInfo(HashMap<String, Object> map){
		return univRpt.findTransInfo(map);
	}
	
	public UnivInfoDto findUnivInfo(HashMap<String, Object> map) {
		return univRpt.findUnivInfo(map);
	}
	
	public List<SltDetailInfoDto> findSltInfo(HashMap<String, Object> map) {
		return univRpt.findSltInfo(map);
	}
	
	public List<SltDetailInfoDto> findSltDetailInfo(HashMap<String, Object> map){
		return univRpt.findSltDetailInfo(map);
	}
	public Integer updateUniv(UnivInfoDto univ) {
		return univRpt.updateUniv(univ);
	}
	public Integer insertTransInfo(TransInfoDto dto) {
		return univRpt.insertTransInfo(dto);
	}
	public Integer updateTransInfo(TransInfoDto dto) {
		return univRpt.updateTransInfo(dto);
	}
	public Integer deleteTransInfo(HashMap<String, Object> map) {
		return univRpt.deleteTransInfo(map);
	}
	public Integer insertSltInfo(SltDetailInfoDto dto) {
		return univRpt.insertSltInfo(dto);
	}	
	public Integer insertSltDetailInfo(SltDetailInfoDto dto) {
		return univRpt.insertSltDetailInfo(dto);
	}	
	public Integer updateSltInfo(SltDetailInfoDto dto) {
		return univRpt.updateSltInfo(dto);
	}	
	public Integer updateSltDetailInfo(SltDetailInfoDto dto) {
		return univRpt.updateSltDetailInfo(dto);
	}	
	public Integer deleteSltInfo(HashMap<String, Object> map) {
		return univRpt.deleteSltInfo(map);
	}	
	public Integer deleteSltDetailInfo(HashMap<String, Object> map) {
		return univRpt.deleteSltDetailInfo(map);
	}	
	public TransInfoDto findTransInfoByNum(HashMap<String, Object> map) {
		return univRpt.findTransInfoByNum(map);
	}
	public Integer findNextSltNum() {
		return univRpt.findNextSltNum();
	}
	public Integer findNextSltDetailNum(int SLT_NUM) {
		return univRpt.findNextSltDetailNum(SLT_NUM);
	}
	public Integer getNextTransNum(int UNIV_NUM) {
		return univRpt.getNextTransNum(UNIV_NUM);
	}
}

