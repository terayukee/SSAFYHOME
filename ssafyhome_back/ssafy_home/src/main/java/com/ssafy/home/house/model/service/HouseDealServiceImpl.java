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
    public List<HouseDealDto> getDealsByAptSeq(String aptSeq, String dealCategory) {
        String tableName = resolveTableName(dealCategory);
        return houseDealMapper.getDealsByAptSeq(aptSeq, tableName);
    }

    @Override
    public List<HouseDealDto> getDealsBySpace(String aptSeq, String dealCategory, double space) {
        String tableName = resolveTableName(dealCategory);
        return houseDealMapper.getDealsBySpace(aptSeq, tableName, space);
    }

    // dealCategory에 따라 테이블 이름 반환
    private String resolveTableName(String dealCategory) {
        switch (dealCategory) {
            case "매매":
                return "housedeals";
            case "전세":
                return "housedeals_jeonse";
            case "월세":
                return "housedeals_month";
            default:
                throw new IllegalArgumentException("Invalid dealCategory: " + dealCategory);
        }
    }
}
