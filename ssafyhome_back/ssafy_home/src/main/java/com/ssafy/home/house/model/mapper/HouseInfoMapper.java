package com.ssafy.home.house.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.home.house.model.HouseInfoDto;

@Mapper
public interface HouseInfoMapper {
    List<HouseInfoDto> getAllHouses();

    List<HouseInfoDto> getHousesInBounds(double swLat, double swLng, double neLat, double neLng);

    // apt_seq로 특정 집 정보 조회
    HouseInfoDto getHousesInfo(String aptSeq);
}
