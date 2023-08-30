package com.my.blahblah.jpatest;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.my.blahblah.entity.Posts;
import com.my.blahblah.entity.Users;
import com.my.blahblah.repository.PostsRepository;
import com.my.blahblah.repository.UsersRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class PostsTest {
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private PostsRepository postsRepository;
	
	@Test
	void contextLoads() {
		log.info(postsRepository.getClass().getName());// Proxy 구현체가 실행
	}
	
	
	@Test
	public void insertPost() {
		//Users user = usersRepository.findById("java");

		Users user = usersRepository.findByUserId("java");
		int num = 1;
		postsRepository.save(new Posts("테스트 제목"+ num++ ,"테스트 내용" + num , user));
		
		//postsRepository.save(Posts.builder().title("테스트 제목 입니다").content("테스트 내용내용내용 입니다").user(usersRepository.findById("java")).createdDate(LocalDateTime.now()));
		// 게시물수 조회
		log.info("posts count {}", postsRepository.count());
	}
	
	@Test
	void findAll() {
		// READ -> select : findAll() 전체리스트 조회
		List<Posts> list = postsRepository.findAll(Sort.by(Sort.Direction.DESC, "postNo"));
		for (Posts p : list)
			log.info("post {} ", p);
	}
	
	@Test
	void findById() {
		// pk로 검색
		Optional<Posts> p= postsRepository.findById(21L);
		log.info("postno 21 exist {}", p.get());
		// Optional get() 메서드는 존재하지 않으면 NoSuchElementException 발생시킨다
	}
	
	@Test
	void methodJpaTest() {
		/*
		 * 직접 정의한 메서드 테스트 : PostsRepository 를 확인해본다 
		 */
		// log.info("findByAddress {}",memberRepository.findByAddress("캐나다"));
		// Like Test
		log.info("findByTitleLike {}",postsRepository.findByTitleLike("%test%"));
		 //log.info("findByNameLikeAndAddressLike {}",memberRepository.findByNameLikeAndAddressLike("%트%","%다%"));
	}
	
	
	@Test
	void pagenationTest() {
		
		Pageable pageable = PageRequest.of(0,3);

        Page<Posts> result = postsRepository.findAll(pageable);
		
		
		log.info("total count: {}", result.getTotalElements());
		log.info("total page: {}", result.getTotalPages());
		log.info("page number: {}", result.getNumber());
		log.info("page size: {}", result.getSize());
		/*
		List<Posts> todoList = result.getContent();
		
		for (Posts p : todoList)
			log.info("post {} ", p);
		*/
		//todoList.forEach(posts -> log.info(posts));
	}
	
	
}