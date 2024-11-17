package com.ssafy.home.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDto {
	
	private String userId;
	private String userName;
	private String userPassword;
	private String email;
	private String phoneNumber;
	private String role;
	private String registerDate; // 가입일 
	private boolean deleted; // 논리적 삭제 
	private String deletedAt; // 삭제 일시

}
