package cn.geekslife.volatiletest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * volatile 防止指令重排序演示类
 * 
 * 该演示类通过多线程并发执行来展示 volatile 如何防止指令重排序
 */
public class VolatileReorderingDemo {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("开始测试 volatile 防止指令重排序功能...");
        
        // 测试非 volatile 变量的重排序情况
        testNonVolatileReordering();
        
        // 测试 volatile 变量防止重排序的情况
        testVolatileReordering();
        
        // 测试更复杂的重排序场景
        testComplexReordering();
    }
    
    /**
     * 测试非 volatile 变量的重排序情况
     */
    public static void testNonVolatileReordering() throws InterruptedException {
        System.out.println("\n=== 测试非 volatile 变量的重排序 ===");
        VolatileReorderingTest test = new VolatileReorderingTest();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // 多次运行测试以增加重排序的概率
        for (int i = 0; i < 1000; i++) {
            test.resetCounters();
            
            // 提交写入线程
            executor.submit(() -> {
                for (int j = 0; j < 10000; j++) {
                    test.nonVolatileWrite();
                }
            });
            
            // 提交读取线程
            executor.submit(() -> {
                for (int j = 0; j < 10000; j++) {
                    test.nonVolatileRead();
                }
            });
            
            Thread.sleep(1); // 短暂等待
        }
        
        executor.shutdownNow();
        System.out.println("非 volatile 变量重排序次数: " + test.getNonVolatileReorderCount());
        System.out.println("说明：非 volatile 变量可能会发生指令重排序");
    }
    
    /**
     * 测试 volatile 变量防止重排序的情况
     */
    public static void testVolatileReordering() throws InterruptedException {
        System.out.println("\n=== 测试 volatile 变量防止重排序 ===");
        VolatileReorderingTest test = new VolatileReorderingTest();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // 多次运行测试
        for (int i = 0; i < 1000; i++) {
            test.resetCounters();
            
            // 提交写入线程
            executor.submit(() -> {
                for (int j = 0; j < 10000; j++) {
                    test.volatileWrite();
                }
            });
            
            // 提交读取线程
            executor.submit(() -> {
                for (int j = 0; j < 10000; j++) {
                    test.volatileRead();
                }
            });
            
            Thread.sleep(1); // 短暂等待
        }
        
        executor.shutdownNow();
        System.out.println("volatile 变量重排序次数: " + test.getVolatileReorderCount());
        System.out.println("说明：volatile 变量不会发生指令重排序");
    }
    
    /**
     * 测试更复杂的重排序场景
     */
    public static void testComplexReordering() throws InterruptedException {
        System.out.println("\n=== 测试更复杂的重排序场景 ===");
        VolatileReorderingTest test = new VolatileReorderingTest();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        int nonVolatileCount = 0;
        int volatileCount = 0;
        
        // 运行多次测试
        for (int i = 0; i < 100; i++) {
            test.resetCounters();
            
            // 提交两个非 volatile 写入线程
            executor.submit(() -> {
                for (int j = 0; j < 50000; j++) {
                    test.nonVolatileWrite();
                }
            });
            
            executor.submit(() -> {
                for (int j = 0; j < 50000; j++) {
                    test.complexNonVolatileWrite();
                }
            });
            
            // 提交两个非 volatile 读取线程
            executor.submit(() -> {
                for (int j = 0; j < 50000; j++) {
                    test.nonVolatileRead();
                    test.complexNonVolatileRead();
                }
            });
            
            Thread.sleep(10); // 短暂等待
            nonVolatileCount += test.getNonVolatileReorderCount();
        }
        
        // 测试 volatile 场景
        for (int i = 0; i < 100; i++) {
            test.resetCounters();
            
            // 提交两个 volatile 写入线程
            executor.submit(() -> {
                for (int j = 0; j < 50000; j++) {
                    test.volatileWrite();
                }
            });
            
            executor.submit(() -> {
                for (int j = 0; j < 50000; j++) {
                    test.complexVolatileWrite();
                }
            });
            
            // 提交两个 volatile 读取线程
            executor.submit(() -> {
                for (int j = 0; j < 50000; j++) {
                    test.volatileRead();
                    test.complexVolatileRead();
                }
            });
            
            Thread.sleep(10); // 短暂等待
            volatileCount += test.getVolatileReorderCount();
        }
        
        executor.shutdownNow();
        System.out.println("复杂场景下非 volatile 变量重排序次数: " + nonVolatileCount);
        System.out.println("复杂场景下 volatile 变量重排序次数: " + volatileCount);
        System.out.println("说明：volatile 变量通过内存屏障防止指令重排序");
    }
}