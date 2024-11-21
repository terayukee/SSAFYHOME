package com.ssafy.home.user.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ssafy.home.user.model.AuthResponse;

import lombok.extern.slf4j.Slf4j;

@Controller
@RestController
@RequestMapping("/oauth/kakao")
@Slf4j
public class KakaoLoginController {


    @GetMapping("/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code , @RequestParam(required = false) String error) {
    	System.out.println(code);
    	
    	return null;
    }
    
    
}
