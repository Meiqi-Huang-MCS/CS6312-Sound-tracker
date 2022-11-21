package com.ucc.soundtrack.mapper;

import com.ucc.soundtrack.pojo.AudioInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AudioInfoMapper {

    int deleteByPrimaryKey(Integer count);

    int insert(AudioInfo record);

    AudioInfo selectByPrimaryKey(Integer count);

    List<AudioInfo> selectAll();

    int updateByPrimaryKey(AudioInfo record);

}