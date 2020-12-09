package com.spring.learn;

import com.spring.learn.service.ConfigCustomService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ConfigCustomServiceTest {

    @Resource
    private ConfigCustomService configCustomService;

    @Test
    public void testConfig(){
        configCustomService.getHostConfig("www.biduo.cc");
    }

}
