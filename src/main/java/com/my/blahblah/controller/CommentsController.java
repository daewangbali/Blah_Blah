package com.my.blahblah.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.blahblah.dto.CommentsDTO;
import com.my.blahblah.dto.UsersDTO;
import com.my.blahblah.entity.Users;
import com.my.blahblah.service.CommentsService;
import com.my.blahblah.service.UsersService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("comments")
@RequiredArgsConstructor
@Slf4j
public class CommentsController {
	
	private final CommentsService commentsService;
	
	//뎃글 리스트 가져오기
    @GetMapping(value = "/{postNo}")
    public ResponseEntity<List<CommentsDTO>> getComments(@PathVariable Long postNo) {
    	
    	log.info("rest get.......");
    	
    	//List<CommentsDTO> comments = commentsService.commentsByPostNo(postNo);
    	
    	List<CommentsDTO> comments = commentsService.commentsByPostNo(postNo);
    	
    	if(comments==null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//댓글리스트가 없을 떄는 404 Not Found
		}else {
			return new ResponseEntity<>(comments,HttpStatus.OK);//정상수행일 때 댓글리스트와 200 으로 응답
		}
    }

    
    @GetMapping(value = "replys/{commentNo}")
    public ResponseEntity<List<CommentsDTO>> getReplys(@PathVariable Long commentNo) {
    	
    	log.info("reply get.......");
    	
    	List<CommentsDTO> replys = commentsService.repliesListByCommentNo(commentNo);
    	
    	if(replys==null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//댓글리스트가 없을 떄는 404 Not Found
		}else {
			return new ResponseEntity<>(replys,HttpStatus.OK);//정상수행일 때 댓글리스트와 200 으로 응답
		}
    }
    
    
    //댓글 하나 내용 가져오기(for update)
    @GetMapping(value = "readOne/{commentNo}")
    public ResponseEntity<CommentsDTO> readOne(@PathVariable Long commentNo) {
    	log.info("rest get.......");
    	CommentsDTO comment = commentsService.readOne(commentNo);
    	
    	if(comment.getContent()== null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//상품정보가 없을 떄는 404 Not Found
		}else {
			return new ResponseEntity<>(comment,HttpStatus.OK);//정상수행일 때 상품정보와 200 으로 응답
		}
    }

    //댓글 등록
	@ApiOperation(value = "Comments POST", notes = "POST 방식으로 댓글 등록")
	@PostMapping(value = "regi", consumes = "application/json")
	public ResponseEntity<CommentsDTO> register(@RequestBody CommentsDTO commentsDTO, BindingResult bindingResult)throws BindException {
		
		log.info("rest post.......");
		if(bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		
		Long commentNo = commentsService.register(commentsDTO);
		CommentsDTO commentDTO = commentsService.readOne(commentNo);
		log.info("regi DTO!!!!!!!!!!!!!!!!!!! {}",commentDTO);
		
		return new ResponseEntity<>(commentDTO, HttpStatus.OK);
	}
	
	//댓글 삭제
    @DeleteMapping(value = "remove/{commentNo}")
    public void remove(@PathVariable Long commentNo){
    	log.info("rest remove.......");
    	commentsService.remove(commentNo);
    }
    
    //댓글 수정
    @ApiOperation(value = "Modify Comment", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "modify", consumes = "application/json")
    public void modify(@RequestBody CommentsDTO commentsDTO ){
    	log.info("rest modify.......");
    	commentsService.modify(commentsDTO);
    }
   
}
