package com.ucc.helloapp.service;
/**
 * @author Meiqi Huang
 * @date 2022-11-06
 *
 * Calculate the average volume and Assess the Noise Level in a recording file by calling heard() method.
 * After calculation, it would compare the average volume with each Loundness Threshold, and assess the Noise RATE of recording file.
 * It would update AudioInfo instance NoiseRate and CurrentVolume properties values inside heard method.
 *
 */
public class NoiseDetector {

    public static final int DEFAULT_LOUDNESS_THRESHOLD1 = 100;
    public static final int DEFAULT_LOUDNESS_THRESHOLD2 = 200;
    public static final int DEFAULT_LOUDNESS_THRESHOLD3 = 300;
    public static final int DEFAULT_LOUDNESS_THRESHOLD4 = 400;
    public static final int DEFAULT_LOUDNESS_THRESHOLD5 = 500;

    public NoiseDetector() {
    }

    public boolean heard(short[] data, int sampleRate) {

        boolean heard = false;
        // use rms to take the entire audio signal into account
        // and discount any one single high amplitude
        double currentVolume = rootMeanSquared(data);
        AudioInfo.getInstance().setCurrentVolume(currentVolume);

        if (currentVolume > DEFAULT_LOUDNESS_THRESHOLD5) {
            heard = true;
            AudioInfo.getInstance().setNoiseRate(5);
        } else if (currentVolume > DEFAULT_LOUDNESS_THRESHOLD4) {
            heard = true;
            AudioInfo.getInstance().setNoiseRate(4);
        } else if (currentVolume > DEFAULT_LOUDNESS_THRESHOLD3) {
            heard = true;
            AudioInfo.getInstance().setNoiseRate(3);
        } else if (currentVolume > DEFAULT_LOUDNESS_THRESHOLD2) {
            heard = true;
            AudioInfo.getInstance().setNoiseRate(2);
        } else if (currentVolume > DEFAULT_LOUDNESS_THRESHOLD1) {
            heard = true;
            AudioInfo.getInstance().setNoiseRate(1);
        } else {
            AudioInfo.getInstance().setNoiseRate(0);
        }
        return heard;

    }

    private double rootMeanSquared(short[] nums) {

        double ms = 0;
        for (int i = 0; i < nums.length; i++) {
            ms += nums[i] * nums[i];
        }
        ms /= nums.length;

        return Math.sqrt(ms);
    }
}




