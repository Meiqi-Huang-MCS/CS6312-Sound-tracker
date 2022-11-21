package com.ucc.soundtrack.service;

import com.ucc.soundtrack.controller.AudioInfoAndroid;
import com.ucc.soundtrack.pojo.AudioInfo;

import java.util.ArrayList;
import java.util.List;

public interface AudioInfoService {

    int deleteByPrimaryKey(Integer count);

    int insert(AudioInfo record);

    int insert(ArrayList<AudioInfoAndroid> record);

    AudioInfo selectByPrimaryKey(Integer count);

    List<AudioInfo> selectAll();

    int updateByPrimaryKey(AudioInfo record);

}
