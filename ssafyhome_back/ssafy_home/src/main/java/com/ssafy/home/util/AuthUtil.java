package com.ssafy.home.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ssafy.home.user.model.UserDto;
import com.ssafy.home.user.model.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthUtil {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    // JWT 토큰 생성 및 저장
    public Map<String, String> generateAndStoreTokens(UserDto user) throws Exception {
        // 1. JWT 생성
        String accessToken = jwtUtil.createAccessToken(user);
        String refreshToken = jwtUtil.createRefreshToken(user.getUserNo());

        // 2. 리프레시 토큰 DB 저장
        user.setRefreshToken(refreshToken);
        userService.saveRefreshToken(user);

        // 3. 토큰 반환
        return Map.of(
            "access-token", accessToken,
            "refresh-token", refreshToken
        );
    }

    // 사용자 등록 또는 갱신 처리
    public UserDto registerOrUpdateUser(UserDto user, String email, String nickname) throws Exception {
        UserDto existingUser = userService.getUserByEmail(email);

        if (existingUser == null) {
            // 신규 사용자 등록
            user.setUserNickname(nickname);
            user.setEmail(email);
            user.setUserName(nickname); // 이름은 닉네임으로 설정
            user.setRole("USER"); // 기본 권한 설정
            if (userService.registerUser(user) == 0) {
                throw new Exception("회원가입에 실패했습니다.");
            }
            return user;
        } else {
            // 기존 사용자 반환
            return existingUser;
        }
    }
}

