package cn.geekslife.guardedsuspension;

/**
 * 简单的 guarded object 类 - 演示基本的Guarded Suspension模式
 */
public class GuardedObject {
    // 保护的数据
    private Object data;
    // 完成标志
    private boolean ready = false;
    
    /**
     * 获取数据 - Guarded Suspension模式的基本实现
     * @return 数据对象
     * @throws InterruptedException 如果线程被中断
     */
    public synchronized Object get() throws InterruptedException {
        // 等待直到数据准备好
        while (!ready) {
            System.out.println(Thread.currentThread().getName() + "：数据未准备好，等待...");
            wait();
        }
        
        System.out.println(Thread.currentThread().getName() + "：获取数据 " + data);
        return data;
    }
    
    /**
     * 设置数据并通知等待的线程
     * @param data 数据对象
     */
    public synchronized void put(Object data) {
        this.data = data;
        this.ready = true;
        System.out.println(Thread.currentThread().getName() + "：设置数据 " + data);
        // 通知所有等待的线程
        notifyAll();
    }
    
    /**
     * 检查数据是否准备好
     * @return 是否准备好
     */
    public synchronized boolean isReady() {
        return ready;
    }
}