package com.ssafy.home.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.user.model.UserDto;
import com.ssafy.home.user.model.service.UserService;
import com.ssafy.home.util.KakaoUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/oauth/kakao")
@Slf4j
public class KakaoLoginController {		

    // 유저 서비스
    private final UserService userService;

    // 카카오 로그인 유틸
    private final KakaoUtil kakaoUtil;

    public KakaoLoginController(UserService userService, KakaoUtil kakaoUtil) {
        this.kakaoUtil = kakaoUtil;
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam String code,
            @RequestParam String clientId,
            @RequestParam String redirectUri,
            @RequestParam String clientSecret) {
        try {
        	// 1. Front에서 정보 받아오기 
            log.info("카카오 인가 코드: {}", code);
            log.info("클라이언트 정보 - clientId: {}, redirectUri: {}, clientSecret: {}", clientId, redirectUri, clientSecret);

            
            // 2. 인가 코드와 클라이언트 정보를 사용하여 액세스 토큰 가져오기
            String accessToken = kakaoUtil.getAccessToken(code, clientId, redirectUri, clientSecret);

            // 3. 액세스 토큰으로 사용자 정보 가져오기
            Map<String, Object> userInfo = kakaoUtil.getUserInfo(accessToken);

            String id = (String) userInfo.get("id");
            String nickname = (String) userInfo.get("nickname");
            String email = (String) userInfo.get("email");

            log.info("카카오 사용자 정보 - id: {}, nickname: {}, email : {}", id, nickname, email);
            
             
            UserDto user;
            UserDto existingUser = userService.getUserByEmail(email);
            log.info("기존 사용자 정보 : {}", existingUser);
            // 4.1. 기존 사용자가 아니라면 회원가입 처리
            if (existingUser == null) {
                // 신규 회원인 경우 회원가입 처리
                UserDto newUser = new UserDto();
                newUser.setUserNickname(nickname);
                newUser.setEmail(email);
                newUser.setUserName(nickname); // 이름은 닉네임으로 설정
                newUser.setRole("USER"); // 기본 권한 설정
                if (userService.registerUser(newUser) == 0) {
                    throw new Exception("회원가입에 실패했습니다.");
                }
                // 회원가입 후 새로 저장된 사용자 정보 가져오기
                user = newUser;
            } 
            // 4.2. 기존 사용자인 경우 
            else {
                user = existingUser; // 기존 사용자
            }

            // 5. 사용자 정보와 토큰 반환
            return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "id", id,
                "nickname", nickname,
                "email", email,
                "userNo", user.getUserNo() // userNo 추가
            ));

        } catch (Exception e) {
            log.error("카카오 로그인 실패", e);

            // 에러 발생 시 클라이언트에 에러 메시지 반환
            return ResponseEntity.badRequest().body(Map.of(
                "error", "카카오 로그인에 실패했습니다.",
                "details", e.getMessage()
            ));
        }
    }
}
