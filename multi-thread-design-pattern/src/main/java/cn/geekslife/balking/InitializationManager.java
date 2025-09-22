package cn.geekslife.balking;

/**
 * 初始化管理器 - 演示Balking模式在初始化操作中的应用
 */
public class InitializationManager {
    // 是否已初始化标志
    private boolean initialized = false;
    // 初始化结果
    private String result;
    
    /**
     * 初始化操作 - Balking模式实现
     * 初始化操作只能执行一次，重复调用时会放弃执行
     * @return 初始化结果
     * @throws IllegalStateException 如果初始化失败
     */
    public synchronized String initialize() {
        // Balking模式：如果已初始化，则放弃执行
        if (initialized) {
            System.out.println(Thread.currentThread().getName() + "：系统已初始化，放弃重复初始化");
            return result;
        }
        
        // 执行初始化操作
        result = doInitialize();
        // 标记为已初始化
        initialized = true;
        System.out.println(Thread.currentThread().getName() + "：系统初始化完成");
        return result;
    }
    
    /**
     * 实际的初始化操作
     * @return 初始化结果
     * @throws IllegalStateException 如果初始化失败
     */
    private String doInitialize() {
        System.out.println(Thread.currentThread().getName() + "：正在初始化系统...");
        try {
            // 模拟初始化耗时
            Thread.sleep(500);
            
            // 模拟可能的初始化失败
            if (Math.random() < 0.1) { // 10%概率失败
                throw new RuntimeException("初始化过程中发生错误");
            }
            
            String initResult = "初始化成功 - " + System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "：系统初始化中...");
            return initResult;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("初始化被中断", e);
        }
    }
    
    /**
     * 检查是否已初始化
     * @return 是否已初始化
     */
    public synchronized boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 获取初始化结果
     * @return 初始化结果
     */
    public synchronized String getResult() {
        return result;
    }
    
    /**
     * 重置初始化状态（仅用于测试）
     */
    synchronized void resetForTesting() {
        initialized = false;
        result = null;
    }
}