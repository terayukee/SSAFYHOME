package com.ssafy.home.house.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.home.house.model.HouseRecentDealDto;

@Mapper
public interface HouseRecentDealMapper {
    List<HouseRecentDealDto> getHousesInBounds(@Param("swLat") double swLat,
                                               @Param("swLng") double swLng,
                                               @Param("neLat") double neLat,
                                               @Param("neLng") double neLng);
}