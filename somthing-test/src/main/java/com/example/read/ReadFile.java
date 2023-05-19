package com.example.read;

import com.example.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReadFile {

    /**
     * 读取txt的内容 - V1.0
     * 使用jdk8的nio中的 Files.readAllLines 可以使得检索到txt换行符为数组的区分符
     * @param txtPath 文件路径
     */
    public void readTxtByReadAllLines(String txtPath) throws IOException {
        Path path = Paths.get(txtPath);
        // 会根据换行转为数组
        List<String> list = Files.readAllLines(path);
        log.info("list size: {}", list.size());
        list.forEach(item -> {
            log.info(item);
        });
    }

    /**
     * 读取txt的内，并根据传入值截取元素 - V2.0
     * 使用jdk8的nio中的 Files.readAllLines 可以使得检索到txt换行符为数组的区分符
     * @param txtPath 文件路径
     * @param startIndex 开始页数，0起始
     * @param endIndex 结束页数
     */
    public void readTxtByReadAllLines2(String txtPath, int startIndex, int endIndex) throws IOException {
        Path path = Paths.get(txtPath);
        // 会根据换行转为数组
        List<String> list = Files.readAllLines(path);
        int size = list.size();
        log.info("list size: {}", size);
        // end 为0 只查询页数
        if (endIndex == 0) {
            return;
        }
        // 读取
        if (endIndex > 0 && startIndex < endIndex) {
            List<String> logList = new ArrayList<>();
            while (startIndex <= endIndex && startIndex < size) {
                logList.add(startIndex + " " + list.get(startIndex));
                startIndex++;
            }
            logList.forEach(item -> {
                log.info(item);
            });
        }
    }
}
