package cn.geekslife.twophase;

/**
 * 优雅线程类 - Two-Phase Termination模式的基础实现
 */
public class GracefulThread extends Thread {
    // 终止请求标志
    private volatile boolean shutdownRequested = false;
    
    /**
     * 请求终止线程
     */
    public void shutdown() {
        System.out.println(getName() + "：收到终止请求");
        shutdownRequested = true;
        interrupt(); // 中断线程
    }
    
    /**
     * 检查是否请求了终止
     * @return 是否请求了终止
     */
    public boolean isShutdownRequested() {
        return shutdownRequested;
    }
    
    /**
     * 线程主逻辑
     */
    @Override
    public void run() {
        try {
            while (!isShutdownRequested()) {
                doWork();
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + "：捕获到中断异常");
            Thread.currentThread().interrupt(); // 重新设置中断状态
        } finally {
            cleanup();
            System.out.println(getName() + "：线程安全终止");
        }
    }
    
    /**
     * 执行具体工作 - 子类需要重写此方法
     * @throws InterruptedException 如果线程被中断
     */
    protected void doWork() throws InterruptedException {
        // 默认实现：休眠100毫秒
        Thread.sleep(100);
    }
    
    /**
     * 清理资源 - 子类需要重写此方法
     */
    protected void cleanup() {
        System.out.println(getName() + "：执行清理工作");
    }
}