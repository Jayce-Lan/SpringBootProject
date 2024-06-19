package org.example.chapter07.builder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuilderMain {
    private final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        BuilderMain builderMain = new BuilderMain();
        builderMain.testTextBuilder();
        builderMain.testHTMLBuilder();
    }

    private void testTextBuilder() {
        TextBuilder textBuilder = new TextBuilder();
        Director director = new Director(textBuilder);
        director.construct();
        log.info("result >>>>> {}", textBuilder.getResult());
    }

    private void testHTMLBuilder() {
        HTMLBuilder htmlBuilder = new HTMLBuilder();
        Director director = new Director(htmlBuilder);
        director.construct();
        log.info("result >>>>> {}", htmlBuilder.getResult());
    }
}
