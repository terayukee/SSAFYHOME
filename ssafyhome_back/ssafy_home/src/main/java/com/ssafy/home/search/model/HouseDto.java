package com.ssafy.home.search.model;

public class HouseDto {
    private String aptSeq;
    private String sggCd;
    private String umdCd;
    private String umdNm;
    private String jibun;
    private String roadNmSggCd;
    private String roadNm;
    private String roadNmBonbun;
    private String roadNmBubun;
    private String aptNm;
    private Integer buildYear;
    private String latitude;
    private String longitude;
    private String houseType;
    private String sidoName;
    private String gugunName;
    private String dongName;
    

    // Getters and Setters
    public String getAptSeq() {
        return aptSeq;
    }

    public void setAptSeq(String aptSeq) {
        this.aptSeq = aptSeq;
    }

    public String getSggCd() {
        return sggCd;
    }

    public void setSggCd(String sggCd) {
        this.sggCd = sggCd;
    }

    public String getUmdCd() {
        return umdCd;
    }

    public void setUmdCd(String umdCd) {
        this.umdCd = umdCd;
    }

    public String getUmdNm() {
        return umdNm;
    }

    public void setUmdNm(String umdNm) {
        this.umdNm = umdNm;
    }

    public String getJibun() {
        return jibun;
    }

    public void setJibun(String jibun) {
        this.jibun = jibun;
    }

    public String getRoadNmSggCd() {
        return roadNmSggCd;
    }

    public void setRoadNmSggCd(String roadNmSggCd) {
        this.roadNmSggCd = roadNmSggCd;
    }

    public String getRoadNm() {
        return roadNm;
    }

    public void setRoadNm(String roadNm) {
        this.roadNm = roadNm;
    }

    public String getRoadNmBonbun() {
        return roadNmBonbun;
    }

    public void setRoadNmBonbun(String roadNmBonbun) {
        this.roadNmBonbun = roadNmBonbun;
    }

    public String getRoadNmBubun() {
        return roadNmBubun;
    }

    public void setRoadNmBubun(String roadNmBubun) {
        this.roadNmBubun = roadNmBubun;
    }

    public String getAptNm() {
        return aptNm;
    }

    public void setAptNm(String aptNm) {
        this.aptNm = aptNm;
    }

    public Integer getBuildYear() {
        return buildYear;
    }

    public void setBuildYear(Integer buildYear) {
        this.buildYear = buildYear;
    }

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

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }
    
    public String getSidoName() {
        return sidoName;
    }

    public void setSidoName(String sidoName) {
        this.sidoName = sidoName;
    }

    public String getGugunName() {
        return gugunName;
    }

    public void setGugunName(String gugunName) {
        this.gugunName = gugunName;
    }

    public String getDongName() {
        return dongName;
    }

    public void setDongName(String dongName) {
        this.dongName = dongName;
    }

	@Override
	public String toString() {
		return "HouseDto [aptSeq=" + aptSeq + ", sggCd=" + sggCd + ", umdCd=" + umdCd + ", umdNm=" + umdNm + ", jibun="
				+ jibun + ", roadNmSggCd=" + roadNmSggCd + ", roadNm=" + roadNm + ", roadNmBonbun=" + roadNmBonbun
				+ ", roadNmBubun=" + roadNmBubun + ", aptNm=" + aptNm + ", buildYear=" + buildYear + ", latitude="
				+ latitude + ", longitude=" + longitude + ", houseType=" + houseType + ", sidoName=" + sidoName
				+ ", gugunName=" + gugunName + ", dongName=" + dongName + "]";
	}

    
}
