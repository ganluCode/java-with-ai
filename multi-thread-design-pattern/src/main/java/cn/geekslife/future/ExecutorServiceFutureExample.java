package cn.geekslife.future;

import java.util.concurrent.*;

/**
 * 使用ExecutorService的Future示例
 * 演示Future模式与线程池的结合使用
 */
public class ExecutorServiceFutureExample {
    private final ExecutorService executorService;
    
    /**
     * 构造函数
     */
    public ExecutorServiceFutureExample() {
        this.executorService = Executors.newFixedThreadPool(3);
        System.out.println("ExecutorServiceFutureExample：创建线程池");
    }
    
    /**
     * 提交任务并返回Future
     * @param task 任务
     * @param <T> 返回类型
     * @return Future对象
     */
    public <T> Future<T> submitTask(Callable<T> task) {
        System.out.println("ExecutorServiceFutureExample：提交任务");
        return executorService.submit(task);
    }
    
    /**
     * 批量提交任务
     * @param tasks 任务列表
     * @param <T> 返回类型
     * @return Future列表
     */
    public <T> java.util.List<Future<T>> submitTasks(java.util.List<Callable<T>> tasks) {
        System.out.println("ExecutorServiceFutureExample：批量提交 " + tasks.size() + " 个任务");
        try {
            return executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 关闭线程池
     */
    public void shutdown() {
        System.out.println("ExecutorServiceFutureExample：关闭线程池");
        executorService.shutdown();
    }
    
    /**
     * 立即关闭线程池
     */
    public void shutdownNow() {
        System.out.println("ExecutorServiceFutureExample：立即关闭线程池");
        executorService.shutdownNow();
    }
}