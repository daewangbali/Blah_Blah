package com.my.blahblah.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.my.blahblah.dto.CommentsDTO;
import com.my.blahblah.dto.PostsDTO;
import com.my.blahblah.entity.Comments;
import com.my.blahblah.entity.Posts;
import com.my.blahblah.repository.CommentsRepository;
import com.my.blahblah.repository.PostsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CommentsServiceImpl implements CommentsService {
	
	private final ModelMapper modelMapper;
	
	private final CommentsRepository commentsRepository;
	
	private final PostsRepository postsRepository;
	
	//댓글 등록
	@Override
	public Long register(CommentsDTO commentsDTO) {
		
		commentsDTO.setIsDeleted(false);
		Comments comment = modelMapper.map(commentsDTO, Comments.class);
		Long commentNo = commentsRepository.save(comment).getCommentNo();
		
		Optional<Comments> result = commentsRepository.findById(commentNo);
		Comments reply = result.orElseThrow();
		
		if(commentsDTO.getParent() != null) {
			comment.getChildren().add(reply);
		}
		
		return commentNo;
	}

	//댓글 한개 조회
	@Override
	public CommentsDTO readOne(Long commentNo) {

		Optional<Comments> result = commentsRepository.findById(commentNo);
		Comments comment = result.orElseThrow();
		
		CommentsDTO commentsDTO = modelMapper.map(comment, CommentsDTO.class);
		
		return commentsDTO;
	}
	
	//댓글 수정
	@Override
	public void modify(CommentsDTO commentsDTO) {
		
		Optional<Comments> result = commentsRepository.findById(commentsDTO.getCommentNo());
		
		Comments comment = result.orElseThrow();
		
		comment.update(commentsDTO.getContent() );
		
		commentsRepository.save(comment);
		
	}

	//댓글 삭제
	@Override
	public void remove(Long commentNo) {
		
		Optional<Comments> result = commentsRepository.findById(commentNo);
		
		Comments comment = result.orElseThrow();
		
		
		//Comments comment = commentsRepository.findById(commentNo).orElse(null);
		
		//자식 댓글 존재시(부모댓글)
		if(comment.getChildren().size() > 0) {
			comment.updateDeleteStatus();
		}else { //자식 댓글 존재X
			Comments parent = comment.getParent();
			
			if(parent != null) {
				parent.getChildren().remove(comment);
			}
			commentsRepository.delete(comment);
			
			if (parent != null) {
				//삭제된 부모 댓글 중 자식 댓글이 모두 삭제되었다면 부모 댓글도 삭제될 수 있도록
				if(parent.getChildren().isEmpty() && parent.getIsDeleted()) {
					commentsRepository.delete(parent);
				}
			}
		}
		
	}

	//모든 댓글 조회
	@Override
	public List<CommentsDTO> allList() {
		List<Comments> commentsList = commentsRepository.findAll(Sort.by(Sort.Direction.DESC, "commentNo"));

		List<CommentsDTO> collect = commentsList.stream()
				.map(comments -> modelMapper.map(comments, CommentsDTO.class))
				.collect(Collectors.toList());
		
		return collect;
	}

	//게시물 댓글 조회
	@Override
	public List<CommentsDTO> commentsByPostNo(Long postNo) {
		Optional<Posts> posts = postsRepository.findById(postNo);
		Posts post = posts.orElseThrow();
		//댓글 조회
		List<Comments> commentsList = commentsRepository.findByPostNoAndParentIsNullOrderByCommentNoAsc(post);
		
		//댓글과 대댓글 추가할 리스트
		List<Comments> list = new ArrayList<Comments>();
		
		//대댓글 조회하여 리스트에 추가
		for(int i=0 ; i<commentsList.size() ; i++) {
			
			list.add(commentsList.get(i));
			List<Comments> replyList = commentsRepository.findByParentCommentNoOrderByCommentNoAsc(commentsList.get(i).getCommentNo());
			for(int j=0 ; j<replyList.size(); j++) {
				list.add(replyList.get(j));
			}
		}
		
		List<CommentsDTO> collect = list.stream()
				.map(comments -> modelMapper.map(comments, CommentsDTO.class))
				.collect(Collectors.toList());
				
		
		return collect;
	}

	//대댓글 조회
	@Override
	public List<CommentsDTO> repliesListByCommentNo(Long commentNo) {
		Optional<Comments> result = commentsRepository.findById(commentNo);
		
		Comments comment = result.orElseThrow();
		
		List<Comments> replyList = comment.getChildren();
		
		List<CommentsDTO> collect = replyList.stream()
				.map(replys -> modelMapper.map(replys, CommentsDTO.class))
				.collect(Collectors.toList());
		
		return collect;
	}


	/*
	@Override
	public Long addReplyToComment(Long parentCommentNo, CommentsDTO replyDTO) {
		 // 부모 댓글 조회
	    Comments parentComment = commentsRepository.findById(parentCommentNo)
	            .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다"));

	    // 대댓글 엔티티 생성
	    Comments reply = Comments.builder()
	            .content(replyDTO.getContent())
	            .writer(replyDTO.getWriter())
	            .postNo(parentComment.getPostNo())
	            .parent(parentComment) // 부모 댓글을 지정
	            .build();

	    // 부모 댓글의 자식(대댓글) 목록에 추가
	    parentComment.getChildren().add(reply);

	    // 부모 댓글을 저장하면 자식(대댓글) 엔티티도 함께 저장됨
	    commentsRepository.save(parentComment);

	    // 대댓글 저장 및 ID 반환
	    Long replyCommentNo = commentsRepository.save(reply).getCommentNo();
	    return replyCommentNo;
	}

	@Override
	public List<CommentsDTO> commentsByPostNo(Long postNo) {

		Optional<Posts> result = postsRepository.findById(postNo);
		Posts post = result.orElseThrow();
		
		List<Comments> commentsList = commentsRepository.findByPostNo(post.getPostNo());
		
		List<CommentsDTO> collect = commentsList.stream()
				.map(comments -> modelMapper.map(comments, CommentsDTO.class))
				.collect(Collectors.toList());
		
		return collect;
	}
	*/
}
