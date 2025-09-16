package cn.geekslife.designpattern.mediator;

/**
 * 组件基类
 * 所有具体组件的基类
 */
public abstract class Component {
    protected Mediator mediator;
    
    public Component(Mediator mediator) {
        this.mediator = mediator;
    }
    
    /**
     * 设置中介者
     * @param mediator 中介者
     */
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
    
    /**
     * 获取组件名称
     * @return 组件名称
     */
    public abstract String getName();
}