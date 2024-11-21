package com.ssafy.home.house.model.mapper;

import com.ssafy.home.house.model.HouseDealDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseDealMapper {
    List<HouseDealDto> getDealsByAptSeq(@Param("aptSeq") String aptSeq, @Param("tableName") String tableName);

    List<HouseDealDto> getDealsBySpace(@Param("aptSeq") String aptSeq, @Param("tableName") String tableName, @Param("space") double space);
}
