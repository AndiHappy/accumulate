package com.spring.learn.service;

import com.spring.learn.model.HostConfig;
import com.spring.learn.model.HtmlPage;
import com.spring.learn.util.ConfigUtil;
import com.spring.learn.util.CustomerFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
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

    @Resource
    private FileCacheService fileCacheService;

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

    public HtmlPage orderStore(String begin, HostConfig config, File storeFile) throws IOException {
        HtmlPage cur = constructHtmlPage(begin,config,storeFile);
        HtmlPage head = cur;
        while(cur.getNext() != null){
            cur.setNext(constructHtmlPage(cur.getNext().getLink(),config,storeFile));
            CustomerFileUtils.write(storeFile,"\n",true);
            CustomerFileUtils.write(storeFile,cur.getPageTitle(),true);
            CustomerFileUtils.write(storeFile,"\n",true);
            CustomerFileUtils.write(storeFile,cur.getContent(),true);
            cur = cur.getNext();
        }
        return head;

    }

    /**
     * 构建当前值
     * */
    public HtmlPage constructHtmlPage(HtmlPage cur, HostConfig config, File storeFile) {
        if(StringUtils.isBlank(cur.getContent())) {
            cur = constrctContent(cur,config,storeFile);
        }
        if(StringUtils.isBlank(cur.getPageTitle())) {
            cur = constrctTitle(cur,config,storeFile);
        }
        if(cur.getNext() == null) {
            cur = constrctNext(cur,config,storeFile);
        }
        return cur;
    }


    public HtmlPage constructHtmlPage(String pageLink, HostConfig config, File storeFile) {
        if (config == null) config = configCustomService.findHostConfig(pageLink);
        driver.get(pageLink);
        log.debug("load:{}", pageLink);
        HtmlPage htmlPage = new HtmlPage(pageLink);
        htmlPage = constrctTitle(htmlPage,config,storeFile);
        if(!ConfigUtil.chapterLinkPattern.matcher(htmlPage.getPageTitle()).find()){
            log.error("discard:{} !",htmlPage.getPageTitle(),htmlPage.getLink());
        }else{
            htmlPage = constrctContent(htmlPage,config,storeFile);
        }

        htmlPage = constrctNext(htmlPage,config,storeFile);
        return htmlPage;
    }

    public HtmlPage constrctNext(HtmlPage htmlPage, HostConfig config, File storeFile){
        WebElement next = null;
        //根据config，找到包含目录的html的部分
        if (StringUtils.isNotBlank(config.getNextSelector())) {
            next = driver.findElementByCssSelector(config.getNextSelector());
        }
        if (next != null && next.isEnabled()) {
            String href = next.getAttribute("href");
            HtmlPage nextPage = new HtmlPage(href);
            htmlPage.setNext(nextPage);
        } else {
            throw new IllegalArgumentException("not find next chapter HTML ELEMENT ");
        }
        return htmlPage;
    }

    public HtmlPage constrctTitle(HtmlPage htmlPage, HostConfig config, File storeFile){
        // chapter的名称
        Assert.notNull(config.getPageTitleSelector(), "HostConfig pageTitle selector is null");
        WebElement pageTitle = driver.findElementByCssSelector(config.getPageTitleSelector());
        Assert.notNull(pageTitle, "cssSelector pageTitle  is null: "+ htmlPage.getLink());
        String title = pageTitle.getText();
        Assert.notNull(title, "Title is null: "+ htmlPage.getLink());
        htmlPage.setPageTitle(title);
        log.info("load: "+ title);
        return htmlPage;
    }

    public HtmlPage constrctContent(HtmlPage htmlPage, HostConfig config, File storeFile){
        // chapter的内容
        Assert.notNull(config.getContainSelector(), "HostConfig pageContent selector is null: "+htmlPage.getLink());

        WebElement pageContent = driver.findElementByCssSelector(config.getContainSelector());
        Assert.notNull(pageContent, "cssSelector pageContent is null: "+ htmlPage.getLink());


        if (pageContent != null && pageContent.isEnabled()) {
            log.debug("find pageContent:{}", pageContent);
            String content = pageContent.getText();
            if (ConfigUtil.isPageContent(content)) {
                // 进行过滤
                if (config != null && config.getRemoveArray() != null && config.getRemoveArray().length > 0) {
                    String[] remove = config.getRemoveArray();
                    for (String removeKeys : remove) {
                        content = StringUtils.remove(content, removeKeys);
                    }
                }
                htmlPage.setContent(content);
            } else {
                log.error("pageContent:{} is ERROR.", pageContent);
            }
        } else {
            throw new IllegalArgumentException("not find pageContent chapter HTML ELEMENT ");
        }
        return htmlPage;
    }


    public TreeMap<String, String> findChapterLink(String allChaptersLinks, HostConfig config) {
        if (config == null) config = configCustomService.findHostConfig(allChaptersLinks);

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

        return null;

    }

    public List<String> pageContent(TreeMap<String, String> pages, HostConfig config) {
        return pageContent(pages, null, config);
    }

    public String pageContent(String pageLink, File storeFile, HostConfig config) {
        String content = null;
        try {
            if (config == null) config = configCustomService.findHostConfig(pageLink);
            driver.get(pageLink);
            //TODO  检查缓存
            log.debug("load:{}", pageLink);
            WebElement pageContent = null;

            //根据config，找到包含文章的html的部分
            if (StringUtils.isNotBlank(config.getPageContentSelector())) {
                pageContent = driver.findElementByCssSelector(config.getPageContentSelector());
            }

            if (pageContent != null && pageContent.isEnabled()) {
                log.debug("find pageContent:{}", pageContent);
                content = pageContent.getText();
                if (ConfigUtil.isPageContent(content)) {
                    // 进行过滤
                    if (config != null && config.getRemoveArray() != null && config.getRemoveArray().length > 0) {
                        String[] remove = config.getRemoveArray();
                        for (String removeKeys : remove) {
                            content = StringUtils.remove(content, removeKeys);
                        }
                    }
                } else {
                    log.error("pageContent:{} is ERROR.", pageContent);
                }
            } else {
                throw new IllegalArgumentException("not find pageContent chapter HTML ELEMENT ");
            }

            if (StringUtils.isNotBlank(content)) {
                CustomerFileUtils.writeStringToFile(storeFile, content, true);
                return content;
            }


        } catch (IOException e) {
            throw new IllegalArgumentException("save page content " + pageLink + " is ERROR: " + e);
        }
        return null;
    }

    // 根据pageLink，获取内容
    public String pageContent(String pageLink, HostConfig config) {
        return pageContent(pageLink, null, config);
    }

    public List<String> pageContent(TreeMap<String, String> pages, File storeFile, HostConfig config) {
        if (pages != null && !pages.isEmpty()) {
            List<String> pageContents = new ArrayList<String>();
            for (String pageLink : pages.keySet()) {
                String name = pages.get(pageLink);
                String content = pageContent(pageLink, storeFile, config);
                log.info("find page: {} content.", name);

                if (ConfigUtil.isPageContent(content)) {
                    //cache
                    fileCacheService.save2SignalFile(storeFile.getName(), name, content);
                }

                pageContents.add(content);
            }
            return pageContents;
        }
        return null;
    }


}
