package com.ucc.soundtrack.service.impl;

import com.ucc.soundtrack.controller.AudioInfoAndroid;
import com.ucc.soundtrack.mapper.AudioInfoMapper;
import com.ucc.soundtrack.pojo.AudioInfo;
import com.ucc.soundtrack.service.AudioInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AudioInfoServiceImpl implements AudioInfoService {

    @Autowired
    AudioInfoMapper audioInfoMapper;

    @Override
    public int deleteByPrimaryKey(Integer count) {
        return 0;
    }

    @Override
    public int insert(AudioInfo record) {
        return audioInfoMapper.insert(record);
    }

    @Override
    public int insert(ArrayList<AudioInfoAndroid> record) {

        for (AudioInfoAndroid audioInfoAndroid : record) {

            AudioInfo audioInfo = new AudioInfo();

            audioInfo.setCount(audioInfoAndroid.getCount());

            audioInfo.setCurrentvolume(audioInfoAndroid.getCurrentVolume());

            audioInfo.setLatitude(audioInfoAndroid.getLatitude());

            audioInfo.setLongtitude(audioInfoAndroid.getLongitude());

            audioInfo.setNoiserate(audioInfoAndroid.getNoiseRate());

            audioInfoMapper.insert(audioInfo);

        }

        return 0;
    }

    @Override
    public AudioInfo selectByPrimaryKey(Integer count) {
        return null;
    }

    @Override
    public List<AudioInfo> selectAll() {

        List<AudioInfo> audioInfos = audioInfoMapper.selectAll();

        return audioInfos;

    }

    @Override
    public int updateByPrimaryKey(AudioInfo record) {
        return 0;
    }

}
