package com.spring.learn.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * 固定文件夹下面的缓存的数据
 *
 * */
@Slf4j
@Service("fileCacheService")
public class FileCacheService implements InitializingBean {

    // 把内容保存到某一个
    public void save2SignalFile(String dir, String fname, String content) {
        return ;
    }

    // 获取某一个文件的内容
    public String getSignalFileContent(String dir, String fname) {
        return null;
    }

    // 判断是否存在文件
    public boolean isExisSignalFile(String dir, String fname) {
        return false;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        //TODO 遍历固定文件的目录装载

    }
}
