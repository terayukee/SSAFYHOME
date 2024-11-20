package com.ssafy.home.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.user.model.AuthResponse;
import com.ssafy.home.user.model.service.KakaoLoginService;

@Controller
@RestController
@RequestMapping("/oauth/kakao")
public class KakaoLoginController {

	

//    @GetMapping("/callback")
//    public ResponseEntity<AuthResponse> kakaoCallback() {
//    	System.out.println("ghkrdls");
//		return null;
//    }
	
//    @Value("${kakao.client.id}")
//    private String clientId;
//
//    @Value("${kakao.redirect.uri}")
//    private String redirectUri;
//
//    private final KakaoLoginService kakaoLoginService;
//
//    @Autowired
//    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
//        this.kakaoLoginService = kakaoLoginService;
//    }

    @GetMapping("/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code ) {
    	System.out.println(code);
    	return null;
    }
}
