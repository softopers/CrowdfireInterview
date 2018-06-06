package com.interview.crowdfireinterview;

import java.util.Objects;

public class Cloth {

    private Integer id;
    private Integer clothType;
    private String clothImageUrl;
    private String timeStamp;

    public Cloth() {
    }

    public Cloth(Integer id, Integer clothType, String clothImageUrl, String timeStamp) {

        this.id = id;
        this.clothType = clothType;
        this.clothImageUrl = clothImageUrl;
        this.timeStamp = timeStamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClothType() {
        return clothType;
    }

    public void setClothType(Integer clothType) {
        this.clothType = clothType;
    }

    public String getClothImageUrl() {
        return clothImageUrl;
    }

    public void setClothImageUrl(String clothImageUrl) {
        this.clothImageUrl = clothImageUrl;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Cloth{" +
                "id=" + id +
                ", clothType=" + clothType +
                ", clothImageUrl='" + clothImageUrl + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cloth cloth = (Cloth) o;
        return Objects.equals(clothImageUrl, cloth.clothImageUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clothImageUrl);
    }
}