package cn.geekslife.designpattern.abstractfactory;

/**
 * UI组件族包装类
 * 封装一组相关的UI组件
 */
public class UIComponentFamily {
    
    private final Button button;
    private final TextBox textBox;
    
    public UIComponentFamily(Button button, TextBox textBox) {
        this.button = button;
        this.textBox = textBox;
    }
    
    /**
     * 渲染UI组件族
     */
    public void renderFamily() {
        System.out.println("=== 渲染UI组件族 ===");
        button.render();
        textBox.render();
        System.out.println("=== UI组件族渲染完毕 ===");
    }
    
    /**
     * 模拟用户交互
     */
    public void simulateUserInteraction() {
        System.out.println("=== 模拟用户交互 ===");
        button.onClick();
        textBox.inputText("Hello, Abstract Factory!");
        System.out.println("文本框内容: " + textBox.getText());
        System.out.println("=== 用户交互结束 ===");
    }
    
    /**
     * 获取按钮
     * @return 按钮
     */
    public Button getButton() {
        return button;
    }
    
    /**
     * 获取文本框
     * @return 文本框
     */
    public TextBox getTextBox() {
        return textBox;
    }
    
    /**
     * 获取UI组件族信息
     * @return UI组件族信息字符串
     */
    public String getFamilyInfo() {
        return "UI组件族: " + button.getClass().getSimpleName() + " + " + textBox.getClass().getSimpleName();
    }
}