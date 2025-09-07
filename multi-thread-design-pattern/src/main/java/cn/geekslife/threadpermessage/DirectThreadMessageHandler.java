package cn.geekslife.threadpermessage;

/**
 * 直接创建线程的消息处理器
 * 演示Thread-Per-Message模式的基本实现
 */
public class DirectThreadMessageHandler {
    
    /**
     * 处理消息 - 为每个消息创建新线程
     * @param message 消息
     */
    public void handleMessage(Message message) {
        System.out.println("DirectThreadMessageHandler：接收到消息 " + message);
        
        // 为每个消息创建新线程
        Thread thread = new Thread(() -> {
            message.process();
        }, "MessageThread-" + message.getMessageId());
        
        // 启动线程
        thread.start();
        
        System.out.println("DirectThreadMessageHandler：已启动线程处理消息 " + message.getMessageId());
    }
    
    /**
     * 处理多个消息
     * @param messages 消息数组
     */
    public void handleMessages(Message[] messages) {
        System.out.println("DirectThreadMessageHandler：开始处理 " + messages.length + " 个消息");
        
        for (Message message : messages) {
            handleMessage(message);
        }
    }
}