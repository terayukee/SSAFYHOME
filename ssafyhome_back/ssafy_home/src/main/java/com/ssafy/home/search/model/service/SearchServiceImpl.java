package com.ssafy.home.search.model.service;


import java.util.List;

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
     return searchMapper.searchHouse(keyword);
 }
}
