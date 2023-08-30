package com.my.blahblah.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my.blahblah.entity.Authority;
import com.my.blahblah.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
	
	//아이디 중복확인
	boolean existsByUserId(String id);
	
	Users findByUserId(String userId);
	
	public List<Users> findByName(String name);
	public List<Users> findByTel(String tel);
	public List<Users> findByPassword(String password);
	
	//public List<Authority> findAuthoritiesByUserId(String userId);
	//public List<Authority> findByUserUserId(String userId);

}