package org.example.chapter05.exercise;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * 最多只能生成3个实例，实例编号分别为0，1，2，通过getInstance(int id)获取
 */
public class Triple {
    private static final Logger log = LogManager.getLogger(Triple.class);
    private int count = 0;
    private static Triple triple1 = new Triple();
    private static Triple triple2 = new Triple();
    private static Triple triple3 = new Triple();

    private Triple() {

    }

    public int getNextCount() {
        return count++;
    };

    public static Triple getInstance(int id) {
        if (Objects.equals(0, id)) {
            log.info("id为 {} 生成了triple1", id);
            return triple1;
        }

        if (Objects.equals(1, id)) {
            log.info("id为 {} 生成了triple2", id);
            return triple2;
        }
        log.info("id为 {} 生成了triple3", id);
        return triple3;
    }
}
