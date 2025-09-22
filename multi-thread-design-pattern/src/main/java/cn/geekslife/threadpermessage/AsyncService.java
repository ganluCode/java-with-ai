package cn.geekslife.threadpermessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 异步服务类 - 演示Thread-Per-Message模式在异步服务中的应用
 */
public class AsyncService {
    private final ExecutorService executorService;
    
    /**
     * 构造函数
     */
    public AsyncService() {
        // 使用缓存线程池，根据需要创建新线程
        this.executorService = Executors.newCachedThreadPool();
        System.out.println("AsyncService：创建缓存线程池");
    }
    
    /**
     * 异步处理任务
     * @param task 任务
     */
    public void processAsync(Runnable task) {
        System.out.println("AsyncService：提交异步任务");
        
        executorService.submit(task);
    }
    
    /**
     * 异步处理消息
     * @param message 消息
     * @param callback 回调函数
     */
    public void processMessageAsync(Message message, Runnable callback) {
        System.out.println("AsyncService：异步处理消息 " + message.getMessageId());
        
        executorService.submit(() -> {
            try {
                // 处理消息
                message.process();
                
                // 执行回调
                if (callback != null) {
                    callback.run();
                }
            } catch (Exception e) {
                System.err.println("AsyncService：处理消息时发生错误: " + e.getMessage());
            }
        });
    }
    
    /**
     * 延迟执行任务
     * @param task 任务
     * @param delay 延迟时间（毫秒）
     */
    public void scheduleTask(Runnable task, long delay) {
        System.out.println("AsyncService：安排任务在 " + delay + "ms 后执行");
        
        executorService.submit(() -> {
            try {
                Thread.sleep(delay);
                task.run();
            } catch (InterruptedException e) {
                System.out.println("AsyncService：任务被中断");
                Thread.currentThread().interrupt();
            }
        });
    }
    
    /**
     * 关闭服务
     */
    public void shutdown() {
        System.out.println("AsyncService：关闭服务");
        executorService.shutdown();
        
        try {
            // 等待最多5秒让现有任务完成
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("AsyncService：强制关闭未完成的任务");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("AsyncService：关闭过程中被中断");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}