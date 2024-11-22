package com.ssafy.home.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.home.user.model.UserDto;
import com.ssafy.home.user.model.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/oauth/kakao")
@Slf4j
public class KakaoLoginController {
	
	@Value("${kakao.client.id}")
	private String kakaoClientId;
	@Value("${kakao.client.secret}")
	private String kakaoClientSecret;
	
	// 유저 서비스 
	private UserService userService; 
	
    public KakaoLoginController(UserService userService) {
    	this.userService = userService;
	}

              
    public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestParam String code) {
		// 사용자 인가코드 가져오기 
        String tokenURL = "https://kauth.kakao.com/oauth/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("code", code);
        params.add("client_secret", kakaoClientSecret);
        
        // 템플릿 만들기
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        try {
        	
        	// Url, HttpMethod, request body, 반환 값 DataType
        	// 토큰 가져오기
        	System.out.println("확ㅁㄴㅇㅇㄴㅁ인");
            ResponseEntity<String> response = restTemplate.exchange(tokenURL, HttpMethod.POST, entity, String.class);
            System.out.println("확ㅇㄴㅁㅇㅁㄴㅇ인");
            
            String responseBody = response.getBody();
            log.info("Kakao token response: " + responseBody);
            // 매퍼
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String accessToken = jsonNode.get("access_token").asText();
            String refreshToken = jsonNode.get("refresh_token").asText();
            String tokenType = jsonNode.get("token_type").asText();
            long expiresIn = jsonNode.get("expires_in").asLong();
            
            // 토큰을 이용해 유저 정보 가져오기 
            String userInfoURL = "https://kapi.kakao.com/v2/user/me";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.set("Authorization", "Bearer " + accessToken); // Bearer 토큰 인증
            HttpEntity<String> userInfoEntity = new HttpEntity<>(userInfoHeaders);
            
            // 사용자 정보호출 
            ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoURL, HttpMethod.GET, userInfoEntity, String.class);
            String userInfoResponseBody = userInfoResponse.getBody();
            log.info("Kakao user info response: " + userInfoResponseBody);

            // 닉네임 파싱
            JsonNode userJsonNode = objectMapper.readTree(userInfoResponseBody);
            String nickname = userJsonNode.path("properties").path("nickname").asText();
            
            // 닉네임 정보가 DB에 등록되어있지 않다면 DB에 추가 
            UserDto user = new UserDto();
            user.setUserNickname(nickname);
            user.setUserName(nickname);
            user.setRefreshToken(refreshToken);
            user.setUserPassword("1234");
            user.setEmail("aaa@aaa");
            Map<String, Object> resultMap = userService.checkUserInfo(user);
            
            log.info("User nickname: " + nickname);
            
//            
//            String clientRedirectUri = "http://localhost:5173/login"; // 클라이언트에서 처리할 URI
//            String redirectUrl = clientRedirectUri + "?access_token=" + accessToken + "&refresh_token=" + refreshToken;
//            
//            // 리다이렉트 응답
//            return ResponseEntity.status(HttpStatus.FOUND)
//                    .header(HttpHeaders.LOCATION, redirectUrl)
//                    .build();

            return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error during token request or user info fetch: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
		
}
