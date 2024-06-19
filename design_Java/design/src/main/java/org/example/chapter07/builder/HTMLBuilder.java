package org.example.chapter07.builder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HTMLBuilder implements Builder {
    private final Logger log = LogManager.getLogger();
    private static final String FILE_PATH = "/Users/lanjiesi/Documents/test/Greeting.html";
    private BufferedWriter writer;
    /**
     * 确保方法在调用到close之前只被调用一次，否则调用无效
     */
    private static boolean isFirstCall = true;

    @Override
    public void makeTitle(String title) {
        if (isFirstCall) {
            try {
                writer = new BufferedWriter(new FileWriter(FILE_PATH));
                writer.write("<html><head><title>" + title + "</title></head><body>");
                writer.write("<h1>" + title + "</h1>");
            } catch (IOException e) {
                e.printStackTrace();
            }
            isFirstCall = false;
        } else {
            log.error("重复调用失败！无法在close之前再次调用！");
        }
    }

    @Override
    public void makeString(String str) {
        try {
            writer.write("<p>" + str + "</p>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void makeItems(String[] items) {
        try {
            writer.write("<ul>");
            for (String item : items) {
                writer.write("<li>" + item + "</li>");
            }
            writer.write("</ul>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            writer.write("</body></html>");
            writer.close();
            isFirstCall = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResult() {
        return FILE_PATH;
    }
}
