package com.ssafy.home.news;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NaverNewsController {

    private final NaverNewsService naverNewsService;

    public NaverNewsController(NaverNewsService naverNewsService) {
		super();
		this.naverNewsService = naverNewsService;
	}

	@GetMapping("/api/news")
    public Map<String, Object> getNews(@RequestParam String query) {
		System.out.println(query);
        return naverNewsService.getNews(query);
    }
}