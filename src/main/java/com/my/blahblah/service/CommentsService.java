package com.my.blahblah.service;

import java.util.List;

import com.my.blahblah.dto.CommentsDTO;

public interface CommentsService {
	
	Long register(CommentsDTO commentsDTO);
	
	CommentsDTO readOne(Long commentNo);
	
	void modify(CommentsDTO commentsDTO);
	
	void remove(Long commentNo);
	
	List<CommentsDTO> allList();
	
	List<CommentsDTO> commentsByPostNo(Long postNo);
	
	//대댓글 리스트 조회
	List<CommentsDTO> repliesListByCommentNo(Long commentNo);
	
}
