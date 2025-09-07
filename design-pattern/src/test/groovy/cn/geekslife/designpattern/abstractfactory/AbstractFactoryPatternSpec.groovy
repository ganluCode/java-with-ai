package cn.geekslife.designpattern.abstractfactory

import spock.lang.Specification
import spock.lang.Unroll

/**
 * 抽象工厂模式Spock测试类
 * 使用行为驱动开发(BDD)方式测试抽象工厂模式
 */
class AbstractFactoryPatternSpec extends Specification {
    
    def "应该能够通过具体工厂创建正确的产品族"() {
        given: "创建具体工厂实例"
        AbstractFactory factory1 = new ConcreteFactory1()
        AbstractFactory factory2 = new ConcreteFactory2()
        
        when: "创建产品族"
        AbstractProductA productA1 = factory1.createProductA()
        AbstractProductB productB1 = factory1.createProductB()
        AbstractProductA productA2 = factory2.createProductA()
        AbstractProductB productB2 = factory2.createProductB()
        
        then: "验证创建的产品类型正确"
        productA1 instanceof ProductA1
        productB1 instanceof ProductB1
        productA2 instanceof ProductA2
        productB2 instanceof ProductB2
        
        and: "验证产品属性正确"
        productA1.getName() == "产品A1"
        productA1.getPrice() == 100.0
        productB1.getType() == "类型B1"
        productB1.getDescription() == "这是产品B1的详细描述"
        productA2.getName() == "产品A2"
        productA2.getPrice() == 200.0
        productB2.getType() == "类型B2"
        productB2.getDescription() == "这是产品B2的详细描述"
    }
    
    def "应该能够通过具体工厂创建产品族包装对象"() {
        given: "创建具体工厂实例"
        AbstractFactory factory1 = new ConcreteFactory1()
        AbstractFactory factory2 = new ConcreteFactory2()
        
        when: "创建产品族包装对象"
        ProductFamily family1 = factory1.createProductFamily()
        ProductFamily family2 = factory2.createProductFamily()
        
        then: "验证产品族包装对象正确"
        family1.getProductA() instanceof ProductA1
        family1.getProductB() instanceof ProductB1
        family2.getProductA() instanceof ProductA2
        family2.getProductB() instanceof ProductB2
        
        and: "验证产品族信息正确"
        family1.getFamilyInfo().contains("产品A1")
        family1.getFamilyInfo().contains("类型B1")
        family2.getFamilyInfo().contains("产品A2")
        family2.getFamilyInfo().contains("类型B2")
    }
    
    def "产品应该能够正确执行其行为"() {
        given: "创建具体产品实例"
        AbstractProductA productA1 = new ProductA1()
        AbstractProductB productB1 = new ProductB1()
        AbstractProductA productA2 = new ProductA2()
        AbstractProductB productB2 = new ProductB2()
        
        when: "执行产品行为"
        productA1.use()
        productB1.operate()
        productA2.use()
        productB2.operate()
        
        then: "验证行为执行成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "应该能够通过UI工厂创建正确的UI组件族"() {
        given: "创建具体UI工厂实例"
        UIFactory windowsFactory = new WindowsUIFactory()
        UIFactory macFactory = new MacUIFactory()
        
        when: "创建UI组件"
        Button windowsButton = windowsFactory.createButton()
        TextBox windowsTextBox = windowsFactory.createTextBox()
        Button macButton = macFactory.createButton()
        TextBox macTextBox = macFactory.createTextBox()
        
        then: "验证创建的UI组件类型正确"
        windowsButton instanceof WindowsButton
        windowsTextBox instanceof WindowsTextBox
        macButton instanceof MacButton
        macTextBox instanceof MacTextBox
        
        and: "验证UI组件属性正确"
        windowsButton.getText() == "Windows按钮"
        macButton.getText() == "Mac按钮"
    }
    
    def "UI组件应该能够正确执行其行为"() {
        given: "创建具体UI组件实例"
        Button windowsButton = new WindowsButton()
        TextBox windowsTextBox = new WindowsTextBox()
        Button macButton = new MacButton()
        TextBox macTextBox = new MacTextBox()
        
        when: "执行UI组件行为"
        windowsButton.render()
        windowsButton.onClick()
        windowsTextBox.render()
        windowsTextBox.inputText("测试文本")
        
        macButton.render()
        macButton.onClick()
        macTextBox.render()
        macTextBox.inputText("测试文本")
        
        then: "验证行为执行成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "应该能够通过UI工厂创建UI组件族包装对象"() {
        given: "创建具体UI工厂实例"
        UIFactory windowsFactory = new WindowsUIFactory()
        UIFactory macFactory = new MacUIFactory()
        
        when: "创建UI组件族包装对象"
        UIComponentFamily windowsFamily = windowsFactory.createUIComponentFamily()
        UIComponentFamily macFamily = macFactory.createUIComponentFamily()
        
        then: "验证UI组件族包装对象正确"
        windowsFamily.getButton() instanceof WindowsButton
        windowsFamily.getTextBox() instanceof WindowsTextBox
        macFamily.getButton() instanceof MacButton
        macFamily.getTextBox() instanceof MacTextBox
        
        and: "验证UI组件族信息正确"
        windowsFamily.getFamilyInfo().contains("WindowsButton")
        windowsFamily.getFamilyInfo().contains("WindowsTextBox")
        macFamily.getFamilyInfo().contains("MacButton")
        macFamily.getFamilyInfo().contains("MacTextBox")
    }
    
