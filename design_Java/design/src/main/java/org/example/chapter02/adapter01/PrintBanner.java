package org.example.chapter02.adapter01;

public class PrintBanner extends Banner implements Print {
    public PrintBanner(String printMsg) {
        super(printMsg);
    }

    @Override
    public void printWeak() {
        super.showWithParen();
    }

    @Override
    public void printStrong() {
        super.showWithAster();
    }
}
