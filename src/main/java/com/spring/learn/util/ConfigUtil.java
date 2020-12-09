package com.spring.learn.util;

import java.util.regex.Pattern;

public class ConfigUtil {

    public static final Pattern resourceLinkFilters = Pattern.compile(
            ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
            "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    
    public static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");
    
    public static final Pattern googlePatterns =  Pattern.compile("https://www.google.com/{1}+[^\\s]*");
    
    public static final Pattern chapterLinkPattern = Pattern.compile("第[\\u4e00-\\u9fa50-9〇]+章|第[\\u4e00-\\u9fa50-9〇]+节|[0-9]\\d{0,5}");
    
    public static final String fileStorePath = "/bookstore/";
    
    public static final long fileStoreMaxSizePath = 1024*1024*1024L;

    public static boolean isPageContent(String content) {
        if(content != null && content.length() > 200) return true;
        return false;
    }
}