    @Unroll
    def "应该能够根据工厂类型创建正确的产品族: #factoryClass.simpleName"() {
        given: "创建工厂实例"
        AbstractFactory factory = factoryClass.newInstance()
        
        when: "创建产品族"
        AbstractProductA productA = factory.createProductA()
        AbstractProductB productB = factory.createProductB()
        
        then: "验证创建的产品族正确"
        productA.getClass() == expectedProductAClass
        productB.getClass() == expectedProductBClass
        
        where: "测试数据"
        factoryClass          | expectedProductAClass | expectedProductBClass
        ConcreteFactory1.class | ProductA1.class       | ProductB1.class
        ConcreteFactory2.class | ProductA2.class       | ProductB2.class
    }
    
    @Unroll
    def "应该能够根据UI工厂类型创建正确的UI组件族: #factoryClass.simpleName"() {
        given: "创建UI工厂实例"
        UIFactory factory = factoryClass.newInstance()
        
        when: "创建UI组件族"
        Button button = factory.createButton()
        TextBox textBox = factory.createTextBox()
        
        then: "验证创建的UI组件族正确"
        button.getClass() == expectedButtonClass
        textBox.getClass() == expectedTextBoxClass
        
        where: "测试数据"
        factoryClass        | expectedButtonClass | expectedTextBoxClass
        WindowsUIFactory.class | WindowsButton.class  | WindowsTextBox.class
        MacUIFactory.class     | MacButton.class      | MacTextBox.class
    }
    
    def "产品族包装对象应该能够正确使用产品族"() {
        given: "创建产品族包装对象"
        AbstractFactory factory1 = new ConcreteFactory1()
        ProductFamily family1 = factory1.createProductFamily()
        
        when: "使用产品族"
        family1.useFamily()
        
        then: "验证产品族使用成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "UI组件族包装对象应该能够正确渲染和交互"() {
        given: "创建UI组件族包装对象"
        UIFactory windowsFactory = new WindowsUIFactory()
        UIComponentFamily windowsFamily = windowsFactory.createUIComponentFamily()
        
        when: "渲染和交互UI组件族"
        windowsFamily.renderFamily()
        windowsFamily.simulateUserInteraction()
        
        then: "验证UI组件族操作成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "应该能够设置和获取产品及UI组件的属性"() {
        given: "创建产品和UI组件实例"
        AbstractProductA productA = new ProductA1()
        AbstractProductB productB = new ProductB1()
        Button button = new WindowsButton()
        TextBox textBox = new WindowsTextBox()
        
        when: "设置属性"
        button.setText("新按钮文本")
        textBox.setPlaceholder("新提示信息")
        
        then: "验证属性设置正确"
        button.getText() == "新按钮文本"
        textBox.getText() == ""
        
        when: "输入文本"
        textBox.inputText("输入的文本")
        
        then: "验证文本输入正确"
        textBox.getText() == "输入的文本"
    }
    
    def "抽象工厂应该保持产品族的一致性"() {
        given: "创建具体工厂实例"
        AbstractFactory factory1 = new ConcreteFactory1()
        AbstractFactory factory2 = new ConcreteFactory2()
        
        when: "分别创建多个产品"
        AbstractProductA productA1_first = factory1.createProductA()
        AbstractProductB productB1_first = factory1.createProductB()
        AbstractProductA productA1_second = factory1.createProductA()
        AbstractProductB productB1_second = factory1.createProductB()
        
        AbstractProductA productA2_first = factory2.createProductA()
        AbstractProductB productB2_first = factory2.createProductB()
        AbstractProductA productA2_second = factory2.createProductA()
        AbstractProductB productB2_second = factory2.createProductB()
        
        then: "验证同一工厂创建的产品属于同一产品族"
        productA1_first.getClass() == productA1_second.getClass()
        productB1_first.getClass() == productB1_second.getClass()
        productA2_first.getClass() == productA2_second.getClass()
        productB2_first.getClass() == productB2_second.getClass()
        
        and: "验证不同工厂创建的产品属于不同产品族"
        productA1_first.getClass() != productA2_first.getClass()
        productB1_first.getClass() != productB2_first.getClass()
    }
}