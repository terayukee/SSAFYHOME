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
import com.ssafy.home.favorite.model.service.FavoriteService;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/get-favorites-by-user")
    public ResponseEntity<List<FavoriteHouseDto>> getFavoritesByUser(@RequestParam int userNo) {
        List<FavoriteHouseDto> favorites = favoriteService.getFavoritesByUser(userNo);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/toggle")
    public ResponseEntity<?> toggleFavorite(@RequestBody Map<String, Object> params) {
        int userNo = (int) params.get("userNo");
        String aptSeq = (String) params.get("aptSeq");
        String houseType = (String) params.get("houseType");
        
        boolean isFavorite = favoriteService.toggleFavorite(userNo, aptSeq, houseType);
        return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }
}
