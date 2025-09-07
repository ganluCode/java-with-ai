package cn.geekslife.balking;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 使用AtomicBoolean实现的Balking模式
 * 演示如何使用原子类实现线程安全的Balking模式
 */
public class AtomicBalkingExample {
    // 使用AtomicBoolean确保原子性
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private String data;
    
    /**
     * 初始化操作 - 使用AtomicBoolean实现Balking模式
     * @return 是否成功初始化
     */
    public boolean initialize() {
        // 使用CAS操作实现Balking模式
        if (!initialized.compareAndSet(false, true)) {
            System.out.println(Thread.currentThread().getName() + "：已初始化，放弃执行");
            return false;
        }
        
        // 执行初始化操作
        doInitialize();
        System.out.println(Thread.currentThread().getName() + "：初始化完成");
        return true;
    }
    
    /**
     * 实际的初始化操作
     */
    private void doInitialize() {
        System.out.println(Thread.currentThread().getName() + "：正在初始化...");
        try {
            // 模拟初始化耗时
            Thread.sleep(200);
            data = "初始化数据 - " + System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "：初始化中...");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 获取数据
     * @return 数据
     * @throws IllegalStateException 如果未初始化
     */
    public String getData() {
        if (!initialized.get()) {
            throw new IllegalStateException("尚未初始化");
        }
        return data;
    }
    
    /**
     * 检查是否已初始化
     * @return 是否已初始化
     */
    public boolean isInitialized() {
        return initialized.get();
    }
}