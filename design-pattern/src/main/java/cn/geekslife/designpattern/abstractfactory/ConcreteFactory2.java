package cn.geekslife.designpattern.abstractfactory;

/**
 * 具体工厂2
 * 实现抽象工厂接口，创建产品族2
 */
public class ConcreteFactory2 implements AbstractFactory {
    
    @Override
    public AbstractProductA createProductA() {
        return new ProductA2();
    }
    
    @Override
    public AbstractProductB createProductB() {
        return new ProductB2();
    }
}