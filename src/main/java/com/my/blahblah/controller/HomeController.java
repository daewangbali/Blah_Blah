package com.my.blahblah.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.my.blahblah.dto.UsersDTO;
import com.my.blahblah.entity.Authority;
import com.my.blahblah.entity.Users;
import com.my.blahblah.service.PostsService;
import com.my.blahblah.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final UsersService usersService;
	
	@RequestMapping(value = {"/","home"})
	public String Home(@AuthenticationPrincipal Users user, Authentication authentication, Model model, HttpServletRequest request){
		
		if(authentication!=null) {
			log.info("Home: 인증받은 사용자 {} ",authentication.getPrincipal());
			
			HttpSession session = request.getSession();
			session.setAttribute("userDTO", authentication.getPrincipal());
			
			Object principal = authentication.getPrincipal();
			UsersDTO userDTO = (UsersDTO) principal;
			List<Authority> role = usersService.readAuthority(userDTO.getUserId());
			session.setAttribute("userRoles", role);
		}else {			
			log.info("Home: 인증받지 않은 사용자");	
		}
			model.addAttribute("userDTO", user);
			return "index";
				
	}
	
	@RequestMapping("accessDeniedView")
	public String accessDeniedView() {
		return "auth/accessDeniedView";
	}
	
}
