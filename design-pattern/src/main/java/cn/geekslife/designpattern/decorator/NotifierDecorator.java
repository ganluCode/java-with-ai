package cn.geekslife.designpattern.decorator;

/**
 * 通知装饰器抽象类 - 装饰器
 */
public abstract class NotifierDecorator implements Notifier {
    protected Notifier notifier;
    
    public NotifierDecorator(Notifier notifier) {
        this.notifier = notifier;
    }
    
    @Override
    public void send(String message) {
        notifier.send(message);
    }
}