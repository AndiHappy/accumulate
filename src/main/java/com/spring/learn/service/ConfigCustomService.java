package com.spring.learn.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.learn.model.HostConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

@Slf4j
@Service("configCustomService")
public class ConfigCustomService implements InitializingBean {

    private Properties confg;

    @Override
    public void afterPropertiesSet() throws Exception {
        confg = new Properties();
        File configs = new File("src/main/resources/config.properties");
        System.out.println(configs.getAbsolutePath());
        FileReader reader = new FileReader(configs);
        confg.load(reader);
        System.out.println(confg.stringPropertyNames());
    }

    public HostConfig getHostConfig(String host) {
       String jsonString =  this.confg.getProperty(host);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HostConfig config = objectMapper.readValue(jsonString, HostConfig.class);
            return config;
        } catch (JsonProcessingException e) {
           log.info("parse error:{}",e);
        }
        return null;
    }
}
