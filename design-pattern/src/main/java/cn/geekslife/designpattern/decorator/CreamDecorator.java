package cn.geekslife.designpattern.decorator;

/**
 * 奶油装饰器 - 具体装饰器
 */
public class CreamDecorator extends CoffeeDecorator {
    public CreamDecorator(Coffee coffee) {
        super(coffee);
    }
    
    @Override
    public double getCost() {
        return super.getCost() + 0.7;
    }
    
    @Override
    public String getDescription() {
        return super.getDescription() + ", 奶油";
    }
}