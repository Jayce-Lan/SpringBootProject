package org.example.chapter08.abstractfactory;

import org.example.chapter08.abstractfactory.factory.Factory;
import org.example.chapter08.abstractfactory.factory.Link;
import org.example.chapter08.abstractfactory.factory.Page;
import org.example.chapter08.abstractfactory.factory.Tray;
import org.example.chapter08.abstractfactory.listfactory.ListFactory;

public class AbstractFactoryMain {
    public static void main(String[] args) {
        AbstractFactoryMain main = new AbstractFactoryMain();
        // org.example.chapter08.abstractfactory.listfactory.ListFactory
        // 没跑通。。。
        main.testTableFactory(ListFactory.class.getName());
    }

    private void testTableFactory(String className) {
        Factory factory = Factory.getFactory(className);
        Link google = factory.createLink("google", "https://google.com.hk");
        Link wiki = factory.createLink("wiki", "https://zh.wikioedia.org/zh-gabs");
        Tray traySearch = factory.createTray("搜索引擎");
        traySearch.add(google);
        traySearch.add(wiki);

        Link chatGPT = factory.createLink("chatGPT", "https://chat.openai.com");
        Link github = factory.createLink("github", "https://github.com");
        Tray trayCode = factory.createTray("编程与ai");
        trayCode.add(chatGPT);
        trayCode.add(github);

        Page page = factory.createPage("LinkPage", "Jayce");
        page.add(traySearch);
        page.add(trayCode);
        page.output();
    }
}
