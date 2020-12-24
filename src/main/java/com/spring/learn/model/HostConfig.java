package com.spring.learn.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class HostConfig implements Serializable {

    private String hostN;

    private String containS;

    private String chapterTagName;

    private String contentS;

    private String[] remove;

    private String nextS;

    private String titleS;

    private String pageTitleS;

}
