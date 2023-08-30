package com.my.blahblah.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.my.blahblah.dto.CommentsDTO;
import com.my.blahblah.dto.UsersDTO;
import com.my.blahblah.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {
	
	private final UsersService usersService;
	
	@GetMapping("joinForm")
	public String joinForm(Model model){
		log.info("open joinForm...");
		model.addAttribute("message", "Thymeleaf home page!");
		return "users/joinForm";
	}
	
	@GetMapping("loginForm")
	public void loginForm(Model model){
		log.info("open loginForm...");
		//model.addAttribute("message", "Thymeleaf home page!");
		//return "users/loginForm";
	}
	
	//회원가입
	@ResponseBody
	@PostMapping(value = "registerUser", consumes = "application/json")
	public ResponseEntity<UsersDTO> registerUser(@RequestBody UsersDTO usersDTO, BindingResult bindingResult)throws BindException {
		log.info("registerUser...");
		if(bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		Long userNo = usersService.register(usersDTO);
		
		UsersDTO userDTO = usersService.readOne(userNo);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}
	
	
	//중복아이디 확인
    @GetMapping("checkDuplicateId/{id}")
    @ResponseBody
    public ResponseEntity<Boolean> checkDuplicateId(@PathVariable String id) {
    	log.info("checkDuplicateId...");
    	return new ResponseEntity<>(usersService.checkDuplicateId(id), HttpStatus.OK);
    }
    
    //로그인 실패시
    @RequestMapping("loginFail")
	public String loginFail(Model model) {
    	//model.addAttribute("failMessage", "로그인실패");
		//return "users/loginform";
    	return "users/loginFail";
	}
    
    //마이페이지 이동
    @GetMapping("myPage")
	public String myPage(Model model){
		log.info("open myPage home...");
		return "mypage/home";
	}
    
    //회원정보수정 폼으로 이동
    @GetMapping("updateForm")
	public String updateForm(Model model){
		log.info("open updateForm...");
		return "mypage/updateForm";
	}
    
    //회원정보수정 
    @PostMapping("updateUser")
	public String updateUser(UsersDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Authentication authentication, HttpServletRequest request){
		log.info("updateUser.......");
		
		if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/users/updateForm";
        }
		
		usersService.modify(userDTO);
		
		//세션값 변경
		HttpSession session = request.getSession();
		session.setAttribute("userDTO", userDTO);
		
		return "redirect:/users/updateForm";
	}
    
    //회원탈퇴 폼으로 이동
    @GetMapping("deleteForm")
	public String deleteForm(Model model){
		log.info("open deleteForm...");
		return "mypage/deleteForm";
	}
    
    //회원탈퇴 비밀번호 확인
    @GetMapping(value = "deleteChkPass/{userId}/{password}")
    @ResponseBody
	public ResponseEntity<Boolean> deleteChkPass(@PathVariable String userId, @PathVariable String password, Model model){
		log.info("open deleteChkPass...");
		//입력한 비밀번호와 일치하는지 확인
		boolean chkResult = usersService.checkPassword(userId, password);
		return new ResponseEntity<>(chkResult,HttpStatus.OK);
	}
    
    
    //회원탈퇴
    @PostMapping("deleteUser")
	public String deleteUser(UsersDTO user, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes, Authentication authentication, HttpServletRequest request){
		log.info("deleteUser.......");
		
		if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/users/deleteForm";
        }
		
		usersService.delete(user.getUserId(), user.getPassword());
		
		HttpSession session = request.getSession();
		session.invalidate();
		
		return "redirect:/home";
		
	}
	
}
