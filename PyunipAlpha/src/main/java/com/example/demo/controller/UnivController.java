package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.SltDetailInfoDto;
import com.example.demo.dto.TransInfoDto;
import com.example.demo.dto.UnivInfoDto;
import com.example.demo.service.CalendarService;
import com.example.demo.service.LogService;
import com.example.demo.service.UnivCacheService;
import com.example.demo.service.UnivService;
import com.example.demo.util.DateUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UnivController {
	@Autowired
	private CalendarService calendarService;
	@Autowired
	private DateUtils dateService;
	@Autowired
	private UnivService univService;
    @Autowired
    private UnivCacheService univCacheService;
    @Autowired
    private LogService errorService;
	
	/**
	 * 편입 일정 반환
	 */	
	@GetMapping("/calendarEvent")
	@ResponseBody
	public List<Map<String, Object>> calendarEvent(){
		List<UnivInfoDto> list=univCacheService.findApyDtmList();
		List<Map<String, Object>> mapList=calendarService.getEventList(list);
		
		return mapList;
	}
	
	/**
	 * 상세모집요강
	 * @param UNIV_NUM 대학_일련번호
	 * @throws Exception 
	 */
	@GetMapping("/univ/univ_apply_detail")
	public void univApplyDetail(@RequestParam int UNIV_NUM, 
								Model model,HttpServletRequest request, HttpSession session) throws Exception {
		HashMap<String, Object> map=new HashMap<>();
		map.put("UNIV_NUM", UNIV_NUM);		
		try {
			model.addAttribute("s", univService.findUnivInfo(map));
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "오류가 발생했습니다. 관리자에게 문의해주세요.");
		}
	}
	
	/**
	 * 편입 학과 정보
	 * @param MAJOR_NM 모집단위명
	 */	
	@GetMapping("/univ/univ_major")
	public void univMajor(@RequestParam(required = false)String MAJOR_NM , Model model) {
		if(MAJOR_NM != null && MAJOR_NM != "") {
			model.addAttribute("MAJOR_NM", MAJOR_NM);
		}
		HashMap<String, Object> map=new HashMap<>();
		model.addAttribute("univList", univCacheService.getUnivList(map));
		model.addAttribute("majorList", univService.findDeptNm());
	}
	
	/**
	 * 최신모집요강
	 */		
	@GetMapping("/univ/univ_apply")
	public void univApply() {
	}
	
	/**
	 * 상세 모집 요강 : 편입 일정 및 비고
	 * @param UNIV_NUM 대학_일련번호
	 * @param SLT 전형 방법
	 * @throws Exception 
	 */		
	@GetMapping("/univ/apply_dtm_form")
	public String ApplyDtmForm(@RequestParam int UNIV_NUM,
							   @RequestParam String SLT,
							   @RequestParam int nSelect,
							   Model model,HttpSession session,HttpServletRequest request) throws Exception {
		HashMap<String, Object> map=new HashMap<>();
		map.put("UNIV_NUM", UNIV_NUM);		
		
		try {
			UnivInfoDto dto=univService.findUnivInfo(map);
			
			dto.setAPY_ST_DTM(dateService.DateConversion3(dto.getAPY_ST_DTM()));
			dto.setAPY_ED_DTM(dateService.DateConversion3(dto.getAPY_ED_DTM()));
			dto.setSLT_DATE(dateService.DateConversion(dto.getSLT_DATE()));
			
			String suc_apy_dtm=dto.getSUC_APY_DTM();
			String intv_dtm=dto.getINTV_DTM();
			String doc_sub_dtm = dto.getDOC_SUB_DTM();
			int size =0;
			int size2 = 0;
			
			//합격자발표
			if(suc_apy_dtm != null) {
				List<String> suc = dateService.GetDateList2(suc_apy_dtm);
				size=suc.size();
				model.addAttribute("suc", suc);
			}
			
			//면접일정
			if(intv_dtm != null) {
				List<String> intv =dateService.GetDateList2(intv_dtm);
				size2=intv.size();
				
				model.addAttribute("intv", intv);
			}
			
			//서류제출
			if(doc_sub_dtm != null) {
				List<String> doc = dateService.GetDateList2(doc_sub_dtm);
				model.addAttribute("doc", doc);
			}
			
			model.addAttribute("size", size);
			model.addAttribute("size2", size2);
			model.addAttribute("s", dto);
			
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "오류가 발생했습니다. 관리자에게 문의해주세요.");
		}
		return "forward:/WEB-INF/views/univ/apply_dtm_form.jsp";
	}
	
	/**
	 * 상세모집요강 : 계열, 대학/학부, 모집단위, 모집인원, 지원인원, 경쟁률, 전형
	 * @param UNIV_NUM 대학_일련번호
	 * @param SLT 전형 방법 0 일반평입 ,1 학사편입, 2 특별전형, 4 전체
	 */	
	@GetMapping("/univ/apply_size_form")
	public String ApplySizeForm(@RequestParam int UNIV_NUM, 
								@RequestParam String SLT,
								@RequestParam int nSelect,
								Model model) {
		HashMap<String, Object> map=new HashMap<>();
		map.put("UNIV_NUM", UNIV_NUM);
		
		if(!SLT.equals("4") && SLT!=null) {
			if(SLT.equals("2")) {
				map.put("SLT_WAY_2", SLT);
			}
			map.put("SLT_WAY", SLT);
		}
		
		List<TransInfoDto> list=univService.findTransInfo(map);
		int nMaxRecordCnt=list.size();
		int nMaxVCnt=10;
		
		map.put("nSelectPage", nSelect);
		map.put("nMaxVCnt",nMaxVCnt);
		list=univService.findTransInfo(map);
		
		model.addAttribute("list", list);
		model.addAttribute("nMaxVCnt", nMaxVCnt);
		model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);		
		model.addAttribute("slt", SLT);
		
		return "forward:/WEB-INF/views/univ/apply_size_form.jsp";
	}
	
	/**
	 * 상세모집요강 : 전형방법, 지원자격, 진행 요소 별 반영 비율 및 배점
	 * @param UNIV_NUM 대학_일련번호
	 * @param SLT 전형 방법
	 * @throws Exception 
	 */		
	@GetMapping("/univ/apply_select_form")
	public String ApplySelectForm(@RequestParam int UNIV_NUM, 
								  @RequestParam String SLT,
								  @RequestParam int nSelect,
								  Model model,HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map=new HashMap<>();
		map.put("UNIV_NUM", UNIV_NUM);		
		map.put("OPEN_YN", "Y");
		
		try {
			List<SltDetailInfoDto> list=univService.findSltInfo(map);
			List<SltDetailInfoDto> list2 = new ArrayList<>();
			
			for(SltDetailInfoDto dto :list) {
				HashMap<String, Object> map2=new HashMap<>();
				map2.put("SLT_NUM", dto.getSLT_NUM());
				
				List<SltDetailInfoDto> detail=univService.findSltDetailInfo(map2);
				int size=detail.size();
				dto.setSize(size);
				
				for(int i=0;i < size;i++) {
					SltDetailInfoDto dto2=detail.get(i);
					
					if(dto2.getSLT_STEP().indexOf("2") == -1) {
						dto.setSLT_STEP(dto2.getSLT_STEP());
						dto.setENG_PC(dto2.getENG_PC());
						dto.setMATH_PC(dto2.getMATH_PC());
						dto.setMAJOR_PC(dto2.getMAJOR_PC());
						dto.setPREV_GRADE(dto2.getPREV_GRADE());
						dto.setINTV_PC(dto2.getINTV_PC());
						dto.setRECOG_ENG_PC(dto2.getRECOG_ENG_PC());
						dto.setWHATEVER(dto2.getWHATEVER());
						dto.setWHATEVER_NOTE(dto2.getWHATEVER_NOTE());
					}else{
						dto.setSLT_STEP2(dto2.getSLT_STEP());
						dto.setENG_PC2(dto2.getENG_PC());
						dto.setMATH_PC2(dto2.getMATH_PC());
						dto.setMAJOR_PC2(dto2.getMAJOR_PC());
						dto.setPREV_GRADE2(dto2.getPREV_GRADE());
						dto.setINTV_PC2(dto2.getINTV_PC());
						dto.setRECOG_ENG_PC2(dto2.getRECOG_ENG_PC());
						dto.setWHATEVER2(dto2.getWHATEVER());
						dto.setWHATEVER_NOTE2(dto2.getWHATEVER_NOTE());
					}
					
				}
				
				list2.add(dto);
			}
			model.addAttribute("list", list2);
			model.addAttribute("size", list2.size());
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "오류가 발생했습니다. 관리자에게 문의해주세요.");
		}
		
		return "forward:/WEB-INF/views/univ/apply_select_form.jsp";
	}
	
	/**
	 * 편입 학과 정보 리스트
	 * @param YEAR      연도
	 * @param SERIES    계열
	 * @param UNIV_NAME 대학이름
	 * @param MAJOR_NM  전공이름
	 * @param SLT_WAY   전형 방법 0 일반평입 ,1 학사편입, 2 특별전형
	 */		
	@GetMapping("/univ/major_list")
	public String univMajorList(@RequestParam int nSelect,
								@RequestParam String YEAR,
								@RequestParam String SERIES,
								@RequestParam String UNIV_NAME,
								@RequestParam String MAJOR_NM,
								@RequestParam String SLT_WAY,
								Model model) {
		HashMap<String, Object> map=new HashMap<>();
		map.put("YEAR", YEAR);
		map.put("SERIES", SERIES);
		map.put("UNIV_NAME", UNIV_NAME);
		map.put("MAJOR_NM", MAJOR_NM);
		
		if(!SLT_WAY.equals("4") && SLT_WAY!=null) {
			if(SLT_WAY.equals("2")) {
				map.put("SLT_WAY_2", SLT_WAY);
			}
			map.put("SLT_WAY", SLT_WAY);
		}
		
		
		List<TransInfoDto> list=univService.findTransInfo(map);
		int nMaxRecordCnt=list.size();
		int nMaxVCnt=10;
		
		map.put("nSelectPage", nSelect);
		map.put("nMaxVCnt",nMaxVCnt);
		list=univService.findTransInfo(map);
		
		model.addAttribute("list", list);
		model.addAttribute("nMaxVCnt", nMaxVCnt);
		model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);		
		
		return "forward:/WEB-INF/views/univ/major_list.jsp";
	}
}
