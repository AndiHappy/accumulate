package com.spring.learn.service;

import com.spring.learn.model.HostConfig;
import com.spring.learn.util.ConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

@Slf4j
@Service("chromeDriverCustomService")
public class ChromeDriverCustomService implements InitializingBean {

    private ChromeDriver driver;

    @Resource
    private ConfigCustomService configCustomService;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        // Optional, if not specified, WebDriver will search your path for chromedriver.
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Arrays.asList("disable-popup-blocking"));
//		options.addArguments("--headless");
        options.addArguments("disable-infobars");//关闭使用ChromeDriver打开浏览器时上部提示语"Chrome正在受到自动软件的控制"
        options.addArguments("headless");//加载浏览器的静默模式，使浏览器在后台运行
//		options.addArguments("--no-sandbox");//  # 取消沙盒模式
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        ChromeDriver driver = new ChromeDriver(options);
        setDriver(driver);
    }

    public ChromeDriver getDriver() {
        return driver;
    }

    public void setDriver(ChromeDriver driver) {
        this.driver = driver;
    }


    public TreeMap<String, String> findChapterLink(String allChaptersLinks) {
        try {
            URL chaptersLinks = new URL(allChaptersLinks);
            String host = chaptersLinks.getHost();
            HostConfig config = configCustomService.getHostConfig(host);
            driver.get(allChaptersLinks);
            log.debug("load:{}", allChaptersLinks);
            WebElement contain = null;

            //根据config，找到包含目录的html的部分
            if (StringUtils.isNotBlank(config.getContainSelector())) {
                contain = driver.findElementByCssSelector(config.getContainSelector());
            }

            if (contain != null && contain.isEnabled()) {
                log.debug("find contain:{}", contain);

                //章节目录链接的去重，排序

                // link ==> name
                TreeMap<String, String> links2chapterNames = new TreeMap<>();

                // name ==> link
                TreeMap<String, String> chapterNames2links = new TreeMap<>();

                //寻找章节的链接
                if (StringUtils.isNotBlank(config.getChapterTagName())) {
                    List<WebElement> chaptersElements = contain.findElements(By.tagName(config.getChapterTagName()));
                    if (chaptersElements != null && !chaptersElements.isEmpty()) {
                        for (WebElement chapterElement : chaptersElements
                        ) {
                            WebElement a = chapterElement.findElement(By.tagName("a"));
                            if (a != null) {
                                String href = a.getAttribute("href");
                                String name = a.getText();
                                //如果匹配的上
                                if (ConfigUtil.chapterLinkPattern.matcher(name).find()) {
                                    links2chapterNames.put(href, name);
                                    chapterNames2links.put(name, href);
                                    log.info("name: {} add.", name);
                                } else {
                                    log.info("name: {} discard!!", name);
                                }
                            } else {
                                throw new IllegalArgumentException("chapterElement: " + chapterElement + " contains no a");
                            }
                        }
                    } else {
                        // 没有发现章节
                        throw new IllegalArgumentException("containElement: " + contain + " do not contain chapter element");
                    }
                }

                //是否需要排序
                if (!links2chapterNames.isEmpty() && !chapterNames2links.isEmpty()) {
                    return links2chapterNames;
                }
            } else {
                throw new IllegalArgumentException("not find contain chapter HTML ELEMENT ");
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("URL: " + allChaptersLinks + " is ERROR: " + e);
        }
        return null;

    }

    public List<String> pageContent( TreeMap<String,String> pages) {
        if(pages != null && !pages.isEmpty()){
            List<String> pageContents =  new ArrayList<String>();
            for (String pageLink : pages.keySet()) {
                String content = pageContent(pageLink);
                log.info("find page: {} content.",pages.get(pageLink));
                pageContents.add(content);
            }
            return pageContents;
        }
       return null;
    }

    // 根据pageLink，获取内容
    public String pageContent(String pageLink) {
        String content = null;
        try {
            URL chaptersLinks = new URL(pageLink);
            String host = chaptersLinks.getHost();
            HostConfig config = configCustomService.getHostConfig(host);
            driver.get(pageLink);
            log.debug("load:{}", pageLink);
            WebElement pageContent = null;

            //根据config，找到包含文章的html的部分
            if (StringUtils.isNotBlank(config.getPageContentSelector())) {
                pageContent = driver.findElementByCssSelector(config.getPageContentSelector());
            }

            if (pageContent != null && pageContent.isEnabled()) {
                log.debug("find pageContent:{}", pageContent);
                content = pageContent.getText();
                if(ConfigUtil.isPageContent(content)){
                    return content;
                }
            } else {
                throw new IllegalArgumentException("not find pageContent chapter HTML ELEMENT ");
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("URL: " + pageLink + " is ERROR: " + e);
        }
        return null;

    }
}
