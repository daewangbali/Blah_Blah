package com.my.blahblah.repository;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my.blahblah.entity.Comments;
import com.my.blahblah.entity.Posts;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
	
	List<Comments> findByPostNo(Posts post, Sort sort);
	
	List<Comments> findCommentHierarchyByPostNo(Posts postNo, Sort sort);
	
	List<Comments> findByPostNoAndParentIsNullOrderByCommentNoAsc(Posts postNo);
	
	List<Comments> findByParentCommentNoOrderByCommentNoAsc(Long parentCommentNo);
	
}