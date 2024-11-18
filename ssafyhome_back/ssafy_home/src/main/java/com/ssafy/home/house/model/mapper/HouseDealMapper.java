package com.ssafy.home.house.model.mapper;

import com.ssafy.home.house.model.HouseDealDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HouseDealMapper {
    List<HouseDealDto> getRecentDeals(String aptSeq);
}
