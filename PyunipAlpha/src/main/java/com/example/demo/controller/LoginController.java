package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
	
	/**
	 * 로그인
	 */
	@GetMapping("/main/login_form")
	public void login(@RequestParam(value = "error", required = false) String error,
									Model model, HttpServletRequest request) {
		model.addAttribute("error", error);
		/* 로그인 성공 시 이전 페이지로 이동 */
		String uri = request.getHeader("Referer");		
		if (uri==null) {
			uri = "http://pyunipalpha.shop/main/main_form";
			request.getSession().setAttribute("prevPage", uri);
		}else {
			if(uri.contains("update_pwd")|| uri.contains("signUp_form")||uri.contains("main/login")) {
				uri = "http://pyunipalpha.shop/main/main_form";
			}
			// 이전 url 정보 담기
			request.getSession().setAttribute("prevPage", uri);
		}		
	}
	

	/**
	 * 에러처리
	 */
	@RequestMapping("/loginError")
	public ModelAndView loginError(@RequestParam(value = "errorM", required = false) String errorM,
	                                RedirectAttributes redirectAttributes) {
	    ModelAndView mav = new ModelAndView("redirect:/main/login_form");
	    redirectAttributes.addAttribute("error", errorM);
	    return mav;
	}	
	
}
