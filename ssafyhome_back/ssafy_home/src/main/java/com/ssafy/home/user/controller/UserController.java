package com.ssafy.home.user.controller;

import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
@RequestMapping("/user")
@Slf4j
public class UserController {
	
	// 유저 서비스
    private final UserService userService;
    
    // jwt 
    private JWTUtil jwtUtil;
    
    // 인증 
    private AuthUtil authUtil;
	
	public UserController(UserService userService, JWTUtil jwtUtil, AuthUtil authUtil) {        
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authUtil = authUtil;
    }
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto userDto) {
	    try {
	        // 1. 사용자 인증
	        UserDto loginUser = userService.login(userDto);

	        if (loginUser != null && !loginUser.getDeleted()) {
	            // 2. JWT 토큰 생성 및 저장
	            Map<String, String> tokens = authUtil.generateAndStoreTokens(loginUser);

	            // 3. 토큰 반환
	            return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인 실패"));
	        }
	    } catch (Exception e) {
	        log.error("로그인 에러 발생 : {}", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
	            "error", "서버 에러가 발생했습니다.",
	            "details", e.getMessage()
	        ));
	    }
	}

	
	@GetMapping("/info/{userNo}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable("userNo") String userId,
			@RequestHeader("Authorization") String header) {
		log.debug("userId : {}, header : {} ", userId, header);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (jwtUtil.checkToken(header)) {
			log.info("사용 가능한 토큰!!!");
			try {
				UserDto userDto = userService.getUserInfo(userId);
				resultMap.put("userInfo", userDto);
				log.info("사용자 정보 decode : {}", userDto);
				System.out.println(userDto);
				status = HttpStatus.OK;
			} catch (Exception e) {
				log.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			log.error("사용 불가능 토큰!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	// 만료된 토큰 재 발급
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody UserDto userDto, @RequestHeader("refreshToken") String token) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
//		String token = request.getHeader("refreshToken");
		
		log.debug("token : {}, userDto : {}", token, userDto);
		if (jwtUtil.checkToken(token)) {
			if (token.equals(userService.getRefreshToken(userDto.getUserNo()))) {
				String accessToken = jwtUtil.createAccessToken(userDto);
				log.debug("token : {}", accessToken);
				log.debug("정상적으로 access token 재발급!!!");
				resultMap.put("access-token", accessToken);
				status = HttpStatus.CREATED;
			}
		} else {
			log.debug("refresh token 도 사용 불가!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	
	@GetMapping("/logout")
	public ResponseEntity<?> removeToken(@RequestParam int userNo) {
	    Map<String, Object> resultMap = new HashMap<>();
	    HttpStatus status = HttpStatus.ACCEPTED;

	    try {
	        // RefreshToken 삭제 로직
	        userService.deleteRefreshToken(userNo);
	        status = HttpStatus.OK;
	        resultMap.put("message", "로그아웃 성공");
	        log.info("사용자 {} 로그아웃 성공", userNo);
	    } catch (Exception e) {
	        log.error("로그아웃 실패 : {}", e.getMessage());
	        resultMap.put("message", "로그아웃 중 오류가 발생했습니다.");
	        status = HttpStatus.INTERNAL_SERVER_ERROR;
	    }

	    return ResponseEntity.status(status).body(resultMap);
	}

	
	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> regist(@RequestBody UserDto user) {
	    Map<String, Object> resultMap = new HashMap<>();

	    try {
	        // 이메일로 삭제된 계정 확인
	        UserDto deletedUser = userService.getDeletedUserByEmail(user.getEmail());

	        if (deletedUser != null) {
	            // 삭제된 계정이 존재하면
	            resultMap.put("message", "이전에 탈퇴한 계정이 존재합니다. 복구하시겠습니까?");
	            resultMap.put("userInfo", deletedUser);
	            return ResponseEntity.status(HttpStatus.FOUND).body(resultMap);  // 302 Found
	        }
	        
	        // 일반적인 회원가입 처리
	        int no = userService.registerUser(user);
	        if(no == 1) {
	            return ResponseEntity.status(HttpStatus.CREATED).build();
	        } else {
	            resultMap.put("message", "회원가입에 실패했습니다.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	        resultMap.put("message", "서버 오류가 발생했습니다.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
	    }
	}

	// 회원 복구 엔드포인트 추가
	@PostMapping("/restore")
	public ResponseEntity<Map<String, Object>> restoreUser(@RequestBody UserDto user) {
	    Map<String, Object> resultMap = new HashMap<>();
	    
	    try {
	        boolean result = userService.restoreUser(user.getUserNo());
	        if (result) {
	            resultMap.put("message", "계정이 성공적으로 복구되었습니다.");
	            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
	        } else {
	            resultMap.put("message", "계정 복구에 실패했습니다.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
	        }
	    } catch (Exception e) {
	        resultMap.put("message", "서버 오류가 발생했습니다.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
	    }
	}
	
	@PutMapping("/update")
	public ResponseEntity<Map<String, Object>> updateUser(@RequestBody UserDto userDto) {
	    Map<String, Object> resultMap = new HashMap<>();
	    HttpStatus status = HttpStatus.ACCEPTED;
	    
	    try {
	        // 닉네임 업데이트
	        int result = userService.userUpdate(userDto);
	        if (result > 0) {
	            // 업데이트된 유저 정보 조회
	            UserDto updatedUser = userService.getUserInfo(String.valueOf(userDto.getUserNo()));
	            resultMap.put("userInfo", updatedUser);
	            status = HttpStatus.OK;
	        } else {
	            resultMap.put("message", "업데이트 실패");
	            status = HttpStatus.BAD_REQUEST;
	        }
	    } catch (Exception e) {
	        log.error("사용자 정보 수정 실패 : {}", e);
	        resultMap.put("message", e.getMessage());
	        status = HttpStatus.INTERNAL_SERVER_ERROR;
	    }
	    
	    return new ResponseEntity<>(resultMap, status);
	}
	
	
	@DeleteMapping("/withdraw/{userNo}")
	public ResponseEntity<Map<String, Object>> withdrawUser(@PathVariable("userNo") int userNo) {
	    Map<String, Object> resultMap = new HashMap<>();
	    
	    try {
	        boolean result = userService.withdrawUser(userNo);
	        if (result) {
	            resultMap.put("message", "회원 탈퇴가 완료되었습니다.");
	            System.out.println("성공");
	            return new ResponseEntity<>(resultMap, HttpStatus.OK);
	        } else {
	            resultMap.put("message", "회원 탈퇴 처리에 실패했습니다.");
	            System.out.println("회원처리에 실패");
	            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
	        }
	    } catch (Exception e) {
	    	System.out.println("서버에러");
	        resultMap.put("message", e.getMessage());
	        return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
