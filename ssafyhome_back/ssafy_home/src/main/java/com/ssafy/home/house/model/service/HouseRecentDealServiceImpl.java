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
	public List<HouseRecentDealDto> getHousesInBounds(double swLat, double swLng, double neLat, double neLng,
			String dealCategory, String roomSize, String approvalDate, String houseType) {
		
		String tablePrefix = resolveTableName(houseType);
		
		String infoTableName = tablePrefix + "infos";
		String targetTableName = tablePrefix + "recentdeals";
		
		// 1. 월세
		if (dealCategory.equals("월세")) {
			return houseRecentDealMapper.getMonthlyHousesInBounds(swLat, swLng, neLat, neLng, roomSize, approvalDate,
					infoTableName, targetTableName);
		}
		// 2. 매매 or 전세
		else {
			return houseRecentDealMapper.getHousesInBounds(swLat, swLng, neLat, neLng, dealCategory, roomSize,
					approvalDate, infoTableName, targetTableName);
		}
	}

	
	// houseType에 따라 테이블 이름 반환
    private String resolveTableName(String houseType) {
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
    	    
    	
    	return tablePrefix;
    }
}
