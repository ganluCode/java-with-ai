package cn.geekslife.learning.java.aqs.ratelimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AQS限流器测试类
 */
public class AQSRateLimiterTest {
    
    public static void main(String[] args) throws InterruptedException {
        // 创建一个每秒允许2个请求的限流器
        RateLimiter rateLimiter = RateLimiter.create(2.0);
        
        System.out.println("开始测试限流器，每秒允许2个请求");
        
        // 测试基本获取功能
        testBasicAcquire(rateLimiter);
        
        // 测试并发获取
        testConcurrentAcquire(rateLimiter);
        
        // 测试超时获取
        testTryAcquireWithTimeout(rateLimiter);
    }
    
    /**
     * 测试基本获取功能
     */
    private static void testBasicAcquire(RateLimiter rateLimiter) throws InterruptedException {
        System.out.println("\n=== 测试基本获取功能 ===");
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            double waitTime = rateLimiter.acquire();
            System.out.printf("获取令牌 %d, 等待时间: %.3f秒%n", i + 1, waitTime);
        }
        long end = System.currentTimeMillis();
        
        System.out.printf("总耗时: %d毫秒%n", end - start);
    }
    
    /**
     * 测试并发获取
     */
    private static void testConcurrentAcquire(RateLimiter rateLimiter) throws InterruptedException {
        System.out.println("\n=== 测试并发获取 ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        
        long start = System.currentTimeMillis();
        
        // 提交20个任务
        for (int i = 0; i < 20; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    // 尝试获取令牌
                    double waitTime = rateLimiter.acquire();
                    successCount.incrementAndGet();
                    System.out.printf("任务 %d 获取令牌成功, 等待时间: %.3f秒%n", taskId, waitTime);
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.err.printf("任务 %d 获取令牌失败: %s%n", taskId, e.getMessage());
                }
            });
        }
        
        // 关闭线程池并等待完成
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        
        long end = System.currentTimeMillis();
        
        System.out.printf("并发测试完成，成功: %d, 失败: %d, 总耗时: %d毫秒%n", 
                         successCount.get(), failCount.get(), end - start);
    }
    
    /**
     * 测试超时获取
     */
    private static void testTryAcquireWithTimeout(RateLimiter rateLimiter) throws InterruptedException {
        System.out.println("\n=== 测试超时获取 ===");
        
        // 先消耗一些令牌
        for (int i = 0; i < 3; i++) {
            rateLimiter.acquire();
        }
        
        long start = System.currentTimeMillis();
        
        // 尝试在100毫秒内获取令牌
        boolean acquired = rateLimiter.tryAcquire(1, 100, TimeUnit.MILLISECONDS);
        long end = System.currentTimeMillis();
        
        System.out.printf("超时获取结果: %s, 耗时: %d毫秒%n", acquired ? "成功" : "失败", end - start);
        
        // 等待一段时间后再尝试获取
        Thread.sleep(1000);
        acquired = rateLimiter.tryAcquire(1, 100, TimeUnit.MILLISECONDS);
        System.out.printf("等待后获取结果: %s%n", acquired ? "成功" : "失败");
    }
}