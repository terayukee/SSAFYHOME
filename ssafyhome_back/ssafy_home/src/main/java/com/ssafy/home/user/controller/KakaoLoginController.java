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

    @GetMapping("callback")
    public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestParam String code) {
        // 사용자 인가코드 가져오기
        String tokenURL = "https://kauth.kakao.com/oauth/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("code", code);
        params.add("client_secret", kakaoClientSecret);

        // RestTemplate을 통해 HTTP 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            // 토큰을 요청
            ResponseEntity<String> response = restTemplate.exchange(tokenURL, HttpMethod.POST, entity, String.class);

            String responseBody = response.getBody();
            log.info("Kakao token response: " + responseBody);

            // 카카오에서 받아온 응답을 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String accessToken = jsonNode.get("access_token").asText();
            String refreshToken = jsonNode.get("refresh_token").asText();

            // 액세스 토큰을 이용해 사용자 정보 가져오기
            String userInfoURL = "https://kapi.kakao.com/v2/user/me";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.set("Authorization", "Bearer " + accessToken); // Bearer 토큰 인증
            HttpEntity<String> userInfoEntity = new HttpEntity<>(userInfoHeaders);
            
            // 사용자 정보 호출
            ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoURL, HttpMethod.GET, userInfoEntity, String.class);
            String userInfoResponseBody = userInfoResponse.getBody();
            log.info("Kakao user info response: " + userInfoResponseBody);

            // 닉네임 파싱
            JsonNode userJsonNode = objectMapper.readTree(userInfoResponseBody);
            String nickname = userJsonNode.path("properties").path("nickname").asText();
            
            // 유저 DB 처리 로직 (로그인 및 회원가입)
            UserDto user = new UserDto();
            user.setUserNickname(nickname);
            user.setUserName(nickname);
            user.setRefreshToken(refreshToken);
            user.setUserPassword("1234");
            user.setEmail("aaa@aaa");
            
            System.out.println("카카오 접근"+accessToken);
            System.out.println("카카오 리프레시"+refreshToken);
            
            
            Map<String, Object> resultMap = userService.checkUserInfo(user);
            String jwtAccessToken = (String) resultMap.get("access-token");
            String jwtRefreshToken = (String) resultMap.get("refresh-token");

//            System.out.println("jwt : "+accessToken);
//            System.out.println("jwt : "+refreshToken);

            // 카카오 로그인 후 반환될 URL을 설정하여 callback.html로 전달
            String callbackUrl = "http://localhost/home/callback.html?access_token=" + jwtAccessToken + "&refresh_token=" + jwtRefreshToken;
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, callbackUrl)
                    .build();

        } catch (Exception e) {
            log.error("Error during token request or user info fetch: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

		
}
