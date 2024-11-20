package com.ssafy.home.user.model.service;

import lombok.Value;

public class KakaoLoginService {

	  	@Value("${kakao.client.id}")
	    private String clientId;

	    @Value("${kakao.client.secret}")
	    private String clientSecret;

	    private final RestTemplate restTemplate;
	    private final UserRepository userRepository;

	    @Autowired
	    public KakaoLoginService(RestTemplate restTemplate, UserRepository userRepository) {
	        this.restTemplate = restTemplate;
	        this.userRepository = userRepository;
	    }

	    public KakaoTokenResponse getKakaoAccessToken(String code) {
	        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	        params.add("grant_type", "authorization_code");
	        params.add("client_id", clientId);
	        params.add("redirect_uri", redirectUri);
	        params.add("code", code);
	        params.add("client_secret", clientSecret);

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

	        return restTemplate.postForObject(
	            "https://kauth.kakao.com/oauth/token", 
	            request, 
	            KakaoTokenResponse.class
	        );
	    }

	    public KakaoUserInfo getKakaoUserInfo(String accessToken) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setBearerAuth(accessToken);

	        HttpEntity<?> request = new HttpEntity<>(headers);

	        return restTemplate.exchange(
	            "https://kapi.kakao.com/v2/user/me",
	            HttpMethod.GET,
	            request,
	            KakaoUserInfo.class
	        ).getBody();
	    }

	    public User processKakaoUser(KakaoUserInfo userInfo) {
	        // 카카오 ID로 기존 사용자 찾기 또는 새로 생성
	        return userRepository.findByKakaoId(userInfo.getId())
	            .orElseGet(() -> {
	                User newUser = new User();
	                newUser.setKakaoId(userInfo.getId());
	                newUser.setEmail(userInfo.getKakaoAccount().getEmail());
	                newUser.setNickname(userInfo.getProperties().getNickname());
	                return userRepository.save(newUser);
	            });
	    }
}
