package com.ssafy.home.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

            log.info("카카오 사용자 정보 - id: {}, nickname: {}", id, nickname);

            // 4. 사용자 정보와 토큰 반환
            return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "id", id,
                "nickname", nickname
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
