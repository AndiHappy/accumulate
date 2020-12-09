package com.spring.learn;

import com.spring.learn.service.ChromeDriverCustomService;
import com.spring.learn.util.CustomerFileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

@SpringBootTest
public class ChromeDriverCustomServiceTest {

    @Resource
    private ChromeDriverCustomService service;

    @Test
    public void findChapterLink() throws IOException {

        TreeMap<String, String> result = service.findChapterLink("https://www.biduo.cc/biquge/15_15881/");

        List<String> res = service.pageContent(result);

        File file = new File("v.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        for (String content : res
        ) {
            CustomerFileUtils.writeStringToFile(file, content,true);
        }

    }
}
