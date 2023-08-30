package com.my.blahblah.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Entity // jpa가 관리하는 클래스 
@Table(name="Authority") // 테이블명 
@SequenceGenerator(name="jpa_autho_seq_generator",sequenceName = "jpa_autho_seq",initialValue = 1,allocationSize = 1)
public class Authority {
	
	@Column(name = "authority_no")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "jpa_autho_seq_generator")
    private Long authorityNo;

    @Column(name = "role")
    private String role;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private Users user;
    
    @Builder
	public Authority(String role, Users user) {
		super();
		this.role = role;
		this.user = user;
	}
    
}
