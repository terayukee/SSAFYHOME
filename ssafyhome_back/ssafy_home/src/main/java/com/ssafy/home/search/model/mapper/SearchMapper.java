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
     * 주택 검색: 아파트 정보
     * @param keyword 검색 키워드
     * @return 아파트 검색 결과 리스트
     */
    List<HouseDto> searchHouseInfos(@Param("keyword") String keyword);

    /**
     * 주택 검색: 빌라 정보
     * @param keyword 검색 키워드
     * @return 빌라 검색 결과 리스트
     */
    List<HouseDto> searchVillaInfos(@Param("keyword") String keyword);

    /**
     * 주택 검색: 오피스텔 정보
     * @param keyword 검색 키워드
     * @return 오피스텔 검색 결과 리스트
     */
    List<HouseDto> searchOfficetelInfos(@Param("keyword") String keyword);
}
