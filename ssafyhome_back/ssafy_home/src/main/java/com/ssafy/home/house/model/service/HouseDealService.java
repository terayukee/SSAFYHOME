package com.ssafy.home.house.model.service;

import com.ssafy.home.house.model.HouseDealDto;

import java.util.List;

public interface HouseDealService {
	List<HouseDealDto> getDealsByAptSeq(String aptSeq, String dealType);

	List<HouseDealDto> getDealsBySpace(String aptSeq, String dealType, double space);
}
