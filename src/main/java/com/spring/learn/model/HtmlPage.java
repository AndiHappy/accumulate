package com.spring.learn.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class HtmlPage {

    private String link;

    private String content;

    private HtmlPage next;

    private String pageTitle;

    public HtmlPage(String pagelink) {
        this.link=pagelink;
    }
}
