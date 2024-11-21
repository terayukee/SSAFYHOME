package com.ssafy.home.house.model;

public class HouseDealDto {
    private int no;
    private String aptSeq;
    private String aptDong;
    private String floor;
    private int dealYear;
    private int dealMonth;
    private int dealDay;
    private double excluUseAr;
    private String dealAmount;
    private String feeAmount;

    // Getter and Setter
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getAptSeq() {
        return aptSeq;
    }

    public void setAptSeq(String aptSeq) {
        this.aptSeq = aptSeq;
    }

    public String getAptDong() {
        return aptDong;
    }

    public void setAptDong(String aptDong) {
        this.aptDong = aptDong;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getDealYear() {
        return dealYear;
    }

    public void setDealYear(int dealYear) {
        this.dealYear = dealYear;
    }

    public int getDealMonth() {
        return dealMonth;
    }

    public void setDealMonth(int dealMonth) {
        this.dealMonth = dealMonth;
    }

    public int getDealDay() {
        return dealDay;
    }

    public void setDealDay(int dealDay) {
        this.dealDay = dealDay;
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

	@Override
	public String toString() {
		return "HouseDealDto [no=" + no + ", aptSeq=" + aptSeq + ", aptDong=" + aptDong + ", floor=" + floor
				+ ", dealYear=" + dealYear + ", dealMonth=" + dealMonth + ", dealDay=" + dealDay + ", excluUseAr="
				+ excluUseAr + ", dealAmount=" + dealAmount + ", feeAmount=" + feeAmount + "]";
	}    
    
    
}
