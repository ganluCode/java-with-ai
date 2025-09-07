package cn.geekslife.designpattern.abstractfactory;

/**
 * Windows文本框实现
 * 实现文本框接口
 */
public class WindowsTextBox implements TextBox {
    
    private String text = "";
    private String placeholder = "请输入内容";
    
    @Override
    public void render() {
        System.out.println("渲染Windows风格文本框: " + (text.isEmpty() ? placeholder : text));
    }
    
    @Override
    public void inputText(String text) {
        this.text = text;
        System.out.println("在Windows文本框中输入: " + text);
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