package com.spring.learn;

import com.spring.learn.model.HostConfig;
import com.spring.learn.model.PageLink;
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

        String link = "https://www.bqg5.cc/61_61314/";

        List<PageLink> result = service.findChapterLink(link,null);

        File storeFile = new File("shenhua.txt");
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

    @Test
    public void order() throws IOException {
        String begine = "https://www.bqg5.cc/61_61314/143742083.html";
        File storeFile = new File("guatou.txt");
        if (!storeFile.exists()) {
            storeFile.createNewFile();
        }else {
            storeFile.delete();
            storeFile.createNewFile();
        }
        service.orderStore(begine,null,storeFile);
    }
}
