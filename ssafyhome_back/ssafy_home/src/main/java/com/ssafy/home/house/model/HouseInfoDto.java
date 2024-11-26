package com.ssafy.home.house.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HouseInfoDto {
    private String aptSeq;
    private String sggCd;
    private String umdCd;
    private String umdNm;
    private String jibun;
    private String roadNm;
    private String aptNm;
    private Integer buildYear;
    private String latitude;
    private String longitude;   
}
