package com.spring.learn;

import com.spring.learn.model.HostConfig;
import com.spring.learn.service.ChromeDriverCustomService;
import com.spring.learn.service.ConfigCustomService;
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

    @Resource
    private ConfigCustomService configCustomService;

    @Test
    public void findChapterLink() throws IOException {

        String link = "https://www.biduo.cc/biquge/15_15881/";

        String pageLink = "https://www.biduo.cc/biquge/15_15881/c4644037.html";

        HostConfig hostconfig = configCustomService.findHostConfig(pageLink);

        service.pageContent(pageLink,hostconfig);

        TreeMap<String, String> result = service.findChapterLink(link,null);

        File storeFile = new File("v.txt");
        if (!storeFile.exists()) {
            storeFile.createNewFile();
        }else {
            storeFile.delete();
            storeFile.createNewFile();
        }

        List<String> res = service.pageContent(result,storeFile,null);
        res.clear();
        System.out.println(storeFile.getAbsolutePath());

    }
}
