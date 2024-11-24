package com.ssafy.home.favorite.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.favorite.model.FavoriteHouseDto;
import com.ssafy.home.favorite.model.FavoriteRealEstateDto;
import com.ssafy.home.favorite.model.service.FavoriteService;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/get-favorite-house")
    public ResponseEntity<List<FavoriteHouseDto>> getFavoriteHouseByUser(@RequestParam int userNo) {
        List<FavoriteHouseDto> favorites = favoriteService.getFavoriteHouseByUser(userNo);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/toggle-house")
    public ResponseEntity<?> toggleFavoriteHouse(@RequestBody Map<String, Object> params) {
        int userNo = (int) params.get("userNo");
        String aptSeq = (String) params.get("aptSeq");
        String houseType = (String) params.get("houseType");
        
        boolean isFavorite = favoriteService.toggleFavoriteHouse(userNo, aptSeq, houseType);
        return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }
    
    // 관심 매물(부동산) 가져오기
    @GetMapping("/get-favorite-realestate")
    public ResponseEntity<List<FavoriteRealEstateDto>> getFavoriteRealEstateByUser(
            @RequestParam int userNo) {
        List<FavoriteRealEstateDto> favorites = favoriteService.getFavoriteRealEstateByUser(userNo);
        return ResponseEntity.ok(favorites);
    }

    // 관심 매물(부동산) 토글
    @PostMapping("/toggle-realestate")
    public ResponseEntity<?> toggleFavoriteRealEstate(@RequestBody Map<String, Object> params) {
        int userNo = (int) params.get("userNo");
        int realestateId = (int) params.get("realestateId");
        String dealCategory = (String) params.get("dealCategory");
        String houseType = (String) params.get("houseType");


        boolean isFavorite = favoriteService.toggleFavoriteRealEstate(userNo, realestateId, dealCategory, houseType);
        return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }
}
