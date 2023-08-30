package com.my.blahblah.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;

import com.my.blahblah.dto.PostsDTO;
import com.my.blahblah.dto.UsersDTO;
import com.my.blahblah.entity.Posts;
import com.my.blahblah.entity.Users;
import com.my.blahblah.service.PostsService;
import com.my.blahblah.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("posts")
@RequiredArgsConstructor
@Slf4j
public class PostsController {
	
	private final ModelMapper modelMapper;
	
	private final PostsService postsService;
	
	private final UsersService usersService;
	
	@Value("${com.my.blahblah.path}")
	private String uploadPath;
	
	/* 페이지네이션 X
	@GetMapping("list")
	public void postsList(Model model){
		log.info("posts list.......");
		model.addAttribute("postslist", postsService.list());
		//return "posts/list";
	}
	*/
	/*
	@GetMapping("list")
    public void listPosts(@RequestParam(name = "page", defaultValue = "1") int page,
                            @RequestParam(name = "size", defaultValue = "5") int size,
                            Model model) {
        Page<Posts> postsPage = postsService.listPagination(page, size);
        
        model.addAttribute("postsPage", postsPage);

    }
	*/
	
	//게시물 리스트 조회
	@GetMapping("list/{page}")
    public String listPostsPage(@PathVariable int page, @RequestParam(name = "size", defaultValue = "5") int size, Model model) {
        Page<Posts> postsPage = postsService.listPagination(page, size);
        model.addAttribute("postsPage", postsPage);
        return "posts/list";
    }
	
	//게시물 작성폼
	@GetMapping("registerForm")
	public void registerForm(){
		log.info("registerForm.......");
	}
	
	//게시물 작성 + 에디터 적용폼
	@GetMapping("registerEditorForm")
	public String registerEditorForm(){
		log.info("registerEditorForm.......");
		return "posts/registerEditorForm";
	}
	
	//게시물 상세조회
	@GetMapping("postView2")
	public void postView(Model model, Long postNo){
		log.info("postView.......");
		PostsDTO postsDTO = postsService.readOne(postNo);
		model.addAttribute("post", postsDTO);
	}
	
	//게시물 + 이미지 상세조회
	@GetMapping("postView")
	public void postWithImagesView(Model model, Long postNo){
		log.info("postWithImagesView.......");
		PostsDTO postsDTO = postsService.getPostWithImages(postNo);
		model.addAttribute("post", postsDTO);
	}
	
	//게시물 + 에디터 상세조회
	@GetMapping("postEditorView")
	public void postEditorView(Model model, Long postNo){
		log.info("postEditorView.......");
		PostsDTO postsDTO = postsService.getPostWithImages(postNo);
		model.addAttribute("post", postsDTO);
	}
	
	//게시물 등록
	@PostMapping("registerPost2")
	public String registerPost(PostsDTO postsDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
		
		if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/posts/registerForm";
        }
		log.info("register post.......");
		Long postNo = postsService.register(postsDTO);
		redirectAttributes.addAttribute("postNo", postNo);
		
		return "redirect:/posts/postView";
	}
	
	//게시물 + 이미지 등록
    @PostMapping("registerPost")
    public String registerPostWithImages(PostsDTO postsDTO, @RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes) throws IOException {

    	log.info("register post + images.......");
        if (bindingResult.hasErrors()) {
        	log.info("register post + images 게시글 등록 중 에러 발생");
            return "posts/registerForm";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // 인증 정보로부터 현재 사용자의 정보 가져오기
        Object principal = authentication.getPrincipal();
        
        // principal이 DTO 타입인지 확인하고, 엔티티로 형변환
        if (principal instanceof UsersDTO) {
            UsersDTO userDTO = (UsersDTO) principal;
            Users user = modelMapper.map(userDTO, Users.class);
    		postsDTO.setWriter(user);
        }
        
        Long postNo;
        if (imageFiles == null || imageFiles.length == 0 || imageFiles[0].isEmpty()) {
        	postNo = postsService.register(postsDTO); // 이미지 업로드 X
        } else {
        	postNo = postsService.registerPostWithImages(postsDTO, imageFiles); // 이미지 업로드 O
        }
        
        redirectAttributes.addAttribute("postNo", postNo);

        return "redirect:/posts/postView";
    }
	
	//게시물 수정폼
	@GetMapping("modifyForm")
	public void modifyForm(Model model, Long postNo){
		log.info("modifyForm.......");
		PostsDTO postsDTO = postsService.readOne(postNo);
		model.addAttribute("post", postsDTO);
	}
	
	//게시물 수정
	@PostMapping("modifyPost")
	public String modifyPost(PostsDTO postsDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
		
		if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/posts/modifyForm";
        }
		log.info("modifyPost post.......");
		postsService.modify(postsDTO);
		redirectAttributes.addAttribute("postNo", postsDTO.getPostNo());
		
		return "redirect:/posts/postView";
	}
	
	//게시물 삭제
	@PostMapping("removePost")
	public String removePost(Model model, Long postNo, RedirectAttributes redirectAttributes){
		log.info("removePost.......");
		postsService.remove(postNo);
		redirectAttributes.addFlashAttribute("message","삭제되었습니다.");
		return "redirect:/posts/list/1";
	}
	
	//게시물 검색 조회
	@GetMapping("searchPostsList/{page}")
	public String searchPostsList(String searchType, String searchContent, @PathVariable int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            Model model){
		log.info("searchPostsList.......");
	
		Page<Posts> postsPage = postsService.searchList(page, size, searchType, searchContent);
	
		model.addAttribute("postsPage", postsPage);
		return "posts/list";
	}
	
}
