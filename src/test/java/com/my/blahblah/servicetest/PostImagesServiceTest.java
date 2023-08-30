package com.my.blahblah.servicetest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.my.blahblah.entity.PostImages;
import com.my.blahblah.entity.Posts;
import com.my.blahblah.repository.PostImagesRepository;
import com.my.blahblah.service.PostImagesServiceImpl;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Slf4j
@Transactional
public class PostImagesServiceTest {
	
    @MockBean
    private PostImagesRepository postImagesRepository;

    @Test
    public void testRegister() throws IOException {
        String uploadPath = "/path/to/upload"; // 실제 업로드 경로가 아닌 모의값

        PostImagesServiceImpl postImagesService = new PostImagesServiceImpl(uploadPath, postImagesRepository);

        // MockMultipartFile 생성
        MockMultipartFile file1 = new MockMultipartFile("file", "filename1.png", "image/png", "filecontent1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "filename2.png", "image/png", "filecontent2".getBytes());

        // Mock Posts 객체 생성
        Posts post = Posts.builder().title("Test Post").content("Test Content").build();

     // Mock 객체 생성
        List<PostImages> mockImagesList = new ArrayList<>();
        PostImages mockImage1 = PostImages.builder()
            .uuid("uuid1")
            .fileName("filename1.png")
            .ord(0)
            .post(post) // post 객체 설정
            .build();
        mockImagesList.add(mockImage1);

        PostImages mockImage2 = PostImages.builder()
            .uuid("uuid2")
            .fileName("filename2.png")
            .ord(1)
            .post(post) // post 객체 설정
            .build();
        mockImagesList.add(mockImage2);

        // Repository 응답 설정
        when(postImagesRepository.saveAll(any())).thenReturn(mockImagesList);

        // 테스트 실행
        //List<PostImages> result = postImagesService.register(List.of(file1, file2), post);

        // 결과 검증
        // 원하는 결과로 변경하여 검증
        //for(PostImages pi : result)
        //log.info("pi {} ",pi);
    }


}
