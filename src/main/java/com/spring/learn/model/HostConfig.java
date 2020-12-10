package com.spring.learn.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HostConfig {
    private String containSelector;
    private String chapterTagName;
    private String pageContentSelector;
    private String[] removeArray;
}
