package com.ssafy.home.comments.model.service;

import com.ssafy.home.comments.model.HouseCommentDto;

import java.util.List;

public interface HouseCommentService {
    List<HouseCommentDto> getCommentsByHouse(String aptSeq, String houseType);
    void addComment(HouseCommentDto houseCommentDto);
}
