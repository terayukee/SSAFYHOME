package com.ssafy.home.comments.model.service;

import com.ssafy.home.comments.model.HouseCommentDto;
import com.ssafy.home.comments.model.mapper.HouseCommentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseCommentServiceImpl implements HouseCommentService {

    private final HouseCommentMapper houseCommentMapper;

    public HouseCommentServiceImpl(HouseCommentMapper houseCommentMapper) {
        this.houseCommentMapper = houseCommentMapper;
    }

    @Override
    public List<HouseCommentDto> getCommentsByHouse(String aptSeq, String houseType) {
        return houseCommentMapper.selectCommentsByHouse(aptSeq, houseType);
    }

    @Override
    public void addComment(HouseCommentDto houseCommentDto) {
        houseCommentMapper.insertComment(houseCommentDto);
    }
}
