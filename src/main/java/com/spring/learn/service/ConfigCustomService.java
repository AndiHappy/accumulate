package com.spring.learn.service;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.javafx.jmx.json.JSONWriter;
import com.spring.learn.model.HostConfig;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.writer.JsonReader;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.json.Json;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service("configCustomService")
public class ConfigCustomService implements InitializingBean {

//    private Properties confg;

    private Map<String,HostConfig> hostConfigMap = new HashMap<>();

    private static final String resource = "src/main/resources/host.yaml";

    private static DumperOptions dumperOptions = new DumperOptions();
    static{
        //设置yaml读取方式为块读取
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        dumperOptions.setPrettyFlow(false);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        confg = new Properties();
        File configs = new File(resource);
//        System.out.println(configs.getAbsolutePath());
//        FileReader reader = new FileReader(configs);
//        confg.load(reader);
//        System.out.println(confg.stringPropertyNames());

        YamlReader reader = new YamlReader(new FileReader(configs));
        while (true) {
            HostConfig hostConfig = reader.read(HostConfig.class);
            if (hostConfig == null) break;
            hostConfigMap.put(hostConfig.getHostN(),hostConfig);
        }

    }

    public HostConfig getHostConfig(String host) {
//       String jsonString =  this.confg.getProperty(host);
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            HostConfig config = objectMapper.readValue(jsonString, HostConfig.class);
//            return config;
//        } catch (JsonProcessingException e) {
//           log.info("parse error:{}",e);
//        }
//        return null;
        return this.hostConfigMap.getOrDefault(host,null);
    }

    public HostConfig findHostConfig(String link) {
        if(StringUtils.isNotBlank(link)){
            URL chaptersLinks = null;
            try {
                chaptersLinks = new URL(link);
                String host = chaptersLinks.getHost();
                return getHostConfig(host);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("URL: " + link + " is ERROR: " + e);
            }
        }
        return null;
    }
}
