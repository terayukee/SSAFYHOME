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
    public List<RealEstateInfoDto> fetchRealEstateInfos(String space, String dealCategory, String aptSeq,
            String houseType) {
        String mappedHouseType = resolveParamName(houseType);
        return realEstateMapper.selectRealEstateInfos(space, dealCategory, aptSeq, mappedHouseType);
    }
    
    @Override
    public RealEstateInfoDto getRealEstateById(String realestateId) {
        return realEstateMapper.selectRealEstateById(realestateId);
    }

    @Override
    public List<RealEstateInfoDto> getRealEstateByUserNo(String registerUserNo) { // 새로운 메서드 추가
        return realEstateMapper.selectRealEstateByUserNo(registerUserNo);
    }

    private String resolveParamName(String houseType) {
        String mappedHouseType = "";
        switch (houseType) {
            case "apartment":
                mappedHouseType = "아파트";
                break;
            case "villa":
                mappedHouseType = "빌라";
                break;
            case "officetel":
                mappedHouseType = "오피스텔";
                break;
            default:
                throw new IllegalArgumentException("Invalid houseType: " + houseType);
        }

        return mappedHouseType;
    }
}

