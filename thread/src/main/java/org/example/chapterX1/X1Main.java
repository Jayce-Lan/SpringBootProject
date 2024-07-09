package org.example.chapterX1;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class X1Main {
    private static final Logger log = LogManager.getLogger();
    public static void main(String[] args) {
        X1Main x1Main = new X1Main();
        x1Main.testHowToStart2();
    }

    private void testSingleThread() {
        for (int i = 0; i < 100; i++) {
            log.info("GOOD {}", i);
        }
    }

    private void testMyThreadX1() {
        MyThreadX1 myThreadX1 = new MyThreadX1();
        // 调用run方法不会启动新的线程
        myThreadX1.start();
//        myThreadX1.run();
        for (int i = 0; i < 100; i++) {
            log.info("GOOD {}", i);
        }
    }

    /**
     * 启动线程方式1-使用Thread的子类，调用其start()方法
     */
    private void testHowToStart1() {
        new PrintThreadX1("Good!").start();
        new PrintThreadX1("Nice!").start();
    }

    /**
     * 启动线程方式2-实现Runnable接口
     */
    private void testHowToStart2() {
        new Thread(new PrintRunnableX1("Good!")).start();
//        new Thread(new PrintRunnableX1("Nice!")).start();
        // 使用不同的方法启动线程
        new PrintThreadX1("Haha~").start();
    }
}
