package com.ssafy.home.favorite.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FavoriteRealEstateDto {
    private int id;
    private int realestateId;
    private int userNo;
    private String dealCategory;
    private String houseType;
    
}
