package cn.geekslife.activeobject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用Java并发包的Active Object实现
 * 演示Active Object模式的现代化实现
 */
public class ModernActiveObject {
    private final ExecutorService executorService;
    private final Servant servant;
    
    /**
     * 构造函数
     * @param threadPoolSize 线程池大小
     */
    public ModernActiveObject(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.servant = new Servant();
        System.out.println("ModernActiveObject：创建现代Active Object，线程池大小: " + threadPoolSize);
    }
    
    /**
     * 执行工作 - 使用CompletableFuture实现
     * @param name 工作名称
     * @return CompletableFuture结果对象
     */
    public CompletableFuture<Result> doWork(String name) {
        System.out.println("ModernActiveObject：提交工作请求 " + name);
        
        String taskId = "MODERN-TASK-" + System.currentTimeMillis() + "-" + name.hashCode();
        
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("ModernActiveObject：异步执行工作 " + name + " (ID: " + taskId + ")");
            return servant.doWork(name, taskId);
        }, executorService);
    }
    
    /**
     * 批量执行工作
     * @param names 工作名称数组
     * @return CompletableFuture数组
     */
    public CompletableFuture<Result>[] doWorkBatch(String[] names) {
        System.out.println("ModernActiveObject：批量提交 " + names.length + " 个工作请求");
        
        @SuppressWarnings("unchecked")
        CompletableFuture<Result>[] futures = new CompletableFuture[names.length];
        
        for (int i = 0; i < names.length; i++) {
            futures[i] = doWork(names[i]);
        }
        
        return futures;
    }
    
    /**
     * 关闭Modern Active Object
     */
    public void shutdown() {
        System.out.println("ModernActiveObject：关闭Modern Active Object");
        executorService.shutdown();
    }
    
    /**
     * 立即关闭Modern Active Object
     */
    public void shutdownNow() {
        System.out.println("ModernActiveObject：立即关闭Modern Active Object");
        executorService.shutdownNow();
    }
}