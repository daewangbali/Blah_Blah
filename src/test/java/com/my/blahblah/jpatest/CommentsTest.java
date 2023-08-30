package com.my.blahblah.jpatest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.my.blahblah.dto.CommentsDTO;
import com.my.blahblah.entity.Comments;
import com.my.blahblah.entity.Posts;
import com.my.blahblah.entity.Users;
import com.my.blahblah.repository.CommentsRepository;
import com.my.blahblah.repository.PostsRepository;
import com.my.blahblah.repository.UsersRepository;
import com.my.blahblah.service.PostsService;
import com.my.blahblah.service.UsersService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@Transactional
public class CommentsTest {
	
	@Autowired
	private CommentsRepository commentsRepository;
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private PostsRepository postsRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Test
	void insertComment() {
		/*
		Posts post = Posts.builder().postNo(87L).build();
		Optional<Users> result = usersRepository.findById("java");
		
		Users user = result.orElseThrow();
		// CREATE -> insert : save ( 기본 제공 메서드 )
		log.info("insert comment {}", commentsRepository.save(new Comments("comment testtest",user,post)));
		*/
	}
	
	@Test
	public void testInsert() {
		/*
		Optional<Posts> postNo = postsRepository.findById(87L);
		
		Posts post = postNo.orElseThrow();
		
		Optional<Users> result = usersRepository.findById("java");
		
		Users user = result.orElseThrow();
		
		Comments comments = Comments.builder()
				.postNo(post)
				.content("댓글 test!")
				.writer(user)
				.build();
		
		commentsRepository.save(comments);
		
		log.info("성공");
				*/
	}
	
	@Test
	void findAll() {
		// READ -> select : findAll() 전체리스트 조회
		List<Comments> list = commentsRepository.findAll(Sort.by(Sort.Direction.DESC, "commentNo"));
		for (Comments c : list)
			log.info("comment {} ", c);
	}
	
	@Test
	public void testList() {
		
		List<Comments> commentsList = commentsRepository.findAll(Sort.by(Sort.Direction.DESC, "commentNo"));
		
		List<CommentsDTO> collect = commentsList.stream()
				.map(comments -> modelMapper.map(comments, CommentsDTO.class))
				.collect(Collectors.toList());
		
		for(CommentsDTO c : collect) {
			log.info("comments {} ", c);
		}
		
	}
	
	@Test
	public void testModify() {
		
		Optional<Comments> result = commentsRepository.findById(25L);
		
		Comments comment = result.orElseThrow();
		
		comment.update("test수정");
		
		commentsRepository.save(comment);
		
		/*
		PostsDTO postDTO = PostsDTO.builder()
				.postNo(64L)
				.title("test수정")
				.content("test수정수정")
				.build();
		
		postsService.modify(postDTO);
		*/
		Optional<Comments> result2 = commentsRepository.findById(25L);
		
		log.info("comment {} ", result2);
		
	}
	
	@Test
	public void testListByPostNo() {
		
		
		Optional<Posts> result = postsRepository.findById(224L);
		
		Posts post = result.orElseThrow();
		
		List<Comments> commentsList = commentsRepository.findByPostNo(post,  Sort.by(Sort.Direction.ASC, "commentNo"));
		
		List<CommentsDTO> collect = commentsList.stream()
				.map(comments -> modelMapper.map(comments, CommentsDTO.class))
				.collect(Collectors.toList());
		/*
		
		Optional<Posts> result = postsRepository.findById(224L);
		
		Posts post = result.orElseThrow();
		
		List<Comments> commentsList = commentsRepository.findCommentHierarchyByPostNo(post, Sort.by(Sort.Direction.ASC, "commentNo"));

		List<CommentsDTO> collect = commentsList.stream()
				.map(comments -> modelMapper.map(comments, CommentsDTO.class))
				.collect(Collectors.toList());
		*/
		
		for(CommentsDTO c : collect) {
			log.info("comments {} ", c);
		}
		
	}
	
	@Test
	public void test() {
		
		Optional<Posts> result = postsRepository.findById(224L);
		
		Posts post = result.orElseThrow();
		
		List<Comments> commentsList = commentsRepository.findByPostNoAndParentIsNullOrderByCommentNoAsc(post);

		List<CommentsDTO> collect = commentsList.stream()
				.map(comments -> modelMapper.map(comments, CommentsDTO.class))
				.collect(Collectors.toList());
		
		for(CommentsDTO c : collect) {
			log.info("comments {} ", c);
		}
		
	}
	
	@Test
	public void testt() {
		
		List<Comments> commentsList = commentsRepository.findByParentCommentNoOrderByCommentNoAsc(321L);

		List<CommentsDTO> collect = commentsList.stream()
				.map(comments -> modelMapper.map(comments, CommentsDTO.class))
				.collect(Collectors.toList());
		
		for(CommentsDTO c : collect) {
			log.info("comments {} ", c);
		}
		
	}
	
	@Test
	public void testDelete() {
		
		Comments comment = commentsRepository.findById(347L).orElse(null);
		
		//자식 댓글 존재시
		if(comment.getChildren().size() > 0) {
			comment.updateDeleteStatus();
			log.info("상태변환완");
		}else {
			Comments parent = comment.getParent();
			log.info("부모댓글번호 {}",parent.getCommentNo());
            if (parent != null) {
                //parent.removeChildComment(comment);
                commentsRepository.delete(comment);
                log.info("삭제완");
            }
		}
		
		
		
	}
	
}
	