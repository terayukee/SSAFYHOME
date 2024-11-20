package com.ssafy.home.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.user.model.AuthResponse;

@Controller
@RestController
@RequestMapping("/api/oauth/kakao")
public class KakaoLoginController {

	
	private final KakaoLoginService kakaoLoginService;

    @GetMapping("/callback")
    public ResponseEntity<AuthResponse> kakaoCallback(@RequestParam String code) {
//    	System.out.println(code);
        return ResponseEntity.ok(authResponse);
    }
	
	
}
