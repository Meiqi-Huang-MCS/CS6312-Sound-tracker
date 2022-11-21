package com.ucc.soundtrack.pojo;

public class AudioInfo {

    private Integer count;

    private Double latitude;

    private Double longtitude;

    private Integer noiserate;

    private Double currentvolume;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Integer getNoiserate() {
        return noiserate;
    }

    public void setNoiserate(Integer noiserate) {
        this.noiserate = noiserate;
    }

    public Double getCurrentvolume() {
        return currentvolume;
    }

    public void setCurrentvolume(Double currentvolume) {
        this.currentvolume = currentvolume;
    }

    @Override
    public String toString() {
        return "AudioInfo{" +
                "count=" + count +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                ", noiserate=" + noiserate +
                ", currentvolume=" + currentvolume +
                '}';
    }
}