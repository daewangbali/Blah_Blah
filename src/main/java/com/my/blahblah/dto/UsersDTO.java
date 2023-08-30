package com.my.blahblah.dto;
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
public class UsersDTO {
	private Long userNo;
	private String userId;
	private String password;
	private String name;
	private String tel;
}
