package com.ucc.helloapp.service;

import java.util.ArrayList;

/**
 * @author Meiqi Huang
 * @date 2022-11-06
 * Save the AudioInfo List during App runs. Singleton mode. Maintain an ArrayList inside.
 * When calling its add(AudioInfo), it would make a copy of AudioInfo and add the copy to the arraylist.
 * Everytime after added the copy, it would call AudioInfo.init() to refresh the AudioInfo value.
 * Everytime SendThread starts to send data, it would fetch the List of AudioInfo value in AudioInfoPool.
 * If SendThread sends to server successfully, it would refresh the AudioInfoPool. The AudioInfoPool would maintain a new list of AudioInfo.
 * If SendThread sends to server failed, the AudioInfoPool won't be refreshed and still stored the original list of AudioInfo.
 */
public class AudioInfoPool {

    public void addAudioInfo(){

        AudioInfoDetail audioInfoDetail = new AudioInfoDetail();
        audioInfoDetail.setCount(AudioInfo.getInstance().getCount());
        audioInfoDetail.setCurrentVolume(AudioInfo.getInstance().getCurrentVolume());
        audioInfoDetail.setLatitude(AudioInfo.getInstance().getLatitude());
        audioInfoDetail.setLongitude(AudioInfo.getInstance().getLongitude());
        audioInfoDetail.setNoiseRate(AudioInfo.getInstance().getNoiseRate());
        audioInfoArrayList.add(audioInfoDetail);
        AudioInfo.getInstance().init();

    }

    public synchronized void init(){
        audioInfoArrayList = new ArrayList<>();
    }

    private ArrayList<AudioInfoDetail> audioInfoArrayList = new ArrayList<>();

    private static AudioInfoPool audioInfoPool = new AudioInfoPool();

    private AudioInfoPool(){}

    public static AudioInfoPool getInstance(){
        return audioInfoPool;
    }

    public ArrayList<AudioInfoDetail> getAudioInfoArrayList() {
        return audioInfoArrayList;
    }

    public void setAudioInfoArrayList(ArrayList<AudioInfoDetail> audioInfoArrayList) {
        this.audioInfoArrayList = audioInfoArrayList;
    }

    private class AudioInfoDetail{
        double latitude;
        double longitude;
        int noiseRate;
        double currentVolume;
        int count=0;


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
            return "AudioInfoDetail{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", noiseRate=" + noiseRate +
                    ", currentVolume=" + currentVolume +
                    ", count=" + count +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AudioInfoPool{" +
                "audioInfoArrayList=" + audioInfoArrayList +
                '}';
    }
}
