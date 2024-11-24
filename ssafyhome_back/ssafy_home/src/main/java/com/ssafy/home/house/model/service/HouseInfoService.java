package com.ssafy.home.house.model.service;

import java.util.List;

import com.ssafy.home.house.model.HouseInfoDto;

public interface HouseInfoService {

    List<HouseInfoDto> getAllHouses();

    List<HouseInfoDto> getHousesInBounds(double swLat, double swLng, double neLat, double neLng);

    // apt_seq로 특정 집 정보 조회
    HouseInfoDto getHousesInfo(String aptSeq);
}
