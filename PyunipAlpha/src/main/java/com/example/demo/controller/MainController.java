package com.example.demo.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.UserDto;
import com.example.demo.service.BoardService;
import com.example.demo.service.UnivCacheService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	@Autowired
	private BoardService boardService;
    @Autowired
    private UnivCacheService univService;
	
	/**
	 * 메인페이지
	 * @throws Exception 
	 */
	@GetMapping("/main/main_form")
	public void main(Model model,HttpSession session) throws Exception {
		if(session.getAttribute("m")!=null && !session.getAttribute("m").equals("")) {
			UserDto m=(UserDto)session.getAttribute("m");
		}		

		HashMap<String, Object> map=new HashMap<>();
		//대학목록 조회
		model.addAttribute("univList", univService.getUnivList(map));
		//편입이야기 조회
		model.addAttribute("talkList", boardService.findBoardByView("0"));
		//편입스터디 조회
		model.addAttribute("studyList", boardService.findBoardStudyByView("1"));
	}
	
	
	@GetMapping("/")
	public String main() {
		return "redirect:/main/main_form";
	}
	
}
