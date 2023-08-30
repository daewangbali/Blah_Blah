package com.my.blahblah.servicetest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import com.my.blahblah.dto.CommentsDTO;
import com.my.blahblah.dto.PostsDTO;
import com.my.blahblah.entity.Comments;
import com.my.blahblah.entity.Posts;
import com.my.blahblah.entity.Users;
import com.my.blahblah.repository.CommentsRepository;
import com.my.blahblah.repository.PostsRepository;
import com.my.blahblah.repository.UsersRepository;
import com.my.blahblah.service.CommentsService;
import com.my.blahblah.service.PostsService;
import com.my.blahblah.service.UsersService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@Transactional
public class CommentsServiceTest {
	
	@Autowired
	private CommentsRepository commentsRepository;
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private PostsRepository postsRepository;
	
	@Autowired
	private PostsService postsService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private CommentsService commentsService;

	@Autowired
	private ModelMapper modelMapper;
	
	@Test
	public void testRegister() {
		
		Optional<Posts> postNo = postsRepository.findById(87L);
		
		Posts post = postNo.orElseThrow();
		
		//Optional<Users> result = usersRepository.findByUserId("java");
		
		Users user = usersRepository.findByUserId("java");
		
		CommentsDTO commentsDTO = CommentsDTO.builder()
				.content("댓글service TEST")
				.writer(user)
				.postNo(post)
				.build();
		
		
		log.info("새 댓글 등록 번호 {}",commentsService.register(commentsDTO));
	}
	
	@Test
	public void testList() {
		
		List<CommentsDTO> commentsList = commentsService.allList();
		
		for(CommentsDTO c : commentsList) {
			log.info("comment {} ", c);
		}
		
	}
	
	@Test
	public void testModify() {
		
		log.info("수정 전 {}" , commentsService.readOne(181L));
		
		CommentsDTO commentsDTO = commentsService.readOne(181L);
		
		commentsDTO.setContent("수정Test");
		
		commentsService.modify(commentsDTO);
		log.info("수정 완 {}" , commentsService.readOne(181L));
		
		/*
		 log.info("수정 전 {}" , commentsService.readOne(181L));

		commentsService.modify(181L, "테스트수정수정수정수정수정수정");
		
		log.info("수정 완 {}" , commentsService.readOne(181L));
		
		 */
		
	}
	
	@Test
	public void testDelete() {
		
		commentsService.remove(257L);
		commentsService.remove(258L);
		log.info("삭제완");
		
	}
	
	@Test
	public void testReadOne() {
		CommentsDTO commentDTO = commentsService.readOne(448L);
		
		log.info("DTO read One {}", commentDTO);
	}
	
	@Test
	public void testReadOneByPostNo() {
		
		Optional<Posts> result = postsRepository.findById(201L);
		
		Posts post = result.orElseThrow();
		
		List<CommentsDTO> commentsList = commentsService.commentsByPostNo(201L);
		
		for(CommentsDTO c : commentsList) {
			log.info("comment {} ", c);
		}
		
	}
	
	@Test
	public void testListre() {
		
		List<CommentsDTO> commentsList = commentsService.allList();
		
		for(CommentsDTO c : commentsList) {
			if(c.getPostNo().getPostNo() == 201L) {
				
				if(c.getParent() != null) {
					commentsService.remove(c.getCommentNo());
					log.info("삭제완");
				}
				
			}
			///log.info("comment {} ", c);
		}
		
	}
	
	@Test
	public void testReplyList2() {
		
		Optional<Comments> result = commentsRepository.findById(331L);
		
		Comments comment = result.orElseThrow();
		
		List<Comments> replyList = comment.getChildren();
		
		List<CommentsDTO> collect = replyList.stream()
				.map(replys -> modelMapper.map(replys, CommentsDTO.class))
				.collect(Collectors.toList());
		
		for(CommentsDTO r : collect) {
			log.info("reply {} ", r);
			
		}
		
	}
	
	@Test
	public void testReplyList() {
		
		List<CommentsDTO> collect =commentsService.repliesListByCommentNo(331L);
		
		for(CommentsDTO r : collect) {
			log.info("reply {} ", r);
			
		}
		
	}
	

	@Test
	public void testChildren() {
		
		Optional<Comments> result = commentsRepository.findById(321L);
		
		Comments comment = result.orElseThrow();
		
		List<Comments> commentsList = comment.getChildren();
		/*
		List<CommentsDTO> collect = commentsList.stream()
				.map(comments -> modelMapper.map(comments, CommentsDTO.class))
				.collect(Collectors.toList());
		*/
		for(Comments c : commentsList) {
			log.info("comments {} ", c);
		}
		
	}
	
	@Test
    public void testReply() {
		 Optional<Comments> parentCommentOptional = commentsRepository.findById(283L);
		    Comments parentComment = parentCommentOptional.orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다"));
			//Optional<Users> result = usersRepository.findByUserId("java");
			
			Users user = usersRepository.findByUserId("java");
			log.info("parentComment {} ", parentComment);
		    // 부모 댓글의 정보를 기반으로 대댓글 엔티티 생성
			/*
		    Comments reply = Comments.builder()
		            .content("테스트중")
		            .writer(user)
		            .postNo(parentComment.getPostNo())
		            .parent(parentComment)
		            .build();
		    log.info("reply {} ", reply);
		    */
    }
	
	
	@Test
	public void testCommentList() {
		Optional<Posts> result = postsRepository.findById(224L);
		
		Posts post = result.orElseThrow();
		
		List<Comments> commentsList = commentsRepository.findByPostNoAndParentIsNullOrderByCommentNoAsc(post);
		
		List<Comments> list = new ArrayList<Comments>();
		
		for(int i=0 ; i<commentsList.size() ; i++) {
			list.add(commentsList.get(i));
			List<Comments> replyList = commentsList.get(i).getChildren();
			for(int j=0 ; j<replyList.size(); j++) {
				list.add(replyList.get(j));
			}
		}
		
		List<CommentsDTO> collect = list.stream()
				.map(comments -> modelMapper.map(comments, CommentsDTO.class))
				.collect(Collectors.toList());
				
		for(CommentsDTO c : collect) {
			log.info("comment {} ", c);
		}
		
	}
	
}
