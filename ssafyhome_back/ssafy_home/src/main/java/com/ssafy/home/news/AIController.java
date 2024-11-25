package com.ssafy.home.news;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.util.PromptTemplateLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AIController {

    private final ChatModel chatModel;
    private final PromptTemplateLoader promptLoader;

    public AIController(ChatModel chatModel, PromptTemplateLoader promptLoader, NaverNewsService naverNewsService) {
        this.chatModel = chatModel;
        this.promptLoader = promptLoader;
    }

    @GetMapping("/news/analysis/{query}")
    public ResponseEntity<String> analyzeNews(@PathVariable("query") String query) {
    	System.out.println("확인");
    	 try {
             log.info("Analyzing news title: {}", query);

             // 유저 프롬프트 템플릿 로드 및 설정
             String userPromptTemplate = promptLoader.loadUserPrompt();
             PromptTemplate userTemplate = new PromptTemplate(userPromptTemplate);
             userTemplate.add("title", query);
             String userCommand = userTemplate.render();

             // 시스템 프롬프트 로드 및 설정
             String systemPromptTemplate = promptLoader.loadSystemPrompt();
             PromptTemplate systemTemplate = new PromptTemplate(systemPromptTemplate);
             String systemCommand = systemTemplate.render();

             // 메시지 생성 및 API 호출
             Message userMessage = new UserMessage(userCommand);
             Message systemMessage = new SystemMessage(systemCommand);
             
             // API 호출
             String response = chatModel.call(userMessage, systemMessage);
             log.info("Analysis complete for title: '{}', Result: {}", query, response);
             return ResponseEntity.ok(response);

         } catch (Exception e) {
             log.error("Error analyzing news title: '{}'", query, e);
             return ResponseEntity.badRequest()
                 .body("분석 중 오류가 발생했습니다: " + e.getMessage());
         }
    }
}
