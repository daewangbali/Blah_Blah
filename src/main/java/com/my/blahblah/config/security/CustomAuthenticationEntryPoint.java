package com.my.blahblah.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	
	public CustomAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }
     
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    	
    	String ajaxHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");    	
        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);             
        if (isAjax) { 
              response.sendError(HttpServletResponse.SC_FORBIDDEN, "사용자 인증이 필요합니다");
        } else { 
            super.commence(request, response, authException);
        }
        
    }
}
