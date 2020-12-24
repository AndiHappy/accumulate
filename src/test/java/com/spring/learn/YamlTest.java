package com.spring.learn;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.spring.learn.model.HostConfig;
import org.yaml.snakeyaml.DumperOptions;

import java.io.*;

public class YamlTest {

    private static DumperOptions dumperOptions = new DumperOptions();

    static {
        //设置yaml读取方式为块读取
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        dumperOptions.setPrettyFlow(false);
    }

    public static void main(String[] args) throws Exception {



        File configs = new File("accumulate/src/main/resources/host.yaml");
//        if(configs.exists()){
//            configs.delete();
//            configs.createNewFile();
//            System.out.println(configs.getAbsolutePath());
//        }

        YamlWriter writer = new YamlWriter(new FileWriter(configs));
        for (int i = 0; i < 10 ; i++) {
            HostConfig config = new HostConfig();
            config.setChapterTagName("第"+i+"ge");
            config.setHostN("www..com"+i);
            config.setContainS("link=>ddda:dadad%$ddd>"+i);
            config.setRemove(new String[]{"44343","dsdasa","去除","ii"+i});
            writer.write(config);
        }

        writer.close();

        YamlReader reader = new YamlReader(new FileReader(configs));
        HostConfig object = reader.read(HostConfig.class);
        System.out.println(object);
    }
}
