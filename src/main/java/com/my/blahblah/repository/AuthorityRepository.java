package com.my.blahblah.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my.blahblah.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long>{

	List<Authority> findByUserUserId(String userId);
	
}
