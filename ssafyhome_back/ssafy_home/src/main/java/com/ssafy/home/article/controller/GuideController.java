package com.ssafy.home.article.controller;

import com.ssafy.home.article.model.GuideDto;
import com.ssafy.home.article.model.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article/guide")
@CrossOrigin("*")
public class GuideController {

    @Autowired
    private GuideService guideService;

    // 가이드 카드 목록 조회
    @GetMapping("list")
    public ResponseEntity<List<GuideDto>> getGuides() {
        List<GuideDto> guides = guideService.getAllGuides();
        return ResponseEntity.ok(guides);
    }
}
