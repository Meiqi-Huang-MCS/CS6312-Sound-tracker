package com.ucc.soundtrack.controller;

import com.ucc.soundtrack.pojo.AudioInfo;
import com.ucc.soundtrack.service.AudioInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PresentController {
    @Autowired
    AudioInfoService audioInfoService;

    @RequestMapping("/index")
    public ModelAndView presentMap2() {

        List<AudioInfo> audioInfos = audioInfoService.selectAll();

        System.out.println("audioInfos: " + audioInfos.toString());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("audioInfos", audioInfos);

        modelAndView.setViewName("index");

        return modelAndView;

    }
}
