package com.my.blahblah.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.my.blahblah.dto.UsersDTO;
import com.my.blahblah.entity.Authority;
import com.my.blahblah.entity.Posts;
import com.my.blahblah.entity.Users;
import com.my.blahblah.repository.AuthorityRepository;
import com.my.blahblah.repository.UsersRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class UsersServiceImpl implements UsersService {
	private final ModelMapper modelMapper;
	
	private final UsersRepository usersRepository;
	
	private final AuthorityRepository authorityRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public Long register(UsersDTO usersDTO) {
		
		String encodedPassword = passwordEncoder.encode(usersDTO.getPassword());
		usersDTO.setPassword(encodedPassword);
		
		Users user = modelMapper.map(usersDTO, Users.class);
		Users regiUser = usersRepository.save(user);
		
		Authority authority = new Authority("ROLE_USER", regiUser);
		
		/*
		Authority authority = Authority.builder()
				.role("ROLE_USER")
				.user(regiUser)
				.build();
		*/
		
		authorityRepository.save(authority);
		
		Long userNo = regiUser.getUserNo();
		
		return userNo;
		
	}


	@Override
	public void modify(UsersDTO usersDTO) {
		
		String encodedPassword = passwordEncoder.encode(usersDTO.getPassword());
		usersDTO.setPassword(encodedPassword);
		Optional<Users> result = usersRepository.findById(usersDTO.getUserNo());
				
		Users user = result.orElseThrow();
		
		user.updateUser(usersDTO.getPassword(), usersDTO.getName(), usersDTO.getTel());
		
		usersRepository.save(user);
		
	}

	@Override
	public boolean checkDuplicateId(String id) {
		return usersRepository.existsByUserId(id);
	}

	@Override
	public UsersDTO readOneByUserId(String id) {
		Users user =  usersRepository.findByUserId(id);
		if(user == null) return null;
		else {
			UsersDTO usersDTO = modelMapper.map(user, UsersDTO.class);
			
			return usersDTO;
		}
		
	}

	@Override
	public UsersDTO readOne(Long userNo) {
		Users user =  usersRepository.findById(userNo).get();
		UsersDTO usersDTO = modelMapper.map(user, UsersDTO.class);
		
		return usersDTO;
	}


	@Override
	public List<Authority> readAuthority(String userId) {
		return authorityRepository.findByUserUserId(userId);
	}

	//회원탈퇴(ROLE 변경)
	@Override
	public void delete(String userId, String password) {
		
		Users user =  usersRepository.findByUserId(userId);
		
		List<Authority> list = authorityRepository.findByUserUserId(userId);
		for(Authority a : list) {
			authorityRepository.delete(a);
		}
		
		Authority authority = new Authority("ROLE_WITHDRAWAL", user);
		authorityRepository.save(authority);
		
	}


	@Override
	public boolean checkPassword(String id, String password) {
		Users user =  usersRepository.findByUserId(id);
		String userPassword = user.getPassword();
		
		if(passwordEncoder.matches(password, userPassword)) {
			return true;
		}else
			return false;
			
	}
	
	
	
}
