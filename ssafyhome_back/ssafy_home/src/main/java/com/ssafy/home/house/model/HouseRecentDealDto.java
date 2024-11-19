package com.ssafy.home.house.model;

public class HouseRecentDealDto {
    private String aptSeq;
    private String aptNm;
    private String latitude; // 위도 추가
    private String longitude; // 경도 추가
    private int dealType; // int로 변경
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

    public int getDealType() { // int로 수정
        return dealType;
    }

    public void setDealType(int dealType) { // int로 수정
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
               ", dealType=" + dealType + // 수정된 부분
               ", avgDealAmount='" + avgDealAmount + '\'' +
               '}';
    }
}
