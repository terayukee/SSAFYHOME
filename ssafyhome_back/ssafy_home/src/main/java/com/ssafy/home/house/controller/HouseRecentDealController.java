package com.ssafy.home.house.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.house.model.HouseRecentDealDto;
import com.ssafy.home.house.model.service.HouseRecentDealService;

@RestController
@RequestMapping("/api/houses")
public class HouseRecentDealController {

    private final HouseRecentDealService houseRecentDealService;

    public HouseRecentDealController(HouseRecentDealService houseRecentDealService) {
        this.houseRecentDealService = houseRecentDealService;
    }

}
