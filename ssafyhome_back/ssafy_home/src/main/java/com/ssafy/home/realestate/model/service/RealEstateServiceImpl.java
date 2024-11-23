package com.ssafy.home.realestate.model.service;

import com.ssafy.home.realestate.model.RealEstateInfoDto;
import com.ssafy.home.realestate.model.mapper.RealEstateMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealEstateServiceImpl implements RealEstateService {

    private final RealEstateMapper realEstateMapper;

    public RealEstateServiceImpl(RealEstateMapper realEstateMapper) {
        this.realEstateMapper = realEstateMapper;
    }

    @Override
    public List<RealEstateInfoDto> fetchRealEstateInfos(String space, String dealCategory, String aptSeq) {
        return realEstateMapper.selectRealEstateInfos(space, dealCategory, aptSeq);
    }
}
