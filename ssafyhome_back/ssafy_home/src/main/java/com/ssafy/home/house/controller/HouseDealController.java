package com.ssafy.home.house.controller;

import com.ssafy.home.house.model.HouseDealDto;
import com.ssafy.home.house.model.service.HouseDealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/housedeal")
public class HouseDealController {

    private final HouseDealService houseDealService;

    public HouseDealController(HouseDealService houseDealService) {
        this.houseDealService = houseDealService;
    }

    // 특정 아파트의 모든 거래 정보를 가져오는 API
    @GetMapping("/{aptSeq}/deals")
    public ResponseEntity<List<HouseDealDto>> getDealsByAptSeq(@PathVariable String aptSeq) {
        List<HouseDealDto> deals = houseDealService.getDealsByAptSeq(aptSeq);
        return ResponseEntity.ok(deals);
    }

    // 특정 평형의 거래 정보를 가져오는 API
    @GetMapping("/{aptSeq}/deals-by-space")
    public ResponseEntity<List<HouseDealDto>> getDealsBySpace(
            @PathVariable String aptSeq,
            @RequestParam("space") double space
    ) {
    	List<HouseDealDto> deals = houseDealService.getDealsBySpace(aptSeq, space);
        for(HouseDealDto deal : deals) {
        	System.out.println(deal.toString());
        }
        return ResponseEntity.ok(deals);
    }
}
