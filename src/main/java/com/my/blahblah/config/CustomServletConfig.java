package com.my.blahblah.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/js/**")
				.addResourceLocations("classpath:/static/js/");
		registry.addResourceHandler("/assets/**")
				.addResourceLocations("classpath:/static/assets/");
		registry.addResourceHandler("/css/**")
				.addResourceLocations("classpath:/static/css/");
		registry.addResourceHandler("/login/**")
				.addResourceLocations("classpath:/static/login/");
		registry.addResourceHandler("/display/**")
        		.addResourceLocations("file:/c:/uploadFile/"); // 이미지 저장 경로
		
	}
	
}
