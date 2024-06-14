package org.example.chapter03.templatemethod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 模板模式的Main
 */
public class TemplateMethodMain {
    private final Logger log = LogManager.getLogger(TemplateMethodMain.class);

    public static void main(String[] args) {
        TemplateMethodMain template = new TemplateMethodMain();
        template.testTemplateMethod();
    }

    private void testTemplateMethod() {
        AbstractDisplay d1 = new CharDisplay('H');
        AbstractDisplay d2 = new StringDisplay("Hello, World!");
        d1.display();
        d1.open();
        d2.display();
    }
}
