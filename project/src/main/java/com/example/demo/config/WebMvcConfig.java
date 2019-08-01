package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	@Qualifier("NeedLoginInterceptor")
	private HandlerInterceptor needLoginInterceptor;

	@Autowired
	@Qualifier("BaseInterceptor")
	private HandlerInterceptor baseInterceptor;

	@Autowired
	@Qualifier("NeedLogoutInterceptor")
	private HandlerInterceptor needLogoutInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(baseInterceptor).addPathPatterns("/**").excludePathPatterns("/resource/**");
		
		registry.addInterceptor(needLoginInterceptor).addPathPatterns("/**")
		.excludePathPatterns("/", "/home/main")
		.excludePathPatterns("/article/list", "/article/detail")
		.excludePathPatterns("/article/downloadFile", "/article/showImg", "/common/**")
		.excludePathPatterns("/article/getAllReplies")
		.excludePathPatterns("/member/logout", "/member/join", "/member/confirm", "/member/doJoin")
		.excludePathPatterns("/member/login", "/member/doLogin", "/member/findLoginId", "/member/findLoginPw")
		.excludePathPatterns("/resource/**");
		
		// needLogout
		registry.addInterceptor(needLogoutInterceptor).addPathPatterns("/member/login")
				.addPathPatterns("/member/doLogin").addPathPatterns("/member/join").addPathPatterns("/member/doJoin");

		// 이렇게만 해도 아래처럼 작동됨
		// registry.addInterceptor(specialInterceptor).addPathPatterns("/**");
	}

}
