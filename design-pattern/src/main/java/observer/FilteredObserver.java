package observer;

/**
 * 带过滤条件的观察者
 * 只在满足特定条件时才接收通知
 */
public abstract class FilteredObserver implements Observer {
    private Subject subject;
    
    public FilteredObserver(Subject subject) {
        this.subject = subject;
        subject.attach(this);
    }
    
    // 过滤条件抽象方法
    protected abstract boolean shouldUpdate(Subject subject);
    
    @Override
    public final void update(Subject subject) {
        // 只有满足过滤条件时才执行更新
        if (shouldUpdate(subject)) {
            doUpdate(subject);
        }
    }
    
    // 实际更新逻辑抽象方法
    protected abstract void doUpdate(Subject subject);
    
    // 获取订阅的主题
    protected Subject getSubject() {
        return subject;
    }
}