package com.ssafy.home.article.model.service;

import com.ssafy.home.article.model.GuideDto;
import com.ssafy.home.article.model.mapper.GuideMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuideServiceImpl implements GuideService {

    @Autowired
    private GuideMapper guideMapper;

    @Override
    public List<GuideDto> getAllGuides() {
        return guideMapper.selectAllGuides();
    }
}
