package com.ssafy.home.news;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.List;
import java.util.Map;

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
