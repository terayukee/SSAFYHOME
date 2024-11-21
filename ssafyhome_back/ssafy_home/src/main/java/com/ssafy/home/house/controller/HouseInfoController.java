package com.ssafy.home.house.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.house.model.HouseInfoDto;
import com.ssafy.home.house.model.HouseRecentDealDto;
import com.ssafy.home.house.model.service.HouseInfoService;
import com.ssafy.home.house.model.service.HouseRecentDealService;

@RestController
@RequestMapping("/api/houseinfo")
@CrossOrigin(origins = "*") // CORS 설정
public class HouseInfoController {

	private final HouseInfoService houseInfoService;
	private final HouseRecentDealService houseRecentDealService;

	public HouseInfoController(HouseInfoService houseInfoService, HouseRecentDealService houseRecentDealService) {
		this.houseInfoService = houseInfoService;
		this.houseRecentDealService = houseRecentDealService;
	}

	@GetMapping("/list")
	public List<HouseInfoDto> getAllHouses() {
		return houseInfoService.getAllHouses();
	}

	@GetMapping("/bounds")
	public List<HouseRecentDealDto> getHousesInBounds(@RequestParam("dealCategory") String dealCategory, // 매매, 전세, 월세
			@RequestParam("swLat") double swLat, @RequestParam("swLng") double swLng,
			@RequestParam("neLat") double neLat, @RequestParam("neLng") double neLng,
			@RequestParam(value = "roomSize", required = false) String roomSize,
			@RequestParam("approvalDate") String approvalDate,
			@RequestParam String houseType) {
		
		List<HouseRecentDealDto> resultList = houseRecentDealService.getHousesInBounds(swLat, swLng, neLat, neLng,
				dealCategory, roomSize, approvalDate, houseType);
		
		
		return resultList;
	}
}