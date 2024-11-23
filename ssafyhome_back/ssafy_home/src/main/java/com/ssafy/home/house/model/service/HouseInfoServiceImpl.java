package com.ssafy.home.house.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.home.house.model.HouseInfoDto;
import com.ssafy.home.house.model.mapper.HouseInfoMapper;

@Service
public class HouseInfoServiceImpl implements HouseInfoService {

    @Autowired
    private HouseInfoMapper houseInfoMapper;

    @Override
    public List<HouseInfoDto> getAllHouses() {
        return houseInfoMapper.getAllHouses();
    }

    @Override
    public List<HouseInfoDto> getHousesInBounds(double swLat, double swLng, double neLat, double neLng) {
        return houseInfoMapper.getHousesInBounds(swLat, swLng, neLat, neLng);
    }

    @Override
    public HouseInfoDto getHousesInfo(String aptSeq) {
        return houseInfoMapper.getHousesInfo(aptSeq);
    }
}
