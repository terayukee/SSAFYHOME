package com.ssafy.home.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.user.model.UserDto;
import com.ssafy.home.user.model.service.UserService;
import com.ssafy.home.util.AuthUtil;
import com.ssafy.home.util.JWTUtil;
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
    
    // jwt 
    private JWTUtil jwtUtil;
    
    // 인증 
    private AuthUtil authUtil;

    public KakaoLoginController(UserService userService, KakaoUtil kakaoUtil, JWTUtil jwtUtil, AuthUtil authUtil) {
        this.kakaoUtil = kakaoUtil;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authUtil = authUtil;
    }

    @GetMapping("/login")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam String code,
            @RequestParam String clientId,
            @RequestParam String redirectUri,
            @RequestParam String clientSecret) {
        try {
            // ============= KAKAO 로직 =============
            log.info("카카오 인가 코드: {}", code);

            // 1. KAKAO 액세스 토큰 및 사용자 정보 가져오기
            String kakaoAccessToken = kakaoUtil.getAccessToken(code, clientId, redirectUri, clientSecret);
            Map<String, Object> userInfo = kakaoUtil.getUserInfo(kakaoAccessToken);
            String email = (String) userInfo.get("email");
            String nickname = (String) userInfo.get("nickname");

            // ============= 커스텀 로직 =============
            // 2. 사용자 등록 또는 갱신
            UserDto user = authUtil.registerOrUpdateUser(new UserDto(), email, nickname);

            // 3. JWT 토큰 생성 및 저장
            Map<String, String> tokens = authUtil.generateAndStoreTokens(user);

            // 4. 토큰 반환
            return ResponseEntity.ok(tokens);

        } catch (Exception e) {
            log.error("카카오 로그인 실패", e);
            return ResponseEntity.badRequest().body(Map.of(
                "error", "카카오 로그인에 실패했습니다.",
                "details", e.getMessage()
            ));
        }
    }
}
