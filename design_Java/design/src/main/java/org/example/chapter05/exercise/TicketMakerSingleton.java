package org.example.chapter05.exercise;

/**
 * 计数器单例模式，每次调用getNextTicketNumber都是同一个对象的ticket被操作
 */
public class TicketMakerSingleton {
    private int ticket = 1000;
    private static TicketMakerSingleton ticketMakerSingleton = new TicketMakerSingleton();

    private TicketMakerSingleton() {

    }

    public static TicketMakerSingleton getInstance() {
        return ticketMakerSingleton;
    }

    public int getNextTicketNumber() {
        return ticket++;
    }
}
