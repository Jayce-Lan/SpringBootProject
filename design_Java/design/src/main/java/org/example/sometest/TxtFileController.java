package org.example.sometest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TxtFileController {
    private final Logger log = LogManager.getLogger(this.getClass().getName());
    private static final String FILE_PATH = "/Users/lanjiesi/Documents/test/ltxz-txt-read.txt";

    public static void main(String[] args) {
        TxtFileController txtFileController = new TxtFileController();
//        txtFileController.readWholeTxtAndReplacement(FILE_PATH,
//                "<br />", "\n");
        txtFileController.readTxtFileForRow(FILE_PATH);
    }

    /**
     * 读取txt文件并按行log到控制台
     * @param filePath 文件所在地址
     */
    private void readTxtFileForRow(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 读取整个txt文件的文字
     * @param filePath 文件地址
     */
    private void readWholeTxt(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            // 将所有行合并为一个单独的字符串，如果需要的话
            StringBuilder contentBuilder = new StringBuilder();
            for (String line : lines) {
                contentBuilder.append(line).append("\n"); // 这里添加换行符以保持原文本的格式
            }
            String wholeContent = contentBuilder.toString();
            System.out.println(wholeContent);

            // 或者，如果你确定文件不大，直接使用String.join()方法简化代码
//            String wholeContentSimple = String.join("\n", lines);
//            System.out.println(wholeContentSimple);

        } catch (IOException e) {
            System.err.println(e.getMessage());;
        }
    }

    /**
     * 读取整个文件，并替换其指定字符串
     * @param filePath 文件路径
     * @param target 需要替换的字符串（或者正则表达式）
     * @param replace 替换后的字段
     */
    private void readWholeTxtAndReplacement(String filePath, String target, String replace) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            // 将所有行合并为一个单独的字符串，如果需要的话
            StringBuilder contentBuilder = new StringBuilder();
            for (String line : lines) {
                contentBuilder.append(line).append("\n"); // 这里添加换行符以保持原文本的格式
            }
            String wholeContent = contentBuilder.toString();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(wholeContent.replaceAll(target, replace));
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.err.println(e.getMessage());;
        }
    }

    /**
     * 写入字段到txt文件当中
     * @param filePath
     */
    private void writeTxtFile(String filePath) {
        String content = "This is the content to write into the file."; // 要写入的内容

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
