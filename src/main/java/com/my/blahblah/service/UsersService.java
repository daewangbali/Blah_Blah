package com.my.blahblah.service;

import java.util.List;

import com.my.blahblah.dto.UsersDTO;
import com.my.blahblah.entity.Authority;

public interface UsersService {
	
	Long register(UsersDTO usersDTO);
	//boolean register(UsersDTO usersDTO);
	
	UsersDTO readOneByUserId(String id);
	
	UsersDTO readOne(Long userNo);
	
	void modify(UsersDTO usersDTO);
	
	void delete(String userId, String password);
	
	//id 중복확인
	boolean checkDuplicateId(String id);
	
	//비밀번호 체크
	boolean checkPassword(String id, String password);
	
	//권한체크
	List<Authority> readAuthority(String userId);
	
}
