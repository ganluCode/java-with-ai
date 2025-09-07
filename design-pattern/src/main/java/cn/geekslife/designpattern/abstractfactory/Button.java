package cn.geekslife.designpattern.abstractfactory;

/**
 * 按钮接口
 * 定义按钮的通用行为
 */
public interface Button {
    
    /**
     * 渲染按钮
     */
    void render();
    
    /**
     * 按钮点击事件
     */
    void onClick();
    
    /**
     * 获取按钮文本
     * @return 按钮文本
     */
    String getText();
    
    /**
     * 设置按钮文本
     * @param text 按钮文本
     */
    void setText(String text);
}