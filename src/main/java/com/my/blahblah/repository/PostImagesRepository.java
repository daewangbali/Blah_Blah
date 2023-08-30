package com.my.blahblah.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my.blahblah.entity.PostImages;
import com.my.blahblah.entity.Posts;

@Repository
public interface PostImagesRepository extends JpaRepository<PostImages, String> {
	
	List<PostImages> findByPostOrderByOrd(Posts post);
}
