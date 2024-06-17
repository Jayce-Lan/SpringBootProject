package org.example.chapter05;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.chapter05.exercise.Triple;
import org.example.chapter05.singleton.Singleton;
import org.example.chapter05.exercise.TicketMaker;
import org.example.chapter05.exercise.TicketMakerSingleton;

import java.util.Objects;

public class SingletonMain {
    private static final Logger log = LogManager.getLogger(SingletonMain.class);

    public static void main(String[] args) {
        SingletonMain main = new SingletonMain();
//        main.testSingleton();
//        main.testTicketMaker();
//        main.testTicketMakerSingleton();
        main.testTriple();
    }

    private void testSingleton() {
        Singleton obj1 = Singleton.getInstance();
        Singleton obj2 = Singleton.getInstance();
        if (Objects.equals(obj1, obj2)) {
            log.info("obj1与obj2为同一个对象");
        } else {
            log.info("obj1与obj2不为同一个对象");
        }
    }

    private void testTicketMaker() {
        TicketMaker t1 = new TicketMaker();
        TicketMaker t2 = new TicketMaker();
        log.info("t1 getNextTicketNumber : {}", t1.getNextTicketNumber());
        log.info("t1 getNextTicketNumber : {}", t1.getNextTicketNumber());
        log.info("t2 getNextTicketNumber : {}", t2.getNextTicketNumber());
        log.info("t2 getNextTicketNumber : {}", t2.getNextTicketNumber());
    }

    private void testTicketMakerSingleton() {
        TicketMakerSingleton t1 = TicketMakerSingleton.getInstance();
        TicketMakerSingleton t2 = TicketMakerSingleton.getInstance();
        log.info("t1 getNextTicketNumber : {}", t1.getNextTicketNumber());
        log.info("t1 getNextTicketNumber : {}", t1.getNextTicketNumber());
        log.info("t2 getNextTicketNumber : {}", t2.getNextTicketNumber());
        log.info("t2 getNextTicketNumber : {}", t2.getNextTicketNumber());
    }

    private void testTriple() {
        Triple triple1 = Triple.getInstance(0);
        Triple triple2 = Triple.getInstance(1);
        Triple triple3 = Triple.getInstance(2);
        Triple triple4 = Triple.getInstance(3);
        log.info("triple1 {}", triple1.getNextCount());
        log.info("triple1 {}", triple1.getNextCount());
        log.info("triple2 {}", triple2.getNextCount());
        log.info("triple2 {}", triple2.getNextCount());
        log.info("triple3 {}", triple3.getNextCount());
        log.info("triple3 {}", triple3.getNextCount());
        log.info("triple4 {}", triple4.getNextCount());
        log.info("triple4 {}", triple4.getNextCount());
        log.info("triple1 && triple3 {}", Objects.equals(triple1, triple3));
        log.info("triple3 && triple4 {}", Objects.equals(triple4, triple3));
    }
}
