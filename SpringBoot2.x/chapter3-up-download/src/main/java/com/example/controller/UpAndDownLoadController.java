package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@Controller
public class UpAndDownLoadController {
    // 文件上传路径
    private static final String filePath = "/Users/lanjiesi/Documents/test/";

    @GetMapping("/")
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("title", "文件上传下载");
        modelMap.addAttribute("oneFile", "单文件上传");
        modelMap.addAttribute("moreFile", "多文件上传");
        modelMap.addAttribute("downloadFile", "文件下载");
        return "index";
    }

    /**
     * 单文件上传
     * @param file 被上传文件
     * @return 返回结果
     */
    @RequestMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                log.error("文件为空！");
                return "lost";
            }
            // 获取文件名
            String filename = file.getOriginalFilename();
            log.info("文件名为：" + filename);
            String path = filePath + filename;
            File dest = new File(path);
            log.info(path);
            // 判断是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs(); // 新建文件夹
            }
            // 文件写入
            file.transferTo(dest);
            log.info("上传成功！");
            return "end";
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.error("上传失败！");
        return "lost";
    }

    /**
     * 多文件上传
     * @param request
     * @return
     */
    @RequestMapping("/batch")
    public String batch(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); i++) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    // 设置文件路径以及名称
                    stream = new BufferedOutputStream(new FileOutputStream(new File(filePath + file.getOriginalFilename())));
                    stream.write(bytes); // 写入
                    stream.close();
                } catch (IOException e) {
                    stream = null;
                    log.error("第" + i + "个文件上传失败" + e.getMessage());
                    return "lost";
                }
            } else {
                log.error("第" + i + "个文件为空上传失败");
            }
        }
        return "end";
    }

    /**
     * 文件下载
     * @param response
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse response) throws UnsupportedEncodingException {
        String fileName = "testdownload.txt"; // 文件名
        File file = new File(filePath + fileName);
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName,"utf8"));
            byte[] buffer = new byte[1024];
            // 输出流
            OutputStream os = null;
            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
