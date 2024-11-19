package com.ssafy.home.house.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.home.house.model.HouseInfoDto;
import com.ssafy.home.house.model.mapper.HouseInfoMapper;

@Service
public class HouseInfoService {

    @Autowired
    private HouseInfoMapper houseInfoMapper;

    public List<HouseInfoDto> getAllHouses() {
        return houseInfoMapper.getAllHouses();
    }
    
    public List<HouseInfoDto> getHousesInBounds(double swLat, double swLng, double neLat, double neLng) {
        return houseInfoMapper.getHousesInBounds(swLat, swLng, neLat, neLng);
    }
}
