package com.example.redis.lock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 非公平锁竞争测试
 * 
 * 测试非公平锁在多线程竞争下的行为
 */
@Disabled("需要Redis服务器运行在localhost:6379")
public class NonFairLockCompetitionTest {

    @Test
    public void testNonFairLockCompetition() throws InterruptedException {
        System.out.println("=== 开始执行非公平锁竞争测试 ===");
        final String lockKey = "nonfair:test:competition";
        final int threadCount = 5;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final long[] executionTimes = new long[threadCount];
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 启动多个线程竞争同一个非公平锁
        System.out.println("启动" + threadCount + "个线程竞争非公平锁...");
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                System.out.println("线程" + threadId + "启动");
                
                // 创建非公平锁
                RedisDistributedLock lock = new RedisDistributedLock(lockKey, false); // false表示非公平锁
                
                try {
                    // 等待所有线程准备就绪
                    System.out.println("线程" + threadId + "等待启动信号...");
                    startLatch.await();
                    System.out.println("线程" + threadId + "开始竞争非公平锁...");
                    
                    // 尝试获取锁（设置超时时间）
                    long startTime = System.currentTimeMillis();
                    System.out.println("线程" + threadId + "开始尝试获取非公平锁... (时间: " + startTime + ")");
                    
                    if (lock.tryLock(10, TimeUnit.SECONDS)) {
                        long acquireTime = System.currentTimeMillis();
                        long waitTime = acquireTime - startTime;
                        executionTimes[threadId] = waitTime;
                        
                        System.out.println("线程" + threadId + "获取非公平锁成功 (等待时间: " + waitTime + "ms)");
                        
                        try {
                            // 获取锁成功，执行业务逻辑
                            successCount.incrementAndGet();
                            System.out.println("线程" + threadId + "执行业务逻辑...");
                            
                            // 模拟不同的业务处理时间
                            Thread.sleep(50 + threadId * 10); // 不同线程处理不同时间
                            
                            System.out.println("线程" + threadId + "业务逻辑执行完成");
                        } finally {
                            System.out.println("线程" + threadId + "释放锁...");
                            lock.unlock();
                            long releaseTime = System.currentTimeMillis();
                            System.out.println("线程" + threadId + "锁释放完成 (时间: " + releaseTime + ")");
                        }
                    } else {
                        long failTime = System.currentTimeMillis();
                        long waitTime = failTime - startTime;
                        System.out.println("线程" + threadId + "获取非公平锁失败 (等待时间: " + waitTime + "ms)");
                    }
                } catch (InterruptedException e) {
                    System.out.println("线程" + threadId + "被中断");
                    Thread.currentThread().interrupt();
                } finally {
                    System.out.println("线程" + threadId + "完成");
                    finishLatch.countDown();
                }
            });
        }
        
        // 启动所有线程
        System.out.println("发送启动信号...");
        startLatch.countDown();
        
        // 等待所有线程完成
        System.out.println("等待所有线程完成...");
        finishLatch.await(60, TimeUnit.SECONDS);
        executor.shutdown();
        
        // 输出统计信息
        int success = successCount.get();
        System.out.println("成功获取锁的线程数: " + success);
        System.out.println("各线程等待时间:");
        for (int i = 0; i < threadCount; i++) {
            System.out.println("  线程" + i + ": " + executionTimes[i] + "ms");
        }
        
        // 验证至少有一个线程获取到锁
        assertTrue(success > 0, "应该至少有一个线程获取到锁");
        System.out.println("=== 非公平锁竞争测试完成 ===");
    }

    @Test
    public void testNonFairLockVsFairLock() throws InterruptedException {
        System.out.println("=== 开始执行非公平锁 vs 公平锁对比测试 ===");
        
        // 测试非公平锁
        testLockBehavior("nonfair:comparison", false, "非公平锁");
        
        // 测试公平锁
        testLockBehavior("fair:comparison", true, "公平锁");
        
        System.out.println("=== 非公平锁 vs 公平锁对比测试完成 ===");
    }
    
    private void testLockBehavior(String lockKey, boolean fair, String lockType) throws InterruptedException {
        System.out.println("测试" + lockType + "行为:");
        final int threadCount = 3;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final long[] acquireTimes = new long[threadCount];
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        long testStartTime = System.currentTimeMillis();
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                RedisDistributedLock lock = new RedisDistributedLock(lockKey + ":" + System.currentTimeMillis(), fair);
                
                try {
                    startLatch.await();
                    
                    long requestTime = System.currentTimeMillis();
                    if (lock.tryLock(15, TimeUnit.SECONDS)) {
                        long acquireTime = System.currentTimeMillis();
                        acquireTimes[threadId] = acquireTime - testStartTime;
                        
                        try {
                            System.out.println(lockType + " 线程" + threadId + "获取锁成功 (相对时间: " + acquireTimes[threadId] + "ms)");
                            Thread.sleep(20);
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        acquireTimes[threadId] = -1; // 表示获取失败
                        System.out.println(lockType + " 线程" + threadId + "获取锁失败");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }
        
        startLatch.countDown();
        finishLatch.await(60, TimeUnit.SECONDS);
        executor.shutdown();
        
        System.out.println(lockType + "各线程获取锁时间:");
        for (int i = 0; i < threadCount; i++) {
            System.out.println("  线程" + i + ": " + (acquireTimes[i] == -1 ? "失败" : acquireTimes[i] + "ms"));
        }
    }
}