package cn.geekslife.learning.java.aqs.ratelimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 演示AQS在限流器中的应用
 */
public class AQSRateLimiterDemo {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== AQS限流器演示 ===\n");
        
        // 演示AQS基本概念
        demonstrateAQSConcept();
        
        // 演示限流器工作原理
        demonstrateRateLimiter();
        
        // 演示高并发场景
        demonstrateHighConcurrency();
    }
    
    /**
     * 演示AQS基本概念
     */
    private static void demonstrateAQSConcept() {
        System.out.println("1. AQS基本概念演示:");
        System.out.println("   AQS (AbstractQueuedSynchronizer) 是Java并发包的核心框架");
        System.out.println("   它提供了实现阻塞锁和相关同步器的基础框架");
        System.out.println("   AQS使用CLH队列管理等待线程，支持独占和共享模式");
        System.out.println("   在限流器中，我们使用共享模式来管理线程排队获取令牌\n");
    }
    
    /**
     * 演示限流器工作原理
     */
    private static void demonstrateRateLimiter() throws InterruptedException {
        System.out.println("2. 限流器工作原理演示:");
        
        // 创建每秒允许1个请求的限流器
        RateLimiter rateLimiter = RateLimiter.create(1.0);
        System.out.println("   创建限流器，每秒允许1个请求");
        
        long start = System.currentTimeMillis();
        
        // 连续获取5次令牌
        for (int i = 1; i <= 5; i++) {
            double waitTime = rateLimiter.acquire();
            long elapsed = System.currentTimeMillis() - start;
            System.out.printf("   第%d次获取令牌，等待时间: %.3f秒，总耗时: %d毫秒%n", 
                            i, waitTime, elapsed);
        }
        
        System.out.println("   可以看到每次获取令牌大约间隔1秒，体现了限流效果\n");
    }
    
    /**
     * 演示高并发场景
     */
    private static void demonstrateHighConcurrency() throws InterruptedException {
        System.out.println("3. 高并发场景演示:");
        
        // 创建每秒允许3个请求的限流器
        RateLimiter rateLimiter = RateLimiter.create(3.0);
        System.out.println("   创建限流器，每秒允许3个请求");
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        
        long start = System.currentTimeMillis();
        System.out.println("   启动15个并发请求...");
        
        // 提交15个并发请求
        for (int i = 0; i < 15; i++) {
            final int requestId = i + 1;
            executor.submit(() -> {
                requestCount.incrementAndGet();
                try {
                    // 尝试在500毫秒内获取令牌
                    if (rateLimiter.tryAcquire(1, 500, TimeUnit.MILLISECONDS)) {
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
        System.out.printf("   平均每秒处理请求数: %.2f%n", successCount.get() * 1000.0 / (end - start));
    }
}