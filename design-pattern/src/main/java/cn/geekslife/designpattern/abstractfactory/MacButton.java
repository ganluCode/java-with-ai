package cn.geekslife.designpattern.abstractfactory;

/**
 * Mac按钮实现
 * 实现按钮接口
 */
public class MacButton implements Button {
    
    private String text = "Mac按钮";
    
    @Override
    public void render() {
        System.out.println("渲染Mac风格按钮: " + text);
    }
    
    @Override
    public void onClick() {
        System.out.println("Mac按钮 '" + text + "' 被点击");
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