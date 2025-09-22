package cn.geekslife.twophase;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 带超时机制的优雅终止线程类
 * 演示Two-Phase Termination模式的高级特性
 */
public class TimeoutGracefulThread extends GracefulThread {
    // 超时时间（毫秒）
    private final long timeout;
    // 终止时间戳
    private volatile long shutdownTime = 0;
    // 是否已强制终止
    private final AtomicBoolean forceTerminated = new AtomicBoolean(false);
    
    /**
     * 构造函数
     * @param timeout 超时时间（毫秒）
     */
    public TimeoutGracefulThread(long timeout) {
        this.timeout = timeout;
    }
    
    /**
     * 请求终止线程
     */
    @Override
    public void shutdown() {
        System.out.println(getName() + "：收到终止请求，超时时间: " + timeout + "ms");
        shutdownTime = System.currentTimeMillis();
        super.shutdown();
    }
    
    /**
     * 检查是否超时
     * @return 是否超时
     */
    protected boolean isTimeout() {
        if (shutdownTime == 0) {
            return false;
        }
        return System.currentTimeMillis() - shutdownTime > timeout;
    }
    
    /**
     * 执行具体工作
     * @throws InterruptedException 如果线程被中断
     */
    @Override
    protected void doWork() throws InterruptedException {
        System.out.println(getName() + "：执行工作中...");
        
        // 模拟耗时操作
        Thread.sleep(1000);
        
        // 检查是否超时
        if (isTimeout()) {
            System.out.println(getName() + "：检测到超时，强制终止");
            forceTerminated.set(true);
            throw new InterruptedException("超时强制终止");
        }
    }
    
    /**
     * 清理资源
     */
    @Override
    protected void cleanup() {
        super.cleanup();
        if (forceTerminated.get()) {
            System.out.println(getName() + "：由于超时被强制终止");
        } else {
            System.out.println(getName() + "：正常终止");
        }
    }
    
    /**
     * 检查是否被强制终止
     * @return 是否被强制终止
     */
    public boolean isForceTerminated() {
        return forceTerminated.get();
    }
}