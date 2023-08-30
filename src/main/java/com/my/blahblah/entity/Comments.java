package com.my.blahblah.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString(exclude = {"postNo","children"})
@Entity // jpa가 관리하는 클래스 
@Table(name="Comments") // 테이블명 
@SequenceGenerator(name="jpa_comments_seq_generator",sequenceName = "jpa_comments_seq",initialValue = 1,allocationSize = 1)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Comments {
	@Column(name = "COMMENT_NO") // db 컬럼명 매핑 
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "jpa_comments_seq_generator")
	private Long commentNo;
	@Column
	private String content;

	@ManyToOne
	@JoinColumn(referencedColumnName = "USER_NO")
	private Users writer;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "post_no")
	private Posts postNo;
	
	@CreationTimestamp
	private LocalDateTime createdDate;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_no")
    @JsonIgnore
    private Comments parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    //@JsonIgnore
    private List<Comments> children = new ArrayList<Comments>();
	
    @Column(nullable = true)
    private Boolean isDeleted = false;
	
    
	/*
	@JsonIgnore
    @OneToMany(mappedBy = "commentNo", cascade = CascadeType.REMOVE, fetch =  FetchType.EAGER)
    private List<Replys> replysList;
	*/
	
	@Builder
	public Comments(String content, Users writer, Posts postNo) {
		super();
		this.content = content;
		this.writer = writer;
		this.postNo = postNo;
		this.isDeleted = false;
	}
	
	 @Builder
	 public Comments(String content, Users writer, Posts postNo, Comments parent) {
		 super();
		 this.content = content;
		 this.writer = writer;
		 this.postNo = postNo;
		 this.parent = parent;
	 }
	
	@Builder
	public void update(String content) {
		this.content = content;
	}
	
	
	public void updateDeleteStatus() {
		this.isDeleted = true;
	}
	
	
}