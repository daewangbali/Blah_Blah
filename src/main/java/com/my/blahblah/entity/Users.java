package com.my.blahblah.entity;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
@Entity 
@Table(name="Users") // 테이블명 
@SequenceGenerator(name="jpa_users_seq_generator",sequenceName = "jpa_users_seq",initialValue = 1,allocationSize = 1)
public class Users {
	
	@Column(name = "USER_NO") // db 컬럼명 매핑 
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "jpa_users_seq_generator")
	private Long userNo;
	
	@Column(name = "USER_ID") // 데이터베이스 컬럼명과 매핑
	private String userId;
	@Column
	private String password;
	@Column
	private String name;
	@Column
	private String tel;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Authority> authorities;

	@Builder
	public Users(String userId, String password, String name, String tel) {
		super();
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.tel = tel;
	}
	
	@Builder
	public Users(Long userNo, String userId, String name) {
		super();
		this.userId = userId;
		this.name = name;
	}
	
	@Builder
	public void updateUser(String password, String name, String tel) {
		this.password = password;
		this.name=name;
		this.tel=tel;
	}
	
	public void encodingPassword(String password) {
		this.password = password;
	}
	
	 //@Basic
	 //private byte[] nonSerializableField; // 직렬화를 막음
	
}
