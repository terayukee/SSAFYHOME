package com.ssafy.home.favorite.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.home.favorite.model.FavoriteHouseDto;
import com.ssafy.home.favorite.model.FavoriteRealEstateDto;
import com.ssafy.home.favorite.model.mapper.FavoriteMapper;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    // House
    @Override
    public List<FavoriteHouseDto> getFavoriteHouseByUser(int userNo) {
        return favoriteMapper.getFavoriteHouseByUser(userNo);
    }

    @Override
    public boolean toggleFavoriteHouse(int userNo, String aptSeq, String houseType) {
        FavoriteHouseDto favorite = new FavoriteHouseDto();
        favorite.setUserNo(userNo);
        favorite.setAptSeq(aptSeq);
        favorite.setHouseType(houseType);

        if (isFavoriteHouse(userNo, aptSeq, houseType)) {
            favoriteMapper.removeFavoriteHouse(favorite);
            return false; // Favorite removed
        } else {
            favoriteMapper.addFavoriteHouse(favorite);
            return true; // Favorite added
        }
    }

    // RealEstate
    @Override
    public boolean isFavoriteHouse(int userNo, String aptSeq, String houseType) {
        return favoriteMapper.isFavoriteHouse(userNo, aptSeq, houseType) > 0;
    }

    
    @Override
    public List<FavoriteRealEstateDto> getFavoriteRealEstateByUser(int userNo) {
        return favoriteMapper.getFavoriteRealEstateByUser(userNo);
    }

    @Override
    public boolean toggleFavoriteRealEstate(int userNo, int realestateId, String dealCategory, String houseType) {
        FavoriteRealEstateDto favorite = new FavoriteRealEstateDto();
        favorite.setUserNo(userNo);
        favorite.setRealestateId(realestateId);
        favorite.setDealCategory(dealCategory);
        favorite.setHouseType(houseType);

        if (isFavoriteRealEstate(userNo, realestateId, dealCategory, houseType)) {
            favoriteMapper.removeFavoriteRealEstate(favorite);
            return false; // 관심 해제
        } else {
            favoriteMapper.addFavoriteRealEstate(favorite);
            return true; // 관심 등록
        }
    }

    @Override
    public boolean isFavoriteRealEstate(int userNo, int realestateId, String dealCategory, String houseType) {
        return favoriteMapper.isFavoriteRealEstate(userNo, realestateId, dealCategory, houseType) > 0;
    }
}
