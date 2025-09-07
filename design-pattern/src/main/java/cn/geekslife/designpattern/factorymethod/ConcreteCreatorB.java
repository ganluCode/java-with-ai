package cn.geekslife.designpattern.factorymethod;

/**
 * 具体创建者B
 * 实现工厂方法，创建具体产品B
 */
public class ConcreteCreatorB extends Creator {
    
    @Override
    public Product factoryMethod() {
        return new ConcreteProductB();
    }
}