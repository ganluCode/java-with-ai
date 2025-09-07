package cn.geekslife.designpattern.decorator;

/**
 * 简单咖啡 - 具体组件
 */
public class SimpleCoffee implements Coffee {
    @Override
    public double getCost() {
        return 2.0;
    }
    
    @Override
    public String getDescription() {
        return "简单咖啡";
    }
}