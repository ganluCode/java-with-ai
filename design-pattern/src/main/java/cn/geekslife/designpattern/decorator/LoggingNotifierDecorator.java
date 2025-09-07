package cn.geekslife.designpattern.decorator;

/**
 * 日志通知装饰器 - 具体装饰器
 */
public class LoggingNotifierDecorator extends NotifierDecorator {
    public LoggingNotifierDecorator(Notifier notifier) {
        super(notifier);
    }
    
    @Override
    public void send(String message) {
        System.out.println("[LOG] 准备发送消息: " + message);
        long startTime = System.currentTimeMillis();
        super.send(message);
        long endTime = System.currentTimeMillis();
        System.out.println("[LOG] 消息发送完成，耗时: " + (endTime - startTime) + "ms");
    }
}