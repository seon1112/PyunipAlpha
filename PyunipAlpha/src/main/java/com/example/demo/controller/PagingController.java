package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PagingController {
	int nMaxHCnt=5; // 인덱스에 몇번까지 보여질지 12345
	int nSelectPage; //현재 페이지
	int nMaxPage=1; //총페이지
	int nCurStartPage;    //인덱스번호의첫수
	int nRealMaxHPageCnt; //최대 인덱스 몇개
	
	@GetMapping("/paging")
	public String paging(@RequestParam(defaultValue = "1") String nSelect,
						 @RequestParam(defaultValue = "1") int nMaxRecordCnt,
						 @RequestParam(defaultValue = "1") int nMaxVCnt,
						 Model model) {
		//nMaxRecordCnt 전체 레코드 수 
		//nMaxVCnt 테이블에 보여질 레코드 개수 16개?
		//nSelectPage = (nSelectPage+nMaxVCnt-1)/nMaxVCnt; //페이지 번호
		
		nSelectPage=Integer.parseInt(nSelect); //페이지 번호
		nMaxPage=(int)Math.ceil((double)nMaxRecordCnt/nMaxVCnt);
		if(nMaxPage == 0) {
			nMaxPage=1;
		}
		nCurStartPage=((nSelectPage-1)/nMaxHCnt)*nMaxHCnt+1; //인덱스번호의첫수
		nRealMaxHPageCnt=nMaxPage-nCurStartPage+1; //최대 인덱스 몇개
		
		if(nRealMaxHPageCnt>nMaxHCnt) {
			nRealMaxHPageCnt=nMaxHCnt;
		}
		int nextPage =( (int)Math.ceil((double)nMaxPage/ nMaxHCnt)-1)*nMaxHCnt +1;
		
		model.addAttribute("nRealMaxHPageCnt", nRealMaxHPageCnt);
		model.addAttribute("nSelectPage", nSelectPage);
		model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
		model.addAttribute("nCurStartPage", nCurStartPage);
		model.addAttribute("nMaxVCnt", nMaxVCnt);
		model.addAttribute("nMaxHCnt", nMaxHCnt);
		model.addAttribute("nMaxPage", nMaxPage);
		model.addAttribute("nextPage",nextPage );
		return "forward:/WEB-INF/views/inc/paging.jsp";
	}
}
