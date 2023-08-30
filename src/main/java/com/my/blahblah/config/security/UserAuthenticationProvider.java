package com.my.blahblah.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.my.blahblah.dto.UsersDTO;
import com.my.blahblah.entity.Authority;
import com.my.blahblah.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationProvider implements AuthenticationProvider {
	
	private final UsersService usersService;
	
	private final BCryptPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		 
		if(!supports(authentication.getClass())){
			return null;
		}
		
		String id = authentication.getName();
		
		UsersDTO user = usersService.readOneByUserId(id);
		if(user == null){
			throw new UsernameNotFoundException("회원 아이디가 존재하지 않습니다");
		}
		
		String password=(String)authentication.getCredentials();
        if (!passwordEncoder.matches(password, user.getPassword()))//! 비밀번호가 일치하지 않으면  
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        
        
		//사용자 권한 조회
		List<Authority> list = usersService.readAuthority(id);
	
		// 권한이 하나 이상 없으면 자격 증명이 불충분
		if(list.size() == 0){ 
			throw new InsufficientAuthenticationException("권한이 없습니다.");
		}
		
		//탈퇴한 회원시
		if(list.get(0).getRole().equals("ROLE_WITHDRAWAL")){ 
			throw new InsufficientAuthenticationException("존재하지 않는 회원입니다.");
		}
		
        
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();		
				
		Authentication auth = new UsernamePasswordAuthenticationToken(user, password, authorities);
		log.debug("MemberAuthenticationProvider 인증처리완료:{}",auth);
		return auth;	
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
