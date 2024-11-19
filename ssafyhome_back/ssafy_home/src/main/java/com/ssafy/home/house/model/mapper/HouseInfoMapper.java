package com.ssafy.home.house.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.home.house.model.HouseInfoDto;

@Mapper
public interface HouseInfoMapper {
    List<HouseInfoDto> getAllHouses();

	List<HouseInfoDto> getHousesInBounds(double swLat, double swLng, double neLat, double neLng);
}