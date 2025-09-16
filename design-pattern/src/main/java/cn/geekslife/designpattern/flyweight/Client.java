package cn.geekslife.designpattern.flyweight;

/**
 * 客户端类
 * 维持对Flyweight的引用，计算或存储Flyweight的外部状态
 */
public class Client {
    private Flyweight flyweight;
    private String externalState;
    
    public Client(Flyweight flyweight, String externalState) {
        this.flyweight = flyweight;
        this.externalState = externalState;
    }
    
    /**
     * 执行操作
     */
    public void operation() {
        flyweight.operation(externalState);
    }
    
    /**
     * 设置外部状态
     * @param externalState 外部状态
     */
    public void setExternalState(String externalState) {
        this.externalState = externalState;
    }
}