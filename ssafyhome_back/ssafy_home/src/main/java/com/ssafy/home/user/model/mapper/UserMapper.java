package com.ssafy.home.user.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.home.user.model.UserDto;

@Mapper
public interface UserMapper {

	// 로그인
	UserDto login(UserDto userDto) throws SQLException;
	// 단일유저정보 
	UserDto getUserInfo(String userId) throws SQLException;
	// 유저 전체
	List<UserDto> getAllUserList() throws SQLException;
	// 회원가입
	int registerUser(UserDto UserDto) throws SQLException;
	// 유저 수정
	int userUpdate(UserDto userDto) throws SQLException;
	
	// 회원 탈퇴
	int withdrawUser(int userNo) throws SQLException;
	
	// 리프레시 토큰 저장
	void saveRefreshToken(UserDto userDto) throws SQLException;
	
	// 리프레시 정보 받아오기
	Object getRefreshToken(int userNo) throws SQLException;
	
	// 리프레시 토큰 삭제
	void deleteRefreshToken(int userNo) throws SQLException;
	
	int getUserCountByNickName(String userId) throws Exception;
	
	int getUserNumByNickName(String userName) throws Exception;
	
	int checkEmailExists(String email) throws Exception;
	
	UserDto getDeletedUserByEmail(String email) throws Exception;
	
	int restoreUser(int userNo) throws Exception;
	
	
}
