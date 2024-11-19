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

    @GetMapping("/recent/{aptSeq}")
    public ResponseEntity<List<HouseDealDto>> getRecentDeals(@PathVariable String aptSeq) {
        List<HouseDealDto> deals = houseDealService.getRecentDeals(aptSeq);
        return ResponseEntity.ok(deals);
    }
}