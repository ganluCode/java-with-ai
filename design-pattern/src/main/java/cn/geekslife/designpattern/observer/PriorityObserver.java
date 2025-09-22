package cn.geekslife.designpattern.observer;

/**
 * 带优先级的观察者接口
 * 支持按优先级顺序通知观察者
 */
public interface PriorityObserver extends Observer {
    /**
     * 获取观察者优先级
     * 数值越小优先级越高
     * @return 优先级数值
     */
    int getPriority();
}