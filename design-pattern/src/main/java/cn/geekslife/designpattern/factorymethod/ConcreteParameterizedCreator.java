package cn.geekslife.designpattern.factorymethod;

/**
 * 具体参数化工厂
 * 实现参数化工厂方法
 */
public class ConcreteParameterizedCreator extends ParameterizedCreator {
    
    @Override
    public Product factoryMethod(String type) {
        switch (type.toUpperCase()) {
            case "A":
                return new ConcreteProductA();
            case "B":
                return new ConcreteProductB();
            case "C":
                return new ConcreteProductC();
            default:
                throw new IllegalArgumentException("未知的产品类型: " + type);
        }
    }
}