package org.example.chapter07.builder;

public class Director {
    private Builder builder;

    public Director(Builder builder) {
        this.builder = builder;
    }

    /**
     * 编写文档的实现方法
     */
    public void construct() {
        builder.makeTitle("Greeting!!");
        // 对于HtmlBuilder而言，不能在close之前二次调用，该处用于检测结果
        builder.makeTitle("Greeting");
        builder.makeString("早上与下午");
        builder.makeItems(new String[] {
                "早上好",
                "下午好"
        });
        builder.makeString("晚上");
        builder.makeItems(new String[]{
                "晚上好",
                "goodnight！",
                "goodbye!"
        });
        builder.close();
    }
}
