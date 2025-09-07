package cn.geekslife.designpattern.decorator;

/**
 * 后缀装饰器 - 具体装饰器
 */
public class SuffixDecorator extends TextProcessorDecorator {
    private String suffix;
    
    public SuffixDecorator(TextProcessor textProcessor, String suffix) {
        super(textProcessor);
        this.suffix = suffix;
    }
    
    @Override
    public String process(String text) {
        String processedText = super.process(text);
        return processedText + suffix;
    }
    
    // getter和setter
    public String getSuffix() { return suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }
}