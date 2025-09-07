package cn.geekslife.learning.java.aqs.ratelimiter;

import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.TokenBucketAlgorithm;
import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.SlidingWindowAlgorithm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流器增强测试类
 */
public class EnhancedRateLimiterTest {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 限流器增强测试 ===\n");
        
        // 测试令牌桶算法的速率设置
        testTokenBucketRateSetting();
        
        // 测试滑动窗口算法的取消预留功能
        testSlidingWindowCancelReservation();
        
        // 测试高并发场景下的性能
        testHighConcurrencyPerformance();
    }
    
    /**
     * 测试令牌桶算法的速率设置
     */
    private static void testTokenBucketRateSetting() {
        System.out.println("1. 令牌桶算法速率设置测试:");
        
        RateLimiter rateLimiter = RateLimiter.create(1.0); // 每秒1个请求
        System.out.println("   初始速率: " + rateLimiter.getRate() + " 请求/秒");
        
        rateLimiter.setRate(2.0); // 设置为每秒2个请求
        System.out.println("   修改后速率: " + rateLimiter.getRate() + " 请求/秒");
        
        // 测试滑动窗口算法不支持设置速率
        try {
            RateLimiter slidingWindowLimiter = RateLimiter.createSlidingWindow(10, 1000);
            slidingWindowLimiter.setRate(2.0);
        } catch (UnsupportedOperationException e) {
            System.out.println("   滑动窗口算法正确抛出不支持设置速率异常: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * 测试滑动窗口算法的取消预留功能
     */
    private static void testSlidingWindowCancelReservation() throws InterruptedException {
        System.out.println("2. 滑动窗口算法取消预留测试:");
        
        // 创建滑动窗口限流器，5秒内最多允许3个请求
        RateLimiter rateLimiter = RateLimiter.createSlidingWindow(3, 5000);
        
        // 尝试获取令牌但不等待
        boolean acquired1 = rateLimiter.tryAcquire(1, 100, TimeUnit.MILLISECONDS);
        boolean acquired2 = rateLimiter.tryAcquire(1, 100, TimeUnit.MILLISECONDS);
        boolean acquired3 = rateLimiter.tryAcquire(1, 100, TimeUnit.MILLISECONDS);
        
        System.out.println("   前3次获取结果: " + acquired1 + ", " + acquired2 + ", " + acquired3);
        
        // 第4次应该失败
        boolean acquired4 = rateLimiter.tryAcquire(1, 100, TimeUnit.MILLISECONDS);
        System.out.println("   第4次获取结果: " + acquired4 + " (应该为false)");
        
        // 等待一段时间后再次尝试
        Thread.sleep(1000);
        boolean acquired5 = rateLimiter.tryAcquire(1, 100, TimeUnit.MILLISECONDS);
        System.out.println("   等待1秒后第5次获取结果: " + acquired5 + " (应该为true)");
        
        System.out.println();
    }
    
    /**
     * 测试高并发场景下的性能
     */
    private static void testHighConcurrencyPerformance() throws InterruptedException {
        System.out.println("3. 高并发性能测试:");
        
        // 测试令牌桶算法
        RateLimiter tokenBucketLimiter = RateLimiter.create(10.0); // 每秒10个请求
        runConcurrencyTest(tokenBucketLimiter, "令牌桶算法");
        
        // 测试滑动窗口算法
        RateLimiter slidingWindowLimiter = RateLimiter.createSlidingWindow(10, 1000); // 1秒内最多10个请求
        runConcurrencyTest(slidingWindowLimiter, "滑动窗口算法");
    }
    
    /**
     * 运行并发测试
     */
    private static void runConcurrencyTest(RateLimiter rateLimiter, String algorithmName) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(20);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        
        long start = System.currentTimeMillis();
        
        // 提交50个并发请求
        for (int i = 0; i < 50; i++) {
            final int requestId = i + 1;
            executor.submit(() -> {
                try {
                    // 尝试在100毫秒内获取令牌
                    if (rateLimiter.tryAcquire(1, 100, TimeUnit.MILLISECONDS)) {
                        successCount.incrementAndGet();
                    } else {
                        failCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                }
            });
        }
        
        // 关闭线程池并等待完成
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        long end = System.currentTimeMillis();
        
        System.out.println("   " + algorithmName + "测试结果:");
        System.out.println("     成功请求数: " + successCount.get());
        System.out.println("     失败请求数: " + failCount.get());
        System.out.println("     总耗时: " + (end - start) + "毫秒");
        System.out.println("     平均每秒处理请求数: " + (successCount.get() * 1000.0 / (end - start)));
    }
}