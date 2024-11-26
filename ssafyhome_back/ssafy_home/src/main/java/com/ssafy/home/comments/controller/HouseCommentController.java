package com.ssafy.home.comments.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.comments.model.HouseCommentDto;
import com.ssafy.home.comments.model.service.HouseCommentService;
import com.ssafy.home.realestate.controller.RealEstateController;

@RestController
@RequestMapping("/api/housecomment")
@CrossOrigin("*")
public class HouseCommentController {

	private static final Logger logger = LoggerFactory.getLogger(RealEstateController.class);
    private final HouseCommentService houseCommentService;

    public HouseCommentController(HouseCommentService houseCommentService) {
        this.houseCommentService = houseCommentService;
    }

    @PostMapping("/list")
    public List<HouseCommentDto> getComments(@RequestBody Map<String, String> params) {
        String aptSeq = params.get("aptSeq");
        String houseType = params.get("houseType");
        logger.info("aptSeq: {}, houseType: {}", aptSeq, houseType);

        return houseCommentService.getCommentsByHouse(aptSeq, houseType);
    }


    @PostMapping("/add")
    public String addComment(@RequestBody HouseCommentDto houseCommentDto) {
        houseCommentService.addComment(houseCommentDto);
        return "댓글이 성공적으로 추가되었습니다.";
    }
}
