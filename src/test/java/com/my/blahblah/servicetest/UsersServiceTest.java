package com.my.blahblah.servicetest;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import com.my.blahblah.dto.UsersDTO;
import com.my.blahblah.entity.Authority;
import com.my.blahblah.entity.Posts;
import com.my.blahblah.entity.Users;
import com.my.blahblah.repository.AuthorityRepository;
import com.my.blahblah.repository.UsersRepository;
import com.my.blahblah.service.UsersService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class UsersServiceTest {
	
	@Autowired
	private UsersService usersService;
	
	
	@Test
	public void testRegister() {
		log.info(usersService.getClass().getName());
		 
		
		UsersDTO usersDTO = UsersDTO.builder()
				.userId("jpa")
				.name("러바오")
				.password("1234")
				.tel("010-7777-3333")
				.build();
		
		//String id = usersService.register(usersDTO);
		
		log.info("register 회원 {}", usersService.register(usersDTO));
	}
	
	@Test
	public void testReadOneByUserId() {
		UsersDTO user = usersService.readOneByUserId("jpa");
		
		log.info("findbyid {}", user);
	}
	
	@Test
	public void testReadOne() {
		UsersDTO user = usersService.readOne(10L);
		
		log.info("findbyid {}", user);
	}
	
	
	@Test
	public void testModify() {
		
		UsersDTO usersDTO = UsersDTO.builder()
				.userId("jpa")
				.name("러바옹")
				.password("1111")
				.tel("010-7733-7733")
				.build();
		
		usersService.modify(usersDTO);
		
		log.info("회원정보 수정완료");
	}
	
	@Test
	public void checkDuplicateId() {
		
		log.info("아이디 중복확인 {}",usersService.checkDuplicateId("java"));
	}
	
	@Test
	public void testDelete() {
		String id = "java1";
		String password = "1234";
		
		usersService.delete(id, password);
		
		log.info("회원삭제 ");
	}
	
	
}
