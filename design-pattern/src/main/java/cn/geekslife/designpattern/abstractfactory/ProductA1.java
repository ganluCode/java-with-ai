package cn.geekslife.designpattern.abstractfactory;

/**
 * 具体产品A1
 * 实现抽象产品A接口
 */
public class ProductA1 implements AbstractProductA {
    
    private static final String NAME = "产品A1";
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