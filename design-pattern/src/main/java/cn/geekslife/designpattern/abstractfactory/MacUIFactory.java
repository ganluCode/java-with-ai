package cn.geekslife.designpattern.abstractfactory;

/**
 * Mac UI工厂
 * 实现UI工厂接口，创建Mac风格的UI组件族
 */
public class MacUIFactory implements UIFactory {
    
    @Override
    public Button createButton() {
        return new MacButton();
    }
    
    @Override
    public TextBox createTextBox() {
        return new MacTextBox();
    }
}