package com.ssafy.home.house.model.service;

import com.ssafy.home.house.model.HouseDealDto;

import java.util.List;

public interface HouseDealService {
    List<HouseDealDto> getRecentDeals(String aptSeq);
}
