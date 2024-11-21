package com.ssafy.home.search.model.service;

import com.ssafy.home.search.model.HouseDto;
import com.ssafy.home.search.model.RegionDto;

import java.util.List;

public interface SearchService {
    /**
     * 지역 검색
     * @param keyword 검색 키워드
     * @return 검색 결과 리스트
     */
    List<RegionDto> searchRegion(String keyword);

    /**
     * 주택 검색
     * @param keyword 검색 키워드
     * @return 검색 결과 리스트
     */
    List<HouseDto> searchHouse(String keyword);
}
