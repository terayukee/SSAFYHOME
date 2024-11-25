package com.ssafy.home.news;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverNewsService {

	
	@Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    public Map<String, Object> getNews(String query) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    	// 오늘 날짜
    	LocalDate endDate = LocalDate.now();
    	// 한 달 전 날짜
    	LocalDate startDate = endDate.minusMonths(1);

    	String url = "https://openapi.naver.com/v1/search/news.json?query=" + query 
    	    + "&display=100&sort=date"
    	    + "&start_date=" + startDate.format(formatter)
    	    + "&end_date=" + endDate.format(formatter);

    	//String url = "https://openapi.naver.com/v1/search/news.json?query=" + query + "&display=100&sort=date";
        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // REST 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        // JSON 응답 반환
        return response.getBody();
    }
}
