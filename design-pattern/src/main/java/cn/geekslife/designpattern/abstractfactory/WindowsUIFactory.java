package cn.geekslife.designpattern.abstractfactory;

/**
 * Windows UI工厂
 * 实现UI工厂接口，创建Windows风格的UI组件族
 */
public class WindowsUIFactory implements UIFactory {
    
    @Override
    public Button createButton() {
        return new WindowsButton();
    }
    
    @Override
    public TextBox createTextBox() {
        return new WindowsTextBox();
    }
}