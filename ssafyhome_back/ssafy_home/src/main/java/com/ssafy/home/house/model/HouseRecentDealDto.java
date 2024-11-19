package com.ssafy.home.house.model;

public class HouseRecentDealDto {
    private String aptSeq;
    private String aptNm;
    private String latitude; // 위도 추가
    private String longitude; // 경도 추가
    private int dealSpace; // int로 변경
    private String avgDealAmount;
    private String dealCategory;
    private String feeAmount;

    // Getter & Setter
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAptSeq() {
        return aptSeq;
    }

    public void setAptSeq(String aptSeq) {
        this.aptSeq = aptSeq;
    }

    public String getAptNm() {
        return aptNm;
    }

    public void setAptNm(String aptNm) {
        this.aptNm = aptNm;
    }
    

    public int getDealSpace() {
		return dealSpace;
	}

	public void setDealSpace(int dealSpace) {
		this.dealSpace = dealSpace;
	}

	public String getAvgDealAmount() {
        return avgDealAmount;
    }

    public void setAvgDealAmount(String avgDealAmount) {
        this.avgDealAmount = avgDealAmount;
    }
        

    public String getDealCategory() {
		return dealCategory;
	}

	public void setDealCategory(String dealCategory) {
		this.dealCategory = dealCategory;
	}

	public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

	@Override
	public String toString() {
		return "HouseRecentDealDto [aptSeq=" + aptSeq + ", aptNm=" + aptNm + ", latitude=" + latitude + ", longitude="
				+ longitude + ", dealSpace=" + dealSpace + ", avgDealAmount=" + avgDealAmount + ", dealCategory="
				+ dealCategory + ", feeAmount=" + feeAmount + "]";
	}

    
}
