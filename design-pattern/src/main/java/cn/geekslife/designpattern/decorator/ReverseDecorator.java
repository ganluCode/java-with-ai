package cn.geekslife.designpattern.decorator;

/**
 * 反转文本装饰器 - 具体装饰器
 */
public class ReverseDecorator extends TextProcessorDecorator {
    public ReverseDecorator(TextProcessor textProcessor) {
        super(textProcessor);
    }
    
    @Override
    public String process(String text) {
        String processedText = super.process(text);
        return new StringBuilder(processedText).reverse().toString();
    }
}