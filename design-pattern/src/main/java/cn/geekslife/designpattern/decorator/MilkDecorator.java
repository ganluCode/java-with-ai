package cn.geekslife.designpattern.decorator;

/**
 * 牛奶装饰器 - 具体装饰器
 */
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }
    
    @Override
    public double getCost() {
        return super.getCost() + 0.5;
    }
    
    @Override
    public String getDescription() {
        return super.getDescription() + ", 牛奶";
    }
}