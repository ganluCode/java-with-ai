package cn.geekslife.designpattern.abstractfactory;

/**
 * 抽象工厂模式演示类
 * 展示抽象工厂模式的使用方法
 */
public class AbstractFactoryDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 抽象工厂模式演示 ===");
        
        // 1. 基本抽象工厂模式
        System.out.println("\n1. 基本抽象工厂模式:");
        demonstrateBasicAbstractFactory();
        
        // 2. 跨平台UI组件系统
        System.out.println("\n2. 跨平台UI组件系统:");
        demonstrateUIComponentSystem();
        
        // 3. 产品族使用
        System.out.println("\n3. 产品族使用:");
        demonstrateProductFamily();
        
        // 4. UI组件族使用
        System.out.println("\n4. UI组件族使用:");
        demonstrateUIComponentFamily();
    }
    
    /**
     * 演示基本抽象工厂模式
     */
    private static void demonstrateBasicAbstractFactory() {
        // 使用工厂1创建产品族
        AbstractFactory factory1 = new ConcreteFactory1();
        AbstractProductA productA1 = factory1.createProductA();
        AbstractProductB productB1 = factory1.createProductB();
        
        productA1.use();
        productB1.operate();
        
        // 使用工厂2创建产品族
        AbstractFactory factory2 = new ConcreteFactory2();
        AbstractProductA productA2 = factory2.createProductA();
        AbstractProductB productB2 = factory2.createProductB();
        
        productA2.use();
        productB2.operate();
    }
    
    /**
     * 演示跨平台UI组件系统
     */
    private static void demonstrateUIComponentSystem() {
        // Windows风格UI组件族
        UIFactory windowsFactory = new WindowsUIFactory();
        Button windowsButton = windowsFactory.createButton();
        TextBox windowsTextBox = windowsFactory.createTextBox();
        
        windowsButton.render();
        windowsTextBox.render();
        windowsButton.onClick();
        windowsTextBox.inputText("Windows文本");
        
        System.out.println();
        
        // Mac风格UI组件族
        UIFactory macFactory = new MacUIFactory();
        Button macButton = macFactory.createButton();
        TextBox macTextBox = macFactory.createTextBox();
        
        macButton.render();
        macTextBox.render();
        macButton.onClick();
        macTextBox.inputText("Mac文本");
    }
    
    /**
     * 演示产品族使用
     */
    private static void demonstrateProductFamily() {
        // 使用工厂1创建产品族
        AbstractFactory factory1 = new ConcreteFactory1();
        ProductFamily family1 = factory1.createProductFamily();
        System.out.println(family1.getFamilyInfo());
        family1.useFamily();
        
        System.out.println();
        
        // 使用工厂2创建产品族
        AbstractFactory factory2 = new ConcreteFactory2();
        ProductFamily family2 = factory2.createProductFamily();
        System.out.println(family2.getFamilyInfo());
        family2.useFamily();
    }
    
    /**
     * 演示UI组件族使用
     */
    private static void demonstrateUIComponentFamily() {
        // Windows UI组件族
        UIFactory windowsFactory = new WindowsUIFactory();
        UIComponentFamily windowsFamily = windowsFactory.createUIComponentFamily();
        System.out.println(windowsFamily.getFamilyInfo());
        windowsFamily.renderFamily();
        windowsFamily.simulateUserInteraction();
        
        System.out.println();
        
        // Mac UI组件族
        UIFactory macFactory = new MacUIFactory();
        UIComponentFamily macFamily = macFactory.createUIComponentFamily();
        System.out.println(macFamily.getFamilyInfo());
        macFamily.renderFamily();
        macFamily.simulateUserInteraction();
    }
}