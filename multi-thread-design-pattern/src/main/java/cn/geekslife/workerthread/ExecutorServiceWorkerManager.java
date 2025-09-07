package cn.geekslife.workerthread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 使用ExecutorService的工作线程管理器
 * 演示Worker Thread模式的最佳实践实现
 */
public class ExecutorServiceWorkerManager {
    private final ExecutorService executorService;
    private final int workerCount;
    
    /**
     * 构造函数
     * @param workerCount 工作线程数量
     */
    public ExecutorServiceWorkerManager(int workerCount) {
        this.workerCount = workerCount;
        // 创建固定大小的线程池
        this.executorService = Executors.newFixedThreadPool(workerCount);
        System.out.println("ExecutorServiceWorkerManager：创建线程池，工作线程数量: " + workerCount);
    }
    
    /**
     * 提交任务
     * @param request 请求
     */
    public void submit(Request request) {
        System.out.println("ExecutorServiceWorkerManager：提交请求 " + request);
        
        executorService.submit(() -> {
            request.execute();
        });
    }
    
    /**
     * 批量提交任务
     * @param requests 请求数组
     */
    public void submitBatch(Request[] requests) {
        System.out.println("ExecutorServiceWorkerManager：批量提交 " + requests.length + " 个请求");
        
        for (Request request : requests) {
            submit(request);
        }
    }
    
    /**
     * 关闭工作线程管理器
     */
    public void shutdown() {
        System.out.println("ExecutorServiceWorkerManager：关闭工作线程管理器");
        executorService.shutdown();
        
        try {
            // 等待最多10秒让现有任务完成
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("ExecutorServiceWorkerManager：强制关闭未完成的任务");
                executorService.shutdownNow();
                
                // 再等待10秒让任务响应中断
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("ExecutorServiceWorkerManager：线程池未能正常关闭");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("ExecutorServiceWorkerManager：关闭过程中被中断");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("ExecutorServiceWorkerManager：工作线程管理器已关闭");
    }
    
    /**
     * 立即关闭工作线程管理器
     */
    public void shutdownNow() {
        System.out.println("ExecutorServiceWorkerManager：立即关闭工作线程管理器");
        executorService.shutdownNow();
    }
    
    /**
     * 获取工作线程数量
     * @return 工作线程数量
     */
    public int getWorkerCount() {
        return workerCount;
    }
}