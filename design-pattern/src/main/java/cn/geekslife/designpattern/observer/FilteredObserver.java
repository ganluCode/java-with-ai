package cn.geekslife.designpattern.observer;

/**
 * 带过滤条件的观察者
 * 只在满足特定条件时才接收通知
 */
public abstract class FilteredObserver implements Observer {
    private Object subject;
    
    public FilteredObserver(Object subject) {
        this.subject = subject;
        // 注意：这里需要确保传入的对象有attach方法
        if (subject instanceof Subject) {
            ((Subject) subject).attach(this);
        }
    }
    
    // 过滤条件抽象方法
    protected abstract boolean shouldUpdate(Object subject);
    
    @Override
    public final void update(Object subject) {
        // 只有满足过滤条件时才执行更新
        if (shouldUpdate(subject)) {
            doUpdate(subject);
        }
    }
    
    // 实际更新逻辑抽象方法
    protected abstract void doUpdate(Object subject);
    
    // 获取订阅的主题
    protected Object getSubject() {
        return subject;
    }
}