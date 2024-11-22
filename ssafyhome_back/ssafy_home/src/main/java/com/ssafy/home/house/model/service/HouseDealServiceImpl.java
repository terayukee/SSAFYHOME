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
    public List<HouseDealDto> getDealsByAptSeq(String aptSeq, String dealCategory, String houseType) {
        String tableName = resolveTableName(dealCategory, houseType);
        return houseDealMapper.getDealsByAptSeq(aptSeq, tableName);
    }

    @Override
    public List<HouseDealDto> getDealsBySpace(String aptSeq, String dealCategory, String houseType, double space) {
        String tableName = resolveTableName(dealCategory, houseType);
        return houseDealMapper.getDealsBySpace(aptSeq, tableName, space);
    }

    // dealCategory, houseType에 따라 테이블 이름 반환
    private String resolveTableName(String dealCategory, String houseType) {
    	String tablePrefix = "";
        switch (houseType) {
            case "apartment":
            	tablePrefix = "house";
            	break;
            case "villa":
            	tablePrefix = "villa";
            	break;
            case "officetel":
            	tablePrefix = "officetel";
            	break;
            default:
                throw new IllegalArgumentException("Invalid houseType: " + houseType);
        }
    	
    	String tableSuffix = "";
        switch (dealCategory) {
            case "매매":
            	tableSuffix = "deals";
            	break;
            case "전세":
            	tableSuffix = "deals_jeonse";
            	break;
            case "월세":
            	tableSuffix = "deals_month";
            	break;
            default:
                throw new IllegalArgumentException("Invalid dealCategory: " + dealCategory);
        }
    	
    	return tablePrefix + tableSuffix;
    }
}
