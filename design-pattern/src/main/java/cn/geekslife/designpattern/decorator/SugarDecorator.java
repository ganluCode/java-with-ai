package cn.geekslife.designpattern.decorator;

/**
 * 糖装饰器 - 具体装饰器
 */
public class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }
    
    @Override
    public double getCost() {
        return super.getCost() + 0.3;
    }
    
    @Override
    public String getDescription() {
        return super.getDescription() + ", 糖";
    }
}