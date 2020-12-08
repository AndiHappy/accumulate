package com.spring.learn.service;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service("chromeDriverCustomService")
public class ChromeDriverCustomService implements InitializingBean {

    private ChromeDriver driver;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("webdriver.chrome.driver", "accumulate/src/main/resources/chromedriver");
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
}
