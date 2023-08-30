package com.my.blahblah.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.my.blahblah.dto.PostsDTO;
import com.my.blahblah.entity.Posts;

public interface PostsService {
	
	Long register(PostsDTO postsDTO);
	
	PostsDTO readOne(Long postNo);
	
	void modify(PostsDTO postsDTO);
	
	void remove(Long postNo);
	
	List<PostsDTO> list();
	
	Page<Posts> listPagination(int pageNumber, int pageSize);
	
	Page<Posts> searchList(int pageNumber, int pageSize, String searchType, String searchContent);
	
	Long registerPostWithImages(PostsDTO postsDTO, MultipartFile[] imageFiles);
	
	PostsDTO getPostWithImages(Long postNo);
	
	
	//PageResponseDTO<PostsDTO> list(PageRequestDTO pageRequestDTO);
	
}
