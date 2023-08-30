package com.my.blahblah.dto;
import java.time.LocalDateTime;
import java.util.List;

import com.my.blahblah.entity.PostImages;
import com.my.blahblah.entity.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsDTO {
	
	private Long postNo;
	
	private String title;
	
	private String content;
	
	private Users writer;
	
	private LocalDateTime createdDate;
	
	// 이미지 정보를 담을 필드 추가
    private List<PostImages> imageList;
	
}