package com.ssafy.home.realestate.model.mapper;

import com.ssafy.home.realestate.model.RealEstateInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RealEstateMapper {
    List<RealEstateInfoDto> selectRealEstateInfos(String space, String dealCategory, String aptSeq, String houseType);
    
    RealEstateInfoDto selectRealEstateById(@Param("realestateId") String realestateId);

}

