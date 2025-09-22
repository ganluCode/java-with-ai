package cn.geekslife.volatiletest;

/**
 * DCL（双重检查锁定）单例模式中的重排序问题演示
 * 这是一个更容易观察到重排序问题的场景
 */
public class DCLSingletonReorderingDemo {
    // 非 volatile 实例 - 可能出现重排序问题
    private static DCLSingletonReorderingDemo instance = null;
    
    // volatile 实例 - 防止重排序问题
    private static volatile DCLSingletonReorderingDemo volatileInstance = null;
    
    private int value = 42;
    
    private DCLSingletonReorderingDemo() {}
    
    /**
     * 非 volatile 版本的 getInstance - 可能出现重排序问题
     * 在多线程环境下，可能返回未完全初始化的对象
     */
    public static DCLSingletonReorderingDemo getInstance() {
        if (instance == null) {  // 第一次检查
            synchronized (DCLSingletonReorderingDemo.class) {
                if (instance == null) {  // 第二次检查
                    // 可能的重排序：
                    // 1. 分配内存空间
                    // 2. 将 instance 指向分配的内存地址（此时对象还未初始化）
                    // 3. 初始化对象
                    instance = new DCLSingletonReorderingDemo();
                }
            }
        }
        return instance;
    }
    
    /**
     * volatile 版本的 getInstance - 防止重排序问题
     * volatile 确保了对象的完全初始化后才将引用赋值给 volatileInstance
     */
    public static DCLSingletonReorderingDemo getVolatileInstance() {
        if (volatileInstance == null) {  // 第一次检查
            synchronized (DCLSingletonReorderingDemo.class) {
                if (volatileInstance == null) {  // 第二次检查
                    // volatile 防止重排序，确保：
                    // 1. 分配内存空间
                    // 2. 初始化对象
                    // 3. 将 volatileInstance 指向完全初始化的对象
                    volatileInstance = new DCLSingletonReorderingDemo();
                }
            }
        }
        return volatileInstance;
    }
    
    public int getValue() {
        return value;
    }
    
    /**
     * 模拟可能出现的问题场景
     * 在实际运行中，由于重排序问题，可能返回未初始化的 value（默认值0）
     */
    public static void demonstratePotentialIssue() {
        System.out.println("=== DCL 单例模式重排序问题演示 ===");
        System.out.println("注意：在现代JVM中，这个问题可能不容易复现");
        System.out.println("但在某些特定条件下（如JVM版本、硬件平台）可能观察到");
        
        // 创建多个线程同时获取实例
        Thread[] threads = new Thread[10];
        int[] nonVolatileResults = new int[10];
        int[] volatileResults = new int[10];
        
        // 重置实例
        instance = null;
        volatileInstance = null;
        
        // 启动多个线程测试非 volatile 版本
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    Thread.sleep(1); // 增加并发可能性
                    DCLSingletonReorderingDemo obj = getInstance();
                    if (obj != null) {
                        nonVolatileResults[index] = obj.getValue();
                    }
                } catch (Exception e) {
                    nonVolatileResults[index] = -1; // 异常情况
                }
            });
        }
        
        // 启动所有线程
        for (Thread t : threads) {
            t.start();
        }
        
        // 等待所有线程完成
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // 输出结果
        System.out.println("非 volatile 版本获取到的值:");
        for (int i = 0; i < 10; i++) {
            System.out.print(nonVolatileResults[i] + " ");
        }
        System.out.println();
        
        // 重置实例
        instance = null;
        volatileInstance = null;
        
        // 启动多个线程测试 volatile 版本
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    Thread.sleep(1); // 增加并发可能性
                    DCLSingletonReorderingDemo obj = getVolatileInstance();
                    if (obj != null) {
                        volatileResults[index] = obj.getValue();
                    }
                } catch (Exception e) {
                    volatileResults[index] = -1; // 异常情况
                }
            });
        }
        
        // 启动所有线程
        for (Thread t : threads) {
            t.start();
        }
        
        // 等待所有线程完成
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // 输出结果
        System.out.println("volatile 版本获取到的值:");
        for (int i = 0; i < 10; i++) {
            System.out.print(volatileResults[i] + " ");
        }
        System.out.println();
        
        System.out.println("\n理论解释：");
        System.out.println("1. 非 volatile 版本：由于指令重排序，可能返回未完全初始化的对象（value为0）");
        System.out.println("2. volatile 版本：通过内存屏障防止重排序，确保对象完全初始化后才返回");
    }
    
    public static void main(String[] args) {
        demonstratePotentialIssue();
    }
}