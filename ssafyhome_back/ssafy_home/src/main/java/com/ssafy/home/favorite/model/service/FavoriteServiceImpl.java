package com.ssafy.home.favorite.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.home.favorite.model.FavoriteHouseDto;
import com.ssafy.home.favorite.model.mapper.FavoriteMapper;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public List<FavoriteHouseDto> getFavoritesByUser(int userNo) {
        return favoriteMapper.getFavoritesByUser(userNo);
    }

    @Override
    public boolean toggleFavorite(int userNo, String aptSeq, String houseType) {
        FavoriteHouseDto favorite = new FavoriteHouseDto();
        favorite.setUserNo(userNo);
        favorite.setAptSeq(aptSeq);
        favorite.setHouseType(houseType);

        if (isFavorite(userNo, aptSeq, houseType)) {
            favoriteMapper.removeFavorite(favorite);
            return false; // Favorite removed
        } else {
            favoriteMapper.addFavorite(favorite);
            return true; // Favorite added
        }
    }

    @Override
    public boolean isFavorite(int userNo, String aptSeq, String houseType) {
        return favoriteMapper.isFavorite(userNo, aptSeq, houseType) > 0;
    }

}
