package com.my.blahblah.servicetest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.my.blahblah.dto.PostsDTO;
import com.my.blahblah.entity.Posts;
import com.my.blahblah.repository.PostsRepository;
import com.my.blahblah.service.PostsService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class PostsServiceTest{
	
	@Autowired
	private PostsRepository postsRepository;
	
	@Autowired
	private PostsService postsService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Test
	public void testList() {
		
		List<Posts> postsList = postsRepository.findAll(Sort.by(Sort.Direction.DESC, "postNo"));
		
		List<PostsDTO> collect = postsList.stream()
				.map(posts -> modelMapper.map(posts, PostsDTO.class))
				.collect(Collectors.toList());
		
		for(PostsDTO p : collect) {
			log.info("post {} ", p);
		}
		
	}
	
	@Test
	public void testModify() {
		
		Optional<Posts> result = postsRepository.findById(64L);
		
		Posts post = result.orElseThrow();
		
		post.update("test수정","수정수정수정");
		
		postsRepository.save(post);
		
		/*
		PostsDTO postDTO = PostsDTO.builder()
				.postNo(64L)
				.title("test수정")
				.content("test수정수정")
				.build();
		
		postsService.modify(postDTO);
		*/
		Optional<Posts> result2 = postsRepository.findById(64L);
		
		log.info("post {} ", result2);
		
	}
	
	@Test
	public void testDelete() {
		
		postsService.remove(344L);
		postsService.remove(343L);
		postsService.remove(342L);
		log.info("삭제완");
		
	}
	
	@Test
	public void testListDelete() {
		
		List<Posts> postsList = postsRepository.findAll(Sort.by(Sort.Direction.DESC, "postNo"));
		
		List<PostsDTO> collect = postsList.stream()
				.map(posts -> modelMapper.map(posts, PostsDTO.class))
				.collect(Collectors.toList());
		
		for(PostsDTO p : collect) {
			postsService.remove(p.getPostNo());
		}
		
	}
	
}
