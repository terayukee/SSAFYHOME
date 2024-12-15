package com.ssafy.home.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.user.model.UserDto;
import com.ssafy.home.user.model.service.UserService;
import com.ssafy.home.util.AuthUtil;
import com.ssafy.home.util.JWTUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;
	private final JWTUtil jwtUtil;
	private final AuthUtil authUtil;

	public AuthController(UserService userService, JWTUtil jwtUtil, AuthUtil authUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
		this.authUtil = authUtil;
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto userDto) {
		try {
			UserDto loginUser = userService.login(userDto);

			if (loginUser != null && !loginUser.getDeleted()) {
				Map<String, String> tokens = authUtil.generateAndStoreTokens(loginUser);
				return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인 실패"));
			}
		} catch (Exception e) {
			log.error("로그인 에러 발생 : {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "서버 에러가 발생했습니다.", "details", e.getMessage()));
		}
	}

	// 로그아웃
	@GetMapping("/logout")
	public ResponseEntity<?> removeToken(@RequestParam int userNo) {
		try {
			userService.deleteRefreshToken(userNo);
			log.info("사용자 {} 로그아웃 성공", userNo);
			return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
		} catch (Exception e) {
			log.error("로그아웃 실패 : {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "로그아웃 중 오류 발생"));
		}
	}

	// 액세스 토큰 재발급
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody UserDto userDto, @RequestHeader("refreshToken") String token) {
		try {
			if (jwtUtil.checkToken(token) && token.equals(userService.getRefreshToken(userDto.getUserNo()))) {
				String accessToken = jwtUtil.createAccessToken(userDto);
				return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("access-token", accessToken));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "리프레시 토큰 유효하지 않음"));
			}
		} catch (Exception e) {
			log.error("토큰 재발급 실패 : {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "토큰 재발급 중 오류 발생", "details", e.getMessage()));
		}
	}

	@GetMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorization) {
		String accessToken = authorization.replace("Bearer ", "");
		if (jwtUtil.checkToken(accessToken)) {
			return ResponseEntity.ok().build(); // 유효한 토큰
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
		}
	}
}
