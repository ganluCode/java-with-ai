package cn.geekslife.designpattern.abstractfactory;

/**
 * Windows按钮实现
 * 实现按钮接口
 */
public class WindowsButton implements Button {
    
    private String text = "Windows按钮";
    
    @Override
    public void render() {
        System.out.println("渲染Windows风格按钮: " + text);
    }
    
    @Override
    public void onClick() {
        System.out.println("Windows按钮 '" + text + "' 被点击");
    }
    
    @Override
    public String getText() {
        return text;
    }
    
    @Override
    public void setText(String text) {
        this.text = text;
    }
}