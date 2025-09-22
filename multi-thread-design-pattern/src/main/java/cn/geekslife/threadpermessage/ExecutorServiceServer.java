package cn.geekslife.threadpermessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用ExecutorService的服务器类
 * 演示Thread-Per-Message模式的最佳实践
 */
public class ExecutorServiceServer {
    private final ExecutorService executorService;
    private final AtomicInteger messageCounter = new AtomicInteger(0);
    private volatile boolean running = false;
    
    /**
     * 构造函数
     * @param threadPoolSize 线程池大小
     */
    public ExecutorServiceServer(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        System.out.println("ExecutorServiceServer：创建服务器，线程池大小为 " + threadPoolSize);
    }
    
    /**
     * 启动服务器
     */
    public void start() {
        System.out.println("ExecutorServiceServer：服务器启动");
        running = true;
        
        // 模拟接收消息的主循环
        Thread serverThread = new Thread(() -> {
            while (running) {
                try {
                    // 模拟接收消息
                    receiveMessage();
                    
                    // 随机间隔
                    Thread.sleep((long) (Math.random() * 300) + 50);
                } catch (InterruptedException e) {
                    System.out.println("ExecutorServiceServer：服务器线程被中断");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "ExecutorServerThread");
        
        serverThread.start();
    }
    
    /**
     * 停止服务器
     */
    public void stop() {
        System.out.println("ExecutorServiceServer：服务器停止");
        running = false;
        
        // 关闭线程池
        executorService.shutdown();
    }
    
    /**
     * 模拟接收消息
     */
    private void receiveMessage() {
        int messageId = messageCounter.incrementAndGet();
        Message message = new Message("Executor服务器消息-" + messageId, "EXEC-MSG-" + messageId);
        
        System.out.println("ExecutorServiceServer：接收到消息 " + message);
        
        // 使用线程池处理消息
        executorService.submit(() -> {
            message.process();
        });
    }
    
    /**
     * 获取消息计数
     * @return 消息计数
     */
    public int getMessageCount() {
        return messageCounter.get();
    }
    
    /**
     * 获取活跃线程数
     * @return 活跃线程数
     */
    public int getActiveThreadCount() {
        if (executorService instanceof java.util.concurrent.ThreadPoolExecutor) {
            return ((java.util.concurrent.ThreadPoolExecutor) executorService).getActiveCount();
        }
        return -1;
    }
}