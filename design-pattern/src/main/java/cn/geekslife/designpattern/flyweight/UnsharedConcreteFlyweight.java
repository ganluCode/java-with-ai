package cn.geekslife.designpattern.flyweight;

/**
 * 非共享具体享元类
 * 并非所有Flyweight的子类都需要被共享
 */
public class UnsharedConcreteFlyweight extends Flyweight {
    private String allState;
    
    public UnsharedConcreteFlyweight(String allState) {
        this.allState = allState;
    }
    
    @Override
    public void operation(String extrinsicState) {
        System.out.println("UnsharedConcreteFlyweight: 非共享状态 [" + allState + "] 外部状态 [" + extrinsicState + "]");
    }
}