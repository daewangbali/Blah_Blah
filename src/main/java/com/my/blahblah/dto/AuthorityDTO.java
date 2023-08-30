package com.my.blahblah.dto;

import com.my.blahblah.entity.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthorityDTO {
	
	private Long authorityNo;
    private String role;
    private Users user;

}
