package com.my.blahblah.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.my.blahblah.dto.PostsDTO;
import com.my.blahblah.entity.Posts;
import com.my.blahblah.repository.PostsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PostsServiceImpl implements PostsService {
	
	private final ModelMapper modelMapper;
	
	private final PostsRepository postsRepository;
	
	private final PostImagesServiceImpl postImagesService;

	//게시글 작성
	@Override
	public Long register(PostsDTO postsDTO) {
/*
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = new Users();
        if (principal instanceof Users) {
        	Users userDetails = (Users) principal;
        	user = Users.builder()
        			.userNo(userDetails.getUserNo())
        			.userId(userDetails.getUserId())
        			.name(userDetails.getName())
        			.build(); 
        	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+user);
        	//log.info("user!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! {}",user);
            // 글 작성 로직을 수행하고 작성자 정보를 저장
            // ...
        }
        */
		
        //postsDTO.setWriter(user);
		
		Posts post = modelMapper.map(postsDTO, Posts.class);
		Long postNo = postsRepository.save(post).getPostNo();
		
		return postNo;
	}
	
	//게시글 작성 + 이미지 등록
    public Long registerPostWithImages(PostsDTO postsDTO, MultipartFile[] imageFiles) {
        // 게시물 등록
        Posts post = modelMapper.map(postsDTO, Posts.class);
        Posts savedPost = postsRepository.save(post);

        // 이미지 업로드 및 저장
        postImagesService.saveImages(Arrays.asList(imageFiles), savedPost);

        return savedPost.getPostNo();
    }

    //게시물 상세조회
	@Override
	public PostsDTO readOne(Long postNo) {

		Optional<Posts> result = postsRepository.findById(postNo);
		Posts post = result.orElseThrow();
		PostsDTO postsDTO = modelMapper.map(post, PostsDTO.class);
		
		return postsDTO;
	}

	//게시글 + 이미지 상세조회
    @Override
    public PostsDTO getPostWithImages(Long postNo) {
        Posts post = postsRepository.findById(postNo)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        PostsDTO postsDTO = modelMapper.map(post, PostsDTO.class);
        
        return postsDTO;
    }

	
	@Override
	public void modify(PostsDTO postsDTO) {
		
		Optional<Posts> result = postsRepository.findById(postsDTO.getPostNo());
		
		Posts post = result.orElseThrow();
		
		post.update(postsDTO.getTitle(),postsDTO.getContent() );
		
		postsRepository.save(post);
		
	}

	@Override
	public void remove(Long postNo) {
		
		postsRepository.deleteById(postNo);
		
	}

	@Override
	public List<PostsDTO> list() {
		List<Posts> postsList = postsRepository.findAll(Sort.by(Sort.Direction.DESC, "postNo"));

		List<PostsDTO> collect = postsList.stream()
				.map(posts -> modelMapper.map(posts, PostsDTO.class))
				.collect(Collectors.toList());
		return collect;
	}

	@Override
    public Page<Posts> listPagination(int pageNumber, int pageSize) {
        // 페이지 번호는 0부터 시작하므로 1을 빼줌
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return postsRepository.findAllByOrderByPostNoDesc(pageable);
    }

	@Override
	public Page<Posts> searchList(int pageNumber, int pageSize, String searchType, String searchContent) {
		
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); 
		
		if(searchType.equals("t")) {
			return postsRepository.findByTitleContaining(searchContent, pageable);
		}else if(searchType.equals("c")) {
			return postsRepository.findByContentContaining(searchContent, pageable);
		}else if(searchType.equals("w")) {
			return postsRepository.findByWriter_UserId(searchContent, pageable);
		}else {
			return postsRepository.findByTitleContainingOrContentContaining(searchContent, searchContent, pageable);
		}
		
	}
	
	
	/*
	@Override
	public PageResponseDTO<PostsDTO> list(PageRequestDTO pageRequestDTO) {
		
		String[] types = pageRequestDTO.getTypes();
		String keyword = pageRequestDTO.getKeyword();
		Pageable pageable = postsrepository.searchAll();
		return null;
	}
	*/

}
