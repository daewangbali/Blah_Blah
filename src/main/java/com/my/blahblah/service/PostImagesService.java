package com.my.blahblah.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.my.blahblah.entity.PostImages;
import com.my.blahblah.entity.Posts;

public interface PostImagesService {
	
	List<PostImages> saveImages(List<MultipartFile> imageFiles, Posts post);
	
	List<PostImages> findByPost(Posts post);

}
