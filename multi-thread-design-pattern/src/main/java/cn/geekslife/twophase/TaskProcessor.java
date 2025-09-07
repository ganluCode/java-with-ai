package cn.geekslife.twophase;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 任务处理器类 - 演示Two-Phase Termination模式在任务处理中的应用
 */
public class TaskProcessor extends GracefulThread {
    // 任务队列
    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    
    /**
     * 提交任务
     * @param task 任务
     */
    public void submit(Runnable task) {
        if (!isShutdownRequested()) {
            try {
                queue.put(task);
                System.out.println(getName() + "：提交任务到队列");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(getName() + "：提交任务时被中断");
            }
        } else {
            System.out.println(getName() + "：已请求终止，无法提交新任务");
        }
    }
    
    /**
     * 执行具体工作
     * @throws InterruptedException 如果线程被中断
     */
    @Override
    protected void doWork() throws InterruptedException {
        System.out.println(getName() + "：等待任务...");
        
        // 从队列中获取任务（可中断）
        Runnable task = queue.take();
        
        System.out.println(getName() + "：处理任务");
        
        try {
            task.run();
            System.out.println(getName() + "：任务处理完成");
        } catch (Exception e) {
            System.err.println(getName() + "：任务处理异常: " + e.getMessage());
        }
    }
    
    /**
     * 清理资源
     */
    @Override
    protected void cleanup() {
        super.cleanup();
        System.out.println(getName() + "：清空任务队列，剩余任务数: " + queue.size());
        queue.clear();
        System.out.println(getName() + "：任务处理器已关闭");
    }
    
    /**
     * 获取队列大小
     * @return 队列大小
     */
    public int getQueueSize() {
        return queue.size();
    }
}