package com.my.blahblah.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.my.blahblah.entity.PostImages;
import com.my.blahblah.entity.Posts;
import com.my.blahblah.repository.PostImagesRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PostImagesServiceImpl implements PostImagesService {
	
	private final String uploadPath;
	
	@Autowired
	public PostImagesServiceImpl(@Value("${com.my.blahblah.path}") String uploadPath) {
	    this.uploadPath = uploadPath;
	}
	
	@Autowired
	private PostImagesRepository postImagesRepository;
	
	//에디터 X 이미지 저장
    public List<PostImages> saveImages(List<MultipartFile> imageFiles, Posts post) {
        List<PostImages> imagesList = new ArrayList<>();
        int order = 0;

        for (MultipartFile file : imageFiles) {
            String uuid = UUID.randomUUID().toString();
            String fileName = file.getOriginalFilename();
            String savePath = uploadPath + "/" + uuid + "_" + fileName;

            try {
                // 이미지 파일을 저장하는 로직
                File imageFile = new File(savePath);
                file.transferTo(imageFile);

                PostImages image = PostImages.builder()
                    .uuid(uuid)
                    .fileName(fileName)
                    .ord(order)
                    .post(post)
                    .build();

                imagesList.add(image);

                order++;
            } catch (IOException e) {
                // 예외 처리 로직
            }
        }

        return postImagesRepository.saveAll(imagesList);
    }

	@Override
	public List<PostImages> findByPost(Posts post) {
		return postImagesRepository.findByPostOrderByOrd(post);
	}
    

}
