package com.ssafy.home.house.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.home.house.model.HouseRecentDealDto;
import com.ssafy.home.house.model.mapper.HouseRecentDealMapper;

@Service
public class HouseRecentDealServiceImpl implements HouseRecentDealService {

    private final HouseRecentDealMapper houseRecentDealMapper;

    public HouseRecentDealServiceImpl(HouseRecentDealMapper houseRecentDealMapper) {
        this.houseRecentDealMapper = houseRecentDealMapper;
    }

    @Override
    public List<HouseRecentDealDto> getHousesInBounds(double swLat, double swLng, double neLat, double neLng) {
        return houseRecentDealMapper.getHousesInBounds(swLat, swLng, neLat, neLng);
    }
}
