package com.my.blahblah.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my.blahblah.entity.Posts;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long>{
	
	//findBy 뒤에 컬럼을 붙여주면 이를 이용해 SELECT 가 가능
	public List<Posts> findByTitleLike(String title);
	public List<Posts> findByContentLike(String content);
	
	Page<Posts> findAllByOrderByPostNoDesc(Pageable pageable);
	
	//검색
	Page<Posts> findByTitleContaining(String title, Pageable pageable);
	Page<Posts> findByContentContaining(String content, Pageable pageable);
	Page<Posts> findByWriter_UserId(String writer, Pageable pageable);
	Page<Posts> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
	
}