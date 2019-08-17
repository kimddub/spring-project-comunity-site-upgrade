package com.example.demo.controller;

import java.util.List;

import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.Article;
import com.example.demo.dto.ArticleFile;
import com.example.demo.service.ArticleService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	@Autowired
	ArticleService articleService;

	@RequestMapping("/")
	public String showMain() {
		return "redirect:home/main";
	}
	
	@RequestMapping("/home/main")
	public String showMain2(Model model) {
		
		List<Article> highViewArticle = articleService.getArticleByCondition(Maps.of("highViewArticle",true));
		List<Article> mostRepliesArticle = articleService.getArticleByCondition(Maps.of("mostRepliesArticle",true));
		
		model.addAttribute("highViewArticle", highViewArticle);
		model.addAttribute("mostRepliesArticle", mostRepliesArticle);
		
		return "home/main";
	}
	
	@RequestMapping("/error/no_auth")
	public String showError() {
		return "error/no_auth";
	}
	
}
