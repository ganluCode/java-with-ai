package cn.geekslife.designpattern.decorator;

/**
 * 基本文本处理器 - 具体组件
 */
public class BasicTextProcessor implements TextProcessor {
    @Override
    public String process(String text) {
        return text;
    }
}