package org.example.chapter02.adapter02;

import org.example.chapter02.adapter01.Banner;

public class PrintBanner02 extends Print02 {
    private Banner banner;

    public PrintBanner02(String pringMsg) {
        this.banner = new Banner(pringMsg);
    }

    @Override
    public void printWeak() {
        banner.showWithParen();
    }

    @Override
    public void printStrong() {
        banner.showWithAster();
    }
}
