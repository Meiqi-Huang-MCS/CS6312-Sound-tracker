package com.ucc.soundtrack.controller;

/**
 * @author Meiqi Huang
 * @date 2022-11-06
 * Represent the basic information of a recording file.
 * Position: the latitude and longitude.
 * NoiseRate: the noise level of this recording file.
 * CurrentVolume: the average Volume of this recording file.
 * Count: the version of a recording file, like id. Everytime refresh the data of AudioInfor instance, it would auto increase 1.
 * Singleton mode: Becase it uses the singleton mode, there is only one instance of AudioInfo class.
 * Every 20s,the UI thread would copy instance info and add it into AudioInforPool.
 * After that, the AudioInfo instance's properties value would be refreshed and carry new info of next recording file.
 */

public class AudioInfoAndroid {

    double latitude;

    double longitude;

    int noiseRate;

    double currentVolume;

    int count;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getNoiseRate() {
        return noiseRate;
    }

    public void setNoiseRate(int noiseRate) {
        this.noiseRate = noiseRate;
    }

    public double getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(double currentVolume) {
        this.currentVolume = currentVolume;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AudioInfoAndroid{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", noiseRate=" + noiseRate +
                ", currentVolume=" + currentVolume +
                ", count=" + count +
                '}'+"\n";
    }
}
