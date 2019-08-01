package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

	@RequestMapping("/")
	public String showMain() {
		// 메인페이지 따로??
		return "redirect:home/main";
	}
	
	@RequestMapping("/home/main")
	public String showMain2() {
		// 메인페이지 따로??
		return "home/main";
	}
	
	@RequestMapping("/error/no_auth")
	public String showError() {
		return "error/no_auth";
	}
	
}
