package com.ssafy.home.house.model.service;

import java.util.List;

import com.ssafy.home.house.model.HouseInfoDto;

public interface HouseInfoService {

    // 모든 집 정보를 가져오는 메서드
    List<HouseInfoDto> getAllHouses();

    // 특정 범위 내의 집 정보를 가져오는 메서드
    List<HouseInfoDto> getHousesInBounds(double swLat, double swLng, double neLat, double neLng);
}