package cn.geekslife.threadpermessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用线程池的消息处理器
 * 演示Thread-Per-Message模式的线程池实现
 */
public class ThreadPoolMessageHandler {
    private final ExecutorService executorService;
    
    /**
     * 构造函数
     * @param threadPoolSize 线程池大小
     */
    public ThreadPoolMessageHandler(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        System.out.println("ThreadPoolMessageHandler：创建线程池，大小为 " + threadPoolSize);
    }
    
    /**
     * 处理消息 - 使用线程池处理消息
     * @param message 消息
     */
    public void handleMessage(Message message) {
        System.out.println("ThreadPoolMessageHandler：接收到消息 " + message);
        
        // 使用线程池处理消息
        executorService.submit(() -> {
            message.process();
        });
        
        System.out.println("ThreadPoolMessageHandler：已提交消息 " + message.getMessageId() + " 到线程池");
    }
    
    /**
     * 处理多个消息
     * @param messages 消息数组
     */
    public void handleMessages(Message[] messages) {
        System.out.println("ThreadPoolMessageHandler：开始处理 " + messages.length + " 个消息");
        
        for (Message message : messages) {
            handleMessage(message);
        }
    }
    
    /**
     * 关闭线程池
     */
    public void shutdown() {
        System.out.println("ThreadPoolMessageHandler：关闭线程池");
        executorService.shutdown();
    }
    
    /**
     * 立即关闭线程池
     */
    public void shutdownNow() {
        System.out.println("ThreadPoolMessageHandler：立即关闭线程池");
        executorService.shutdownNow();
    }
}