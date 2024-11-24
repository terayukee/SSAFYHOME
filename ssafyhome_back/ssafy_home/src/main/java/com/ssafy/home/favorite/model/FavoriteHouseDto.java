package com.ssafy.home.favorite.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FavoriteHouseDto {
    private int userNo;
    private String aptSeq;
    private String houseType;
}
