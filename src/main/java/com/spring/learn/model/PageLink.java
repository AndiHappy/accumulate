package com.spring.learn.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageLink {
    private String link;
    private String name;

    public PageLink(String href, String name) {
        setLink(href);
        setName(name);
    }
}
