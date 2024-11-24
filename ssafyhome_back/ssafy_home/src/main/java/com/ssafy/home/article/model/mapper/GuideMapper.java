package com.ssafy.home.article.model.mapper;

import com.ssafy.home.article.model.GuideDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GuideMapper {
    List<GuideDto> selectAllGuides();
}
