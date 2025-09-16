package cn.geekslife.designpattern.flyweight;

/**
 * 具体享元类
 * 实现抽象享元接口，为内部状态增加存储空间
 */
public class ConcreteFlyweight extends Flyweight {
    // 内部状态，可以被共享
    private String intrinsicState;
    
    public ConcreteFlyweight(String intrinsicState) {
        this.intrinsicState = intrinsicState;
    }
    
    /**
     * 实现操作方法
     * @param extrinsicState 外部状态，随环境变化而变化
     */
    @Override
    public void operation(String extrinsicState) {
        System.out.println("ConcreteFlyweight: 内部状态 [" + intrinsicState + "] 外部状态 [" + extrinsicState + "]");
    }
}