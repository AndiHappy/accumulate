package com.spring.learn;

import com.spring.learn.service.ChromeDriverCustomService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ChromeDriverCustomServiceTest {

    @Resource
    private ChromeDriverCustomService service;

    @Test
    public void findChapterLink() {
        service.findChapterLink("https://www.biduo.cc/biquge/15_15881/");
    }
}
