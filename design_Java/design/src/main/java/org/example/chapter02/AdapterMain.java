package org.example.chapter02;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.chapter02.adapter01.Print;
import org.example.chapter02.adapter01.PrintBanner;
import org.example.chapter02.adapter02.Print02;
import org.example.chapter02.adapter02.PrintBanner02;

public class AdapterMain {
    private static final Logger logger = LogManager.getLogger(AdapterMain.class);
    public static void main(String[] args) {
        AdapterMain adapterMain = new AdapterMain();
        adapterMain.testAdapter01();
        logger.info("======================");
        adapterMain.testAdapter02();
    }

    private void testAdapter01() {
        Print print = new PrintBanner("hello");
        print.printWeak();
        print.printStrong();
    }

    private void testAdapter02() {
        Print02 print02 = new PrintBanner02("world");
        print02.printWeak();
        print02.printStrong();
    }
}
