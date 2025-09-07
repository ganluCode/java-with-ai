package cn.geekslife.designpattern.abstractfactory;

/**
 * Mac文本框实现
 * 实现文本框接口
 */
public class MacTextBox implements TextBox {
    
    private String text = "";
    private String placeholder = "请输入内容";
    
    @Override
    public void render() {
        System.out.println("渲染Mac风格文本框: " + (text.isEmpty() ? placeholder : text));
    }
    
    @Override
    public void inputText(String text) {
        this.text = text;
        System.out.println("在Mac文本框中输入: " + text);
    }
    
    @Override
    public String getText() {
        return text;
    }
    
    @Override
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}