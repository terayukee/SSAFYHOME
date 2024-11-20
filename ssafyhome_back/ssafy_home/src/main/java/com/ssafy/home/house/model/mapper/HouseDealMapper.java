package com.ssafy.home.house.model.mapper;

import com.ssafy.home.house.model.HouseDealDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseDealMapper {
    List<HouseDealDto> getDealsByAptSeq(String aptSeq);

    List<HouseDealDto> getDealsBySpace(@Param("aptSeq") String aptSeq, @Param("space") double space);
}
