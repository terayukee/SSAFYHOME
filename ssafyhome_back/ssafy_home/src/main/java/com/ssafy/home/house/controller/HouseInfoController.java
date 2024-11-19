package com.ssafy.home.house.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.house.model.HouseInfoDto;
import com.ssafy.home.house.model.service.HouseInfoService;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin(origins = "*") // CORS 설정
public class HouseInfoController {

    @Autowired
    private HouseInfoService houseInfoService;

    @GetMapping
    public List<HouseInfoDto> getAllHouses() {
        return houseInfoService.getAllHouses();
    }
}