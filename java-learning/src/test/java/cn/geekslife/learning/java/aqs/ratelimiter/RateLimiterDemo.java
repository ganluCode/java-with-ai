package cn.geekslife.learning.java.aqs.ratelimiter;

import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.TokenBucketAlgorithm;
import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.SlidingWindowAlgorithm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流器演示类
 */
public class RateLimiterDemo {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 限流器演示 ===\n");
        
        // 演示令牌桶算法
        demonstrateTokenBucket();
        
        // 演示滑动窗口算法
        demonstrateSlidingWindow();
        
        // 演示高并发场景
        demonstrateHighConcurrency();
    }
    
    /**
     * 演示令牌桶算法
     */
    private static void demonstrateTokenBucket() throws InterruptedException {
        System.out.println("1. 令牌桶算法演示:");
        
        // 创建令牌桶限流器，每秒允许2个请求
        RateLimiter rateLimiter = RateLimiter.create(2.0);
        System.out.println("   创建令牌桶限流器，每秒允许2个请求");
        
        long start = System.currentTimeMillis();
        
        // 连续获取5次令牌
        for (int i = 1; i <= 5; i++) {
            double waitTime = rateLimiter.acquire();
            long elapsed = System.currentTimeMillis() - start;
            System.out.printf("   第%d次获取令牌，等待时间: %.3f秒，总耗时: %d毫秒%n", 
                            i, waitTime, elapsed);
        }
        
        System.out.println("   可以看到每次获取令牌大约间隔0.5秒，体现了限流效果\n");
    }
    
    /**
     * 演示滑动窗口算法
     */
    private static void demonstrateSlidingWindow() throws InterruptedException {
        System.out.println("2. 滑动窗口算法演示:");
        
        // 创建滑动窗口限流器，5秒内最多允许3个请求
        RateLimiter rateLimiter = RateLimiter.createSlidingWindow(3, 5000);
        System.out.println("   创建滑动窗口限流器，5秒内最多允许3个请求");
        
        long start = System.currentTimeMillis();
        
        // 连续获取5次令牌
        for (int i = 1; i <= 5; i++) {
            boolean acquired = rateLimiter.tryAcquire();
            long elapsed = System.currentTimeMillis() - start;
            System.out.printf("   第%d次尝试获取令牌，结果: %s，总耗时: %d毫秒%n", 
                            i, acquired ? "成功" : "失败", elapsed);
            
            // 等待1秒
            Thread.sleep(1000);
        }
        
        System.out.println("   可以看到前3次成功，后2次失败，体现了滑动窗口限流效果\n");
    }
    
    /**
     * 演示高并发场景
     */
    private static void demonstrateHighConcurrency() throws InterruptedException {
        System.out.println("3. 高并发场景演示:");
        
        // 创建令牌桶限流器，每秒允许5个请求
        RateLimiter rateLimiter = RateLimiter.create(5.0);
        System.out.println("   创建令牌桶限流器，每秒允许5个请求");
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        
        long start = System.currentTimeMillis();
        System.out.println("   启动20个并发请求...");
        
        // 提交20个并发请求
        for (int i = 0; i < 20; i++) {
            final int requestId = i + 1;
            executor.submit(() -> {
                requestCount.incrementAndGet();
                try {
                    // 尝试在1000毫秒内获取令牌
                    if (rateLimiter.tryAcquire(1, 1000, TimeUnit.MILLISECONDS)) {
                        successCount.incrementAndGet();
                        long elapsed = System.currentTimeMillis() - start;
                        System.out.printf("   请求%d获取令牌成功，时间: %d毫秒%n", requestId, elapsed);
                        
                        // 模拟业务处理时间
                        Thread.sleep(100);
                    } else {
                        failCount.incrementAndGet();
                        System.out.printf("   请求%d获取令牌超时失败%n", requestId);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    failCount.incrementAndGet();
                    System.out.printf("   请求%d被中断%n", requestId);
                }
            });
        }
        
        // 关闭线程池并等待完成
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        long end = System.currentTimeMillis();
        
        System.out.printf("\n   并发测试结果:%n");
        System.out.printf("   总请求数: %d%n", requestCount.get());
        System.out.printf("   成功请求数: %d%n", successCount.get());
        System.out.printf("   失败请求数: %d%n", failCount.get());
        System.out.printf("   总耗时: %d毫秒%n", end - start);
        System.out.printf("   平均每秒处理请求数: %.2f%n%n", successCount.get() * 1000.0 / (end - start));
    }
}