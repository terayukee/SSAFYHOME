package com.ssafy.home.realestate.model.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.home.realestate.model.RealEstateInfoDto;
import com.ssafy.home.realestate.model.mapper.RealEstateMapper;

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
	
	@Override
	public void registerRealEstate(RealEstateInfoDto realEstateInfoDto) {
		// 현재 날짜 가져오기
	    LocalDate currentDate = LocalDate.now();

	    // RealEstateInfoDto에 날짜 설정
	    realEstateInfoDto.setRegisterYear(currentDate.getYear());
	    realEstateInfoDto.setRegisterMonth(currentDate.getMonthValue());
	    realEstateInfoDto.setRegisterDay(currentDate.getDayOfMonth());
	    
	 // dealAmount를 100,000 형식으로 변환
	    if (realEstateInfoDto.getDealAmount() != null) {
	        String dealAmount = realEstateInfoDto.getDealAmount();
	        try {
	            // 숫자로 변환 후 포맷 적용
	            long amount = Long.parseLong(dealAmount.replace(",", "").trim());
	            DecimalFormat formatter = new DecimalFormat("#,###");
	            realEstateInfoDto.setDealAmount(formatter.format(amount));
	        } catch (NumberFormatException e) {
	            throw new IllegalArgumentException("Invalid dealAmount format: " + dealAmount);
	        }
	    }
		
	    realEstateMapper.insertRealEstate(realEstateInfoDto);
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
