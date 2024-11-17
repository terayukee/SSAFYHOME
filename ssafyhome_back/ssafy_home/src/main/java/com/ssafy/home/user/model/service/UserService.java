package com.ssafy.home.user.model.service;

import java.util.List;

import com.ssafy.home.user.model.UserDto;

public interface UserService {

	// 로그인
	UserDto login(UserDto userDto) throws Exception;
	// 단일유저정보 
	UserDto getUserInfo(String userId) throws Exception;
	// 유저 전체
	List<UserDto> getAllUserList() throws Exception;
	// 회원가입
	UserDto joinUser(String UserDto) throws Exception;
	// 유저 수정
	int userUpdate(UserDto userDto) throws Exception;
	// 유저 삭제 
	int userDelete(UserDto userDto) throws Exception;
	
	
	
	
}
