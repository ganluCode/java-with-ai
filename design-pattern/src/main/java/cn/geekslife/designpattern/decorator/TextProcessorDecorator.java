package cn.geekslife.designpattern.decorator;

/**
 * 文本处理器装饰器抽象类 - 装饰器
 */
public abstract class TextProcessorDecorator implements TextProcessor {
    protected TextProcessor textProcessor;
    
    public TextProcessorDecorator(TextProcessor textProcessor) {
        this.textProcessor = textProcessor;
    }
    
    @Override
    public String process(String text) {
        return textProcessor.process(text);
    }
}