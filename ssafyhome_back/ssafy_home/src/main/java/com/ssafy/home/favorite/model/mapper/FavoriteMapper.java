package com.ssafy.home.favorite.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.home.favorite.model.FavoriteHouseDto;

@Mapper
public interface FavoriteMapper {
	List<FavoriteHouseDto> getFavoritesByUser(int userNo);

    void addFavorite(FavoriteHouseDto favorite);

    void removeFavorite(FavoriteHouseDto favorite);
    
    int isFavorite(int userNo, String aptSeq, String houseType);
}
