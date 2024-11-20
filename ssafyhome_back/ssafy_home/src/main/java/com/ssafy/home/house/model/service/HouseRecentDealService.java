package com.ssafy.home.house.model.service;

import java.util.List;

import com.ssafy.home.house.model.HouseRecentDealDto;

public interface HouseRecentDealService {
    List<HouseRecentDealDto> getHousesInBounds(double swLat, double swLng, double neLat, double neLng, String tableName, String roomSize, String approvalDate);
    
    List<HouseRecentDealDto> getMonthlyHousesInBounds(double swLat, double swLng, double neLat, double neLng, String roomSize, String approvalDate);
}
