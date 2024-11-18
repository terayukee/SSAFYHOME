package com.ssafy.home.house.model.service;

import com.ssafy.home.house.model.HouseDealDto;
import com.ssafy.home.house.model.mapper.HouseDealMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseDealServiceImpl implements HouseDealService {

    private final HouseDealMapper houseDealMapper;

    public HouseDealServiceImpl(HouseDealMapper houseDealMapper) {
        this.houseDealMapper = houseDealMapper;
    }

    @Override
    public List<HouseDealDto> getRecentDeals(String aptSeq) {
        return houseDealMapper.getRecentDeals(aptSeq);
    }
}