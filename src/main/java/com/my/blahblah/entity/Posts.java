package com.my.blahblah.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString(exclude = {"commentsList", "imageSet"})
@Entity
@Table(name="Posts") // 테이블명
@SequenceGenerator(name="jpa_posts_seq_generator",sequenceName = "jpa_posts_seq",initialValue = 1,allocationSize = 1)
public class Posts {
	@Column(name = "POST_NO") // db 컬럼명 매핑 
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "jpa_posts_seq_generator")
	private Long postNo;
	@Column
	private String title;
	@Column
	private String content;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "USER_NO")
	private Users writer;
	
	//@Column(nullable=false, columnDefinition = "date default sysdate")
	@CreationTimestamp
	private LocalDateTime createdDate;
	
	@JsonIgnore
    @OneToMany(mappedBy = "postNo", cascade = CascadeType.REMOVE, fetch =  FetchType.EAGER)
    private List<Comments> commentsList;
	
	@JsonIgnore
    @OneToMany(mappedBy = "post",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @BatchSize(size = 20)
    private List<PostImages> imageList;
	
	/*
	@OneToMany(mappedBy = "posts", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Comments> comments;
	*/
	
	@Builder
	public Posts(String title, String content, Users writer) {
		super();
		this.title = title;
		this.content = content;
		this.writer = writer;
	}
	
	@Builder
	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}
	
}