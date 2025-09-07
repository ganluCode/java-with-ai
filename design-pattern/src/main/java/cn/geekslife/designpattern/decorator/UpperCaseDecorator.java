package cn.geekslife.designpattern.decorator;

/**
 * 大写转换装饰器 - 具体装饰器
 */
public class UpperCaseDecorator extends TextProcessorDecorator {
    public UpperCaseDecorator(TextProcessor textProcessor) {
        super(textProcessor);
    }
    
    @Override
    public String process(String text) {
        String processedText = super.process(text);
        return processedText.toUpperCase();
    }
}