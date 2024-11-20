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
    public List<HouseDealDto> getDealsByAptSeq(String aptSeq) {
        return houseDealMapper.getDealsByAptSeq(aptSeq);
    }

    @Override
    public List<HouseDealDto> getDealsBySpace(String aptSeq, double space) {
        return houseDealMapper.getDealsBySpace(aptSeq, space);
    }
}
