package com.spring.learn.special;

import ch.qos.logback.core.util.FileUtil;
import org.junit.platform.commons.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.Arrays;

/**
 * 另外的思路
 * */
public class Chrome {
	
	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver", "accumulate/src/main/resources/chromedriver");
		Chrome rome = new Chrome();
		rome.testGoogleSearch();
	}
	
	public void testGoogleSearch() throws Exception {
		String fileName = "2.txt";
		File file = new File(fileName);
		if(file.exists()) {
			file.delete();
		}

		file.createNewFile();


		// Optional, if not specified, WebDriver will search your path for chromedriver.
		ChromeOptions options = new ChromeOptions();
//		options.setExperimentalOption("excludeSwitches", Arrays.asList("disable-popup-blocking"));
//		options.addArguments("--headless");
//		options.addArguments("--no-sandbox");//  # 取消沙盒模式
		options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		options.addArguments("disable-infobars");//关闭使用ChromeDriver打开浏览器时上部提示语"Chrome正在受到自动软件的控制"
		options.addArguments("headless");//加载浏览器的静默模式，使浏览器在后台运行
		ChromeDriver driver = new ChromeDriver(options);

		driver.get("https://www.dingdiann.com/ddk14238/");

		// 首先是拿到content
		WebElement elenet = driver.findElementByClassName("box_con");


		System.out.println(elenet.toString());
		System.out.println(elenet.getText());
		String links = driver.findElementByLinkText("下一页").getAttribute("href");
		if(StringUtils.isNotBlank(links)){
			while(true){
				boolean contine = false;
				try{
					WebElement value = driver.findElementByLinkText("下一页");
					System.out.println(value.isEnabled());
					driver.findElementByLinkText("下一页").click();
					boolean flag = true;
					while(flag){
						elenet = driver.findElementById("_17mb_content");
						String text = elenet.getText();
						if(text.startsWith("正在加载")){
							Thread.sleep(50);
						}else {
							flag=false;
						}
					}

					System.out.println("加载了："+ driver.getTitle());
				}catch (Exception e){
					contine = true;
				}

				if(contine){
					try{
						WebElement value = driver.findElementByLinkText("下一章");
						System.out.println(value.isEnabled());
						driver.findElementByLinkText("下一章").click();
						elenet = driver.findElementById("_17mb_content");
						boolean flag = true;
						while(flag){
							elenet = driver.findElementById("_17mb_content");
							String text = elenet.getText();
							if(text.startsWith("正在加载")){
								Thread.sleep(50);
							}else {
								flag=false;
							}
						}

						System.out.println("加载了："+ driver.getTitle());
					}catch (Exception e){
						//
					}
				}
			}
		}


		driver.quit();
	}

}
