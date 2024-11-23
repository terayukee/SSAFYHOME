package com.ssafy.home.realestate.controller;

import com.ssafy.home.realestate.model.RealEstateInfoDto;
import com.ssafy.home.realestate.model.service.RealEstateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/realestate")
@CrossOrigin("*")
public class RealEstateController {

    private static final Logger logger = LoggerFactory.getLogger(RealEstateController.class);
    private final RealEstateService realEstateService;

    public RealEstateController(RealEstateService realEstateService) {
        this.realEstateService = realEstateService;
    }

    @GetMapping("/listby-space-dealtype")
    public List<RealEstateInfoDto> fetchRealEstateInfos(
            @RequestParam("aptSeq") String aptSeq,
            @RequestParam("space") String space,
            @RequestParam("dealCategory") String dealCategory) {

        // Debug log for incoming parameters
        logger.info("Received request with aptSeq: {}, space: {}, dealCategory: {}", aptSeq, space, dealCategory);

        // Check if parameters are null or empty
        if (aptSeq == null || aptSeq.isEmpty()) {
            logger.warn("aptSeq parameter is null or empty.");
        }
        if (space == null || space.isEmpty()) {
            logger.warn("space parameter is null or empty.");
        }
        if (dealCategory == null || dealCategory.isEmpty()) {
            logger.warn("dealCategory parameter is null or empty.");
        }

        // Fetch real estate infos
        List<RealEstateInfoDto> realestateList = realEstateService.fetchRealEstateInfos(space, dealCategory, aptSeq);

        // Debug log for the response
        if (realestateList == null || realestateList.isEmpty()) {
            logger.info("매물 조회 결과 없음 ");
        } else {            
            realestateList.forEach(realestate -> {
            	logger.debug("부동산 매물 : {}", realestate.toString());
            });
        }

        return realestateList;
    }
}
