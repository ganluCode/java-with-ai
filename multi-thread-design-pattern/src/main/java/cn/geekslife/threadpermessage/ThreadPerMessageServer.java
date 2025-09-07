package cn.geekslife.threadpermessage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务器类 - 演示Thread-Per-Message模式在服务器中的应用
 */
public class ThreadPerMessageServer {
    private final AtomicInteger messageCounter = new AtomicInteger(0);
    private volatile boolean running = false;
    
    /**
     * 启动服务器
     */
    public void start() {
        System.out.println("ThreadPerMessageServer：服务器启动");
        running = true;
        
        // 模拟接收消息的主循环
        Thread serverThread = new Thread(() -> {
            while (running) {
                try {
                    // 模拟接收消息
                    receiveMessage();
                    
                    // 随机间隔
                    Thread.sleep((long) (Math.random() * 500) + 100);
                } catch (InterruptedException e) {
                    System.out.println("ThreadPerMessageServer：服务器线程被中断");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "ServerThread");
        
        serverThread.start();
    }
    
    /**
     * 停止服务器
     */
    public void stop() {
        System.out.println("ThreadPerMessageServer：服务器停止");
        running = false;
    }
    
    /**
     * 模拟接收消息
     */
    private void receiveMessage() {
        int messageId = messageCounter.incrementAndGet();
        Message message = new Message("服务器消息-" + messageId, "MSG-" + messageId);
        
        System.out.println("ThreadPerMessageServer：接收到消息 " + message);
        
        // 为每个消息创建新线程处理
        Thread messageThread = new Thread(() -> {
            message.process();
        }, "ServerMessageThread-" + messageId);
        
        messageThread.start();
    }
    
    /**
     * 获取消息计数
     * @return 消息计数
     */
    public int getMessageCount() {
        return messageCounter.get();
    }
}