package com.ssafy.home.favorite.model.service;

import java.util.List;

import com.ssafy.home.favorite.model.FavoriteHouseDto;
import com.ssafy.home.favorite.model.FavoriteRealEstateDto;

public interface FavoriteService {
	List<FavoriteHouseDto> getFavoriteHouseByUser(int userNo);
	boolean toggleFavoriteHouse(int userNo, String aptSeq, String houseType);
	boolean isFavoriteHouse(int userNo, String aptSeq, String houseType);

	List<FavoriteRealEstateDto> getFavoriteRealEstateByUser(int userNo); // 사용자 관심 매물 조회
    boolean toggleFavoriteRealEstate(int userNo, int realestateId, String dealCategory, String houseType); // 관심 매물 토글
    boolean isFavoriteRealEstate(int userNo, int realestateId, String dealCategory, String houseType); // 관심 매물 여부 확인
}
