package com.ssafy.home.favorite.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.home.favorite.model.FavoriteHouseDto;
import com.ssafy.home.favorite.model.FavoriteRealEstateDto;

@Mapper
public interface FavoriteMapper {
	// House
	List<FavoriteHouseDto> getFavoriteHouseByUser(int userNo);

    void addFavoriteHouse(FavoriteHouseDto favorite);

    void removeFavoriteHouse(FavoriteHouseDto favorite);
    
    int isFavoriteHouse(int userNo, String aptSeq, String houseType);
    
    // RealEstate
    List<FavoriteRealEstateDto> getFavoriteRealEstateByUser(int userNo); // 사용자 관심 매물 조회

    void addFavoriteRealEstate(FavoriteRealEstateDto favorite); // 관심 매물 추가

    void removeFavoriteRealEstate(FavoriteRealEstateDto favorite); // 관심 매물 제거

    int isFavoriteRealEstate(int userNo, int realestateId, String dealCategory, String houseType); // 관심 매물 여부 확인
}
