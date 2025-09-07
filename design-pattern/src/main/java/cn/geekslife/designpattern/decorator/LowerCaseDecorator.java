package cn.geekslife.designpattern.decorator;

/**
 * 小写转换装饰器 - 具体装饰器
 */
public class LowerCaseDecorator extends TextProcessorDecorator {
    public LowerCaseDecorator(TextProcessor textProcessor) {
        super(textProcessor);
    }
    
    @Override
    public String process(String text) {
        String processedText = super.process(text);
        return processedText.toLowerCase();
    }
}