package com.ssafy.home.user.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Service;

import com.ssafy.home.user.model.UserDto;
import com.ssafy.home.user.model.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserMapper userMapper;
	
	public UserServiceImpl(UserMapper userMapper) {
		super();
		this.userMapper = userMapper;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto joinUser(String UserDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int userUpdate(UserDto userDto) throws Exception {
		// TODO Auto-generated method stub
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
	public void deleRefreshToken(int userNo) throws Exception {
		userMapper.deleRefreshToken(userNo);;
	}

	
}
