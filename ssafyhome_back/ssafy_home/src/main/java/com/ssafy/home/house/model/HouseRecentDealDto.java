package com.ssafy.home.house.model;

public class HouseRecentDealDto {
    private String aptSeq;
    private String aptNm;
    private String latitude; // 위도 추가
    private String longitude; // 경도 추가
    private String dealType;
    private String avgDealAmount;

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

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getAvgDealAmount() {
		return avgDealAmount;
	}

	public void setAvgDealAmount(String avgDealAmount) {
		this.avgDealAmount = avgDealAmount;
	}

	@Override
    public String toString() {
        return "HouseRecentDealDto{" +
               "aptSeq='" + aptSeq + '\'' +
               ", aptNm='" + aptNm + '\'' +
               ", latitude='" + latitude + '\'' +
               ", longitude='" + longitude + '\'' +
               ", dealType='" + dealType + '\'' +
               ", avgDealAmount='" + avgDealAmount + '\'' +
               '}';
    }

}

