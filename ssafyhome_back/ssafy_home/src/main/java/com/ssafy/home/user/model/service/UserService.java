package com.ssafy.home.user.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.home.user.model.UserDto;

public interface UserService {

	// 로그인
	UserDto login(UserDto userDto) throws Exception;
	
	// 단일유저정보 
	UserDto getUserInfo(String userNo) throws Exception;
	
	// 유저 전체
	List<UserDto> getAllUserList() throws Exception;
	// 회원가입
	int registerUser(UserDto UserDto) throws Exception;
	// 유저 수정
	int userUpdate(UserDto userDto) throws Exception;
	// 유저 삭제 
	boolean withdrawUser(int userNo) throws Exception;
	
	// 리프레시 토큰 저장
	void saveRefreshToken(UserDto userDto) throws Exception;
	
	// 리프레시 정보 받아오기
	Object getRefreshToken(int userNo) throws Exception;
	
	// 리프레시 토큰 삭제
	void deleteRefreshToken(int userNo) throws Exception;
	
	
	UserDto getDeletedUserByEmail(String email) throws Exception;
	
	boolean restoreUser(int userNo) throws Exception;

	UserDto getUserByEmail(String email) throws Exception;
}
