package com.ssafy.home.comments.model.mapper;

import com.ssafy.home.comments.model.HouseCommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseCommentMapper {
    List<HouseCommentDto> selectCommentsByHouse(@Param("aptSeq") String aptSeq, @Param("houseType") String houseType);
    void insertComment(HouseCommentDto houseCommentDto);
}
