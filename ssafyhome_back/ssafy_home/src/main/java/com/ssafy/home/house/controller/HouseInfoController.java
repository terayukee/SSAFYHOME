package com.ssafy.home.house.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.house.model.HouseInfoDto;
import com.ssafy.home.house.model.service.HouseInfoService;

@RestController
@RequestMapping("/api/houseinfo")
@CrossOrigin(origins = "*") // CORS 설정
public class HouseInfoController {

    @Autowired
    private HouseInfoService houseInfoService;

    @GetMapping("/list")
    public List<HouseInfoDto> getAllHouses() {
        return houseInfoService.getAllHouses();
    }
    
    @GetMapping("/bounds")
    public ResponseEntity<?> getHousesInBounds(
        @RequestParam double swLat,
        @RequestParam double swLng,
        @RequestParam double neLat,
        @RequestParam double neLng
    ) {
        try {
            List<HouseInfoDto> houses = houseInfoService.getHousesInBounds(swLat, swLng, neLat, neLng);
            return ResponseEntity.ok(houses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching houses in bounds");
        }
    }
}