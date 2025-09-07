package cn.geekslife.designpattern.abstractfactory;

/**
 * UI工厂接口
 * 定义创建UI组件族的接口
 */
public interface UIFactory {
    
    /**
     * 创建按钮
     * @return 按钮实例
     */
    Button createButton();
    
    /**
     * 创建文本框
     * @return 文本框实例
     */
    TextBox createTextBox();
    
    /**
     * 创建UI组件族
     * @return UI组件族包装对象
     */
    default UIComponentFamily createUIComponentFamily() {
        return new UIComponentFamily(createButton(), createTextBox());
    }
}