package cn.geekslife.designpattern.factorymethod;

/**
 * 具体创建者C
 * 实现工厂方法，创建具体产品C
 */
public class ConcreteCreatorC extends Creator {
    
    @Override
    public Product factoryMethod() {
        return new ConcreteProductC();
    }
}