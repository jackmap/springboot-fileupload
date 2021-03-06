package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
  *@deprecated:
  *@author作者：mp
  *2019年5月31日
*/
@Controller
@RequestMapping("/uploads")
public class UploadController {
	
	private static final Log log = LogFactory.getLog(UploadController.class);

    @GetMapping
    public String index() {
        return "index";
    }


    @PostMapping("/upload1")
    @ResponseBody
    public Map<String, String> upload1(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("[文件类型] - [{}]"+file.getContentType());
        log.info("[文件名称] - [{}]"+ file.getOriginalFilename());
        log.info("[文件大小] - [{}]"+file.getSize());
        String filepath="E:\\app\\chapter16\\";
        File files=new File(filepath);
        if(!files.exists()) {
        	files.mkdirs();
        }
        // TODO 将文件写入到指定目录（具体开发中有可能是将文件写入到云存储/或者指定目录通过 Nginx 进行 gzip 压缩和反向代理，此处只是为了演示故将地址写成本地电脑指定目录）
        file.transferTo(new File(filepath + file.getOriginalFilename()));
        Map<String, String> result = new HashMap<>(16);
        result.put("contentType", file.getContentType());
        result.put("fileName", file.getOriginalFilename());
        result.put("fileSize", file.getSize() + "");
        return result;
    }

    @PostMapping("/upload2")
    @ResponseBody
    public List<Map<String, String>> upload2(@RequestParam("file") MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            return null;
        }
        List<Map<String, String>> results = new ArrayList<>();
        String filepath="E:\\app\\chapter16\\";
        File _file=new File(filepath);
        if(!_file.exists()) {
        	_file.mkdirs();
        }
        for (MultipartFile file : files) {
        	Map<String, String> map = new HashMap<>(16);
        	if(file.isEmpty()) {
        		results.add(map);
        	}else {
	            file.transferTo(new File(filepath + file.getOriginalFilename()));
	            map.put("contentType", file.getContentType());
	            map.put("fileName", file.getOriginalFilename());
	            map.put("fileSize", file.getSize() + "");
	            results.add(map);
        	}
        }
        return results;
    }

    @PostMapping("/upload3")
    @ResponseBody
    public void upload2(String base64) throws IOException {
    	String filepath="E:\\app\\chapter16\\";
        File file=new File(filepath);
        if(!file.exists()) {
        	file.mkdirs();
        }
        // TODO BASE64 方式的 格式和名字需要自己控制（如 png 图片编码后前缀就会是 data:image/png;base64,）
        final File tempFile = new File(filepath+"test.jpg");
        // TODO 防止有的传了 data:image/png;base64, 有的没传的情况
        String[] d = base64.split("base64,");
        final byte[] bytes = Base64Utils.decodeFromString(d.length > 1 ? d[1] : d[0]);
        FileCopyUtils.copy(bytes, tempFile);
    }
}
