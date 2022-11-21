package com.ucc.soundtrack;

import com.ucc.soundtrack.pojo.AudioInfo;
import com.ucc.soundtrack.service.AudioInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SoundtrackApplicationTests {

	@Autowired
	AudioInfoService audioInfoService;

	void contextLoads() {
	}

    @Test
	void insert(){

		AudioInfo audioInfo = new AudioInfo();
		audioInfo.setCount(1);
		audioInfo.setLatitude( 123.1);
		audioInfo.setLongtitude( 31.12);
		audioInfo.setCurrentvolume(123.0);
		audioInfo.setNoiserate(1);
		audioInfoService.insert(audioInfo);

	}
}
