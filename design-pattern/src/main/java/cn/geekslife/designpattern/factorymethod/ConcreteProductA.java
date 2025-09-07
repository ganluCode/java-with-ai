package cn.geekslife.designpattern.factorymethod;

/**
 * 具体产品A
 * 实现抽象产品接口
 */
public class ConcreteProductA implements Product {
    
    private static final String NAME = "产品A";
    private static final double PRICE = 100.0;
    
    @Override
    public void use() {
        System.out.println("使用" + NAME + "，价格：" + PRICE + "元");
    }
    
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public double getPrice() {
        return PRICE;
    }
}