package com.my.blahblah.jpatest;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.blahblah.entity.Posts;
import com.my.blahblah.entity.Users;
import com.my.blahblah.repository.UsersRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class UsersTest {
	@Autowired
	private UsersRepository usersRepository;
	
	@Test
	void contextLoads() {
		log.info(usersRepository.getClass().getName());// Proxy 구현체가 실행
	}

	/*
	 * JPA에서 기본으로 제공하는 메서드를 테스트 해본다
	 * https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/
	 * data/jpa/repository/JpaRepository.html
	 */
	@Test
	void insertUser() {
		// CREATE -> insert : save ( 기본 제공 메서드 )
		Users user = Users.builder()
				.userId("lovefubao")
				.password("1234")
				.name("푸바오")
				.tel("010-7333-7777")
				.build();
		usersRepository.save(user);
		log.info("insert member ");
	}

	@Test
	void userCount() {
		// 총 회원수
		//System.out.println("총 회원수 : " +usersRepository.count());
		log.info("총 회원수 {}명", usersRepository.count());
	}

	@Test
	void existsById() {
		// pk 존재유무
		log.info("id 존재유무 {}", usersRepository.existsByUserId("java2"));
	}

	@Test
	void findAll() {
		//READ -> select : findAll() 전체리스트 조회
		List<Users> list = usersRepository.findAll();
		for (Users u : list)
			log.info("user {} ", u);
	}
	
	@Test
	void findByUserId() {
		// pk로 검색
		Users m = usersRepository.findByUserId("java");
		log.info("jpa id member {}", m);
		// Optional get() 메서드는 존재하지 않으면 NoSuchElementException 발생시킨다
	}
	@Test
	void updateUser() {
		// 객체의 속성값을 변경해서 save 호출하면 update가 된다
		//Optional<Users> result = usersRepository.findByUserId("jpa");
		Users user = usersRepository.findByUserId("java");
		
		//Users user = usersRepository.findById("jpa").get();
		
		log.info("update 전 정보 {}" , user);
		user.updateUser("0000","아이바오", "010-7777-3333");
		usersRepository.save(user);
		log.info("update후 정보 {}" , usersRepository.findByUserId("java"));
	}
	
	@Test
	void deleteUserById() {		
		usersRepository.deleteById(6L);
		log.info("delete result jpa id member {}",usersRepository.existsByUserId("java2"));		
	}
	
	@Test
	void findAllDelete() {
		//READ -> select : findAll() 전체리스트 조회
		List<Users> list = usersRepository.findAll();
		int count = 0;
		for (Users u : list)
			if(u.getUserNo() > 5) {
				usersRepository.deleteById(u.getUserNo());
				log.info("user 삭제 " + count++);
			}
			
	}
	
	
}