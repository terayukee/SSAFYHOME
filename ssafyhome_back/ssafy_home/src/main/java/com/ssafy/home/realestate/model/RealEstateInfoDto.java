package com.ssafy.home.realestate.model;

public class RealEstateInfoDto {
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
	public String getAptSeq() {
		return aptSeq;
	}
	public void setAptSeq(String aptSeq) {
		this.aptSeq = aptSeq;
	}
	public String getMaxFloor() {
		return maxFloor;
	}
	public void setMaxFloor(String maxFloor) {
		this.maxFloor = maxFloor;
	}
	public String getThisFloor() {
		return thisFloor;
	}
	public void setThisFloor(String thisFloor) {
		this.thisFloor = thisFloor;
	}
	public int getRegisterYear() {
		return registerYear;
	}
	public void setRegisterYear(int registerYear) {
		this.registerYear = registerYear;
	}
	public int getRegisterMonth() {
		return registerMonth;
	}
	public void setRegisterMonth(int registerMonth) {
		this.registerMonth = registerMonth;
	}
	public int getRegisterDay() {
		return registerDay;
	}
	public void setRegisterDay(int registerDay) {
		this.registerDay = registerDay;
	}
	public double getExcluUseAr() {
		return excluUseAr;
	}
	public void setExcluUseAr(double excluUseAr) {
		this.excluUseAr = excluUseAr;
	}
	public String getDealAmount() {
		return dealAmount;
	}
	public void setDealAmount(String dealAmount) {
		this.dealAmount = dealAmount;
	}
	public String getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}
	public String getMaintenanceCost() {
		return maintenanceCost;
	}
	public void setMaintenanceCost(String maintenanceCost) {
		this.maintenanceCost = maintenanceCost;
	}
	public String getDealCategory() {
		return dealCategory;
	}
	public void setDealCategory(String dealCategory) {
		this.dealCategory = dealCategory;
	}
	public String getHouseType() {
		return houseType;
	}
	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}
	@Override
	public String toString() {
		return "RealEstateInfoDto [aptSeq=" + aptSeq + ", maxFloor=" + maxFloor + ", thisFloor=" + thisFloor
				+ ", registerYear=" + registerYear + ", registerMonth=" + registerMonth + ", registerDay=" + registerDay
				+ ", excluUseAr=" + excluUseAr + ", dealAmount=" + dealAmount + ", feeAmount=" + feeAmount
				+ ", maintenanceCost=" + maintenanceCost + ", dealCategory=" + dealCategory + ", houseType=" + houseType
				+ "]";
	}

    
}
