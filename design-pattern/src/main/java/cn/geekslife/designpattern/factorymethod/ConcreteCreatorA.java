package cn.geekslife.designpattern.factorymethod;

/**
 * 具体创建者A
 * 实现工厂方法，创建具体产品A
 */
public class ConcreteCreatorA extends Creator {
    
    @Override
    public Product factoryMethod() {
        return new ConcreteProductA();
    }
}