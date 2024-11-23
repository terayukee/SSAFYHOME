package com.ssafy.home.user.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Service;

import com.ssafy.home.user.model.UserDto;
import com.ssafy.home.user.model.mapper.UserMapper;
import com.ssafy.home.util.JWTUtil;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserMapper userMapper;
	private final JWTUtil jwtUtil;
	
	public UserServiceImpl(UserMapper userMapper, JWTUtil jwtUtil) {
		super();
		this.userMapper = userMapper;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public UserDto login(UserDto userDto) throws Exception {
		return userMapper.login(userDto);
	}

	@Override
	public UserDto getUserInfo(String userId) throws Exception {
		return userMapper.getUserInfo(userId);
	}

	@Override
	public List<UserDto> getAllUserList() throws Exception {
		return null;
	}

	@Override
	public int joinUser(UserDto userDto) throws Exception {
		return userMapper.joinUser(userDto);
		
	}
	
	@Override
	public int userUpdate(UserDto userDto) throws Exception {
		return 0;
	}

	@Override
	public int userDelete(UserDto userDto) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void saveRefreshToken(UserDto userDto) throws Exception {
		userMapper.saveRefreshToken(userDto);
	}

	@Override
	public Object getRefreshToken(int userNo) throws Exception {
		return userMapper.getRefreshToken(userNo);
	}

	@Override
	public void deleteRefreshToken(int userNo) throws Exception {
		userMapper.deleteRefreshToken(userNo);;
	}
	
	// 닉네임만 쓰다보니 정말 멀리 돌아감
	@Override
	public Map<String, Object> checkUserInfo(UserDto userDto) throws Exception {
		
		// 토큰 저장용
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 닉네임이 있으면 회원가입 
		int userCount = userMapper.getUserCountByNickName(userDto.getUserNickname());
		// 회원가입
		if(userCount == 0) {
			// 회원가입 
			userMapper.joinUser(userDto);
			// 가입한 유저 번호 
			int userNum = userMapper.getUserNumByNickName(userDto.getUserNickname());
			// 토큰 생성
			String accessToken = jwtUtil.createAccessToken(userNum);
			String refreshToken = jwtUtil.createRefreshToken(userNum);
			// 토큰저장 
			userDto.setRefreshToken(refreshToken);
			saveRefreshToken(userDto);
			
			resultMap.put("access-token", accessToken);
			resultMap.put("refresh-token", refreshToken);
			
			return resultMap;
			
		}else { // 기존 정보로
			int userNum = userMapper.getUserNumByNickName(userDto.getUserNickname());
			String accessToken = jwtUtil.createAccessToken(userNum);
			String refreshToken = jwtUtil.createRefreshToken(userNum);
			// 토큰저장 
			userDto.setRefreshToken(refreshToken);
			saveRefreshToken(userDto);
			
			resultMap.put("access-token", accessToken);
			resultMap.put("refresh-token", refreshToken);
			return resultMap;
		}
	}

	
}
