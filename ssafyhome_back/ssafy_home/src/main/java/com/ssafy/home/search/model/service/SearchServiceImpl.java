package com.ssafy.home.search.model.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.home.search.model.HouseDto;
import com.ssafy.home.search.model.RegionDto;
import com.ssafy.home.search.model.mapper.SearchMapper;

@Service
public class SearchServiceImpl implements SearchService {
 private final SearchMapper searchMapper;

 public SearchServiceImpl(SearchMapper searchMapper) {
     this.searchMapper = searchMapper;
 }

 @Override
 public List<RegionDto> searchRegion(String keyword) {
     return searchMapper.searchRegion(keyword);
 }

 @Override
 public List<HouseDto> searchHouse(String keyword) {
	 List<HouseDto> houses = searchMapper.searchHouse(keyword);

     // 각 HouseDto에 대해 dongcodes 테이블에서 정보를 가져와 설정
     for (HouseDto house : houses) {
         Map<String, String> params = new HashMap<>();
         params.put("sggCd", house.getSggCd());
         params.put("umdCd", house.getUmdCd());

         // dongcodes에서 조회한 값 설정
         Map<String, String> dongInfo = searchMapper.getDongInfo(params);
         if (dongInfo != null) {
             house.setSidoName(dongInfo.get("sidoName"));
             house.setGugunName(dongInfo.get("gugunName"));
             house.setDongName(dongInfo.get("dongName"));
         }
     }

     return houses;
 }
}
