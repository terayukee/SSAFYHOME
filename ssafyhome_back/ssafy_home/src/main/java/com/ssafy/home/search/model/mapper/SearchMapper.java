package com.ssafy.home.search.model.mapper;

import com.ssafy.home.search.model.HouseDto;
import com.ssafy.home.search.model.RegionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SearchMapper {

    /**
     * 지역 검색
     * @param keyword 검색 키워드
     * @return 지역 검색 결과 리스트
     */
    List<RegionDto> searchRegion(@Param("keyword") String keyword);

    /**
     * 주택 검색
     * @param keyword 검색 키워드
     * @return 주택 검색 결과 리스트
     */
    List<HouseDto> searchHouse(@Param("keyword") String keyword);

	Map<String, String> getDongInfo(Map<String, String> params);
}
