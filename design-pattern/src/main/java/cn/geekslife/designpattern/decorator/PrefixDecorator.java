package cn.geekslife.designpattern.decorator;

/**
 * 前缀装饰器 - 具体装饰器
 */
public class PrefixDecorator extends TextProcessorDecorator {
    private String prefix;
    
    public PrefixDecorator(TextProcessor textProcessor, String prefix) {
        super(textProcessor);
        this.prefix = prefix;
    }
    
    @Override
    public String process(String text) {
        String processedText = super.process(text);
        return prefix + processedText;
    }
    
    // getter和setter
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
}