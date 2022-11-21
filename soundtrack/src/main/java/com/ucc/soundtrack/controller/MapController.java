package com.ucc.soundtrack.controller;

import com.ucc.soundtrack.pojo.AudioInfo;
import com.ucc.soundtrack.service.AudioInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MapController {

    @Autowired
    AudioInfoService audioInfoService;

    @PostMapping("/send")
    public String transmit_info(@RequestBody ArrayList<AudioInfoAndroid> audioInfoAndroids) {

        audioInfoService.insert(audioInfoAndroids);

        System.out.println("audioInfo received" + audioInfoAndroids.toString());

        return "receive";
    }


    @GetMapping("/getlist")
    public List<AudioInfo> transmit_info_list() {

        List<AudioInfo> audioInfos = audioInfoService.selectAll();

        System.out.println("audioInfo list" + audioInfos.toString());

        return audioInfos;
    }


}
