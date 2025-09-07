package cn.geekslife.designpattern.abstractfactory;

/**
 * 文本框接口
 * 定义文本框的通用行为
 */
public interface TextBox {
    
    /**
     * 渲染文本框
     */
    void render();
    
    /**
     * 输入文本
     * @param text 输入的文本
     */
    void inputText(String text);
    
    /**
     * 获取文本框内容
     * @return 文本框内容
     */
    String getText();
    
    /**
     * 设置文本框提示信息
     * @param placeholder 提示信息
     */
    void setPlaceholder(String placeholder);
}