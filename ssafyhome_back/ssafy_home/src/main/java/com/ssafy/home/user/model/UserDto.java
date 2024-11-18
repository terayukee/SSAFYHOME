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
	
	private int userId;
	private String userName;
	private String userPassword;
	private String userNickname;
	private String email;
	private String phoneNumber;
	private String role;
	private String registerDate; // 가입일 
	private Boolean deleted; // 논리적 삭제 
	private String deletedAt; // 삭제 일시
	private String refreshToken;
	
}
