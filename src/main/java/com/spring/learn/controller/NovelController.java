package com.spring.learn.controller;

import com.spring.learn.model.HtmlPage;
import com.spring.learn.service.ChromeDriverCustomService;
import com.spring.learn.util.ConfigUtil;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
public class NovelController implements InitializingBean {

    @Resource
    private ChromeDriverCustomService chromeDriverCustomService;

    @RequestMapping(value="/dl", method = {RequestMethod.POST, RequestMethod.GET})
    public JSONObject index(@RequestParam(value = "slink") String slink,
                            @RequestParam(value = "sfile") String sfile
                              ) {
        JSONObject result = new JSONObject();
       try {
           if(!sfile.endsWith(".txt")){
               sfile= sfile+".txt";
           }
           File store = new File(ConfigUtil.fileStorePath+sfile);
           if(store.exists()){
               store.delete();
           }
           store.createNewFile();
           HtmlPage page = chromeDriverCustomService.orderStore(slink,null,store);
           if(page != null){
               result.put("msg","success");
               result.put("file",store.getAbsolutePath());
           }

       }catch (Exception e){
           result.put("Exception",e);
       }
        return result;
    }

    @RequestMapping(value = "/up", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView uploadFileAction(@RequestParam("uploadFile") MultipartFile uploadFile) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("uploadAndDownload");
        InputStream fis = null;
        OutputStream outputStream = null;
        try {
            fis = uploadFile.getInputStream();
            outputStream = new FileOutputStream("/data/logs/data/" + uploadFile.getOriginalFilename());
            IOUtils.copy(fis, outputStream);
            modelAndView.addObject("sucess", "上传成功");
            return modelAndView;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        modelAndView.addObject("sucess", "上传失败!");
        return modelAndView;
    }

    @RequestMapping(value="/down", method = {RequestMethod.POST,RequestMethod.GET})
    public void downloadFileAction(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(name = "fName", required = true) String fName) {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            File file = new File(ConfigUtil.fileStorePath+fName);
            fis = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("webdriver.chrome.driver", "chromedriver");

    }
}
