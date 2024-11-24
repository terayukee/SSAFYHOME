package com.ssafy.home.favorite.model.service;

import java.util.List;

import com.ssafy.home.favorite.model.FavoriteHouseDto;

public interface FavoriteService {
	List<FavoriteHouseDto> getFavoritesByUser(int userNo);
	boolean toggleFavorite(int userNo, String aptSeq, String houseType);
	boolean isFavorite(int userNo, String aptSeq, String houseType);

}
