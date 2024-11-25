package com.ssafy.home.realestate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RealEstateInfoDto {
	private int id;
    private String aptSeq;
    private String maxFloor;
    private String thisFloor;
    private int registerYear;
    private int registerMonth;
    private int registerDay;
    private double excluUseAr;
    private String dealAmount;
    private String feeAmount;
    private String maintenanceCost;
    private String dealCategory;
    private String houseType;	
    private int registerUserNo;
}
