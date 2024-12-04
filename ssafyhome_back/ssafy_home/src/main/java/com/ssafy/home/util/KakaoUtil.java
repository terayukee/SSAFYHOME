//package com.ssafy.home.util;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import com.ssafy.home.realestate.controller.RealEstateController;
//
//@Component
//public class KakaoUtil {
//	@Value("${kakao.client.id}")
//	private String kakaoApiKey;
//	
//	@Value("${kakao.client.secret}")
//	private String kakaoClientSecret;
//	
//	@Value("${kakao.client.redirect-uri}")
//	private String kakaoRedirectUri;
//	
//	private static final Logger logger = LoggerFactory.getLogger(RealEstateController.class);
//	
//	public String getAccessToken(String code) {		
//		
//	    String accessToken = "";
//	    String refreshToken = "";
//	    String reqUrl = "https://kauth.kakao.com/oauth/token";
//
//	    try{
//	        URL url = new URL(reqUrl);
//	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//	        
//	        //필수 헤더 세팅
//	        conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//	        conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
//
//	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//	        StringBuilder sb = new StringBuilder();
//	        
//	        //필수 쿼리 파라미터 세팅
//	        sb.append("grant_type=authorization_code");
//	        sb.append("&client_id=").append(kakaoApiKey);
//	        sb.append("&redirect_uri=").append(kakaoRedirectUri);
//	        sb.append("&code=").append(code);
//
//	        bw.write(sb.toString());
//	        bw.flush();
//
//	        int responseCode = conn.getResponseCode();
//	        logger.info("[KakaoApi.getAccessToken] responseCode = {}", responseCode);
//
//	        BufferedReader br;
//	        if (responseCode >= 200 && responseCode < 300) {
//	            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//	        } else {
//	            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//	        }
//
//	        String line = "";
//	        StringBuilder responseSb = new StringBuilder();
//	        while((line = br.readLine()) != null){
//	            responseSb.append(line);
//	        }
//	        String result = responseSb.toString();
//	        logger.info("responseBody = {}", result);
//
//	        JsonParser parser = new JsonParser();
//	        JsonElement element = parser.parse(result);
//	        accessToken = element.getAsJsonObject().get("access_token").getAsString();
//	        refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
//
//	        br.close();
//	        bw.close();
//	    }catch (Exception e){
//	        e.printStackTrace();
//	    }
//	    return accessToken;
//	}
//}
