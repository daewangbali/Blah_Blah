package com.my.blahblah.dto;
import java.time.LocalDateTime;

import com.my.blahblah.entity.Comments;
import com.my.blahblah.entity.Posts;
import com.my.blahblah.entity.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentsDTO {
	
	private Long commentNo;
	private String content;
	private Users writer;
	private Posts postNo;
	private LocalDateTime createdDate;
	private Comments parent;
	private Boolean isDeleted;
	
}
