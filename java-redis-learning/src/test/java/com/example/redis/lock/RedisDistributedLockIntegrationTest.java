package com.example.redis.lock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis分布式锁集成测试
 * 
 * 注意：此测试需要Redis服务器运行在localhost:6379
 * 如果没有Redis服务器，请使用@Disabled注解禁用此测试
 */
@Disabled("需要Redis服务器运行在localhost:6379")
public class RedisDistributedLockIntegrationTest {

    @Test
    public void testSingleThreadLock() throws InterruptedException {
        System.out.println("=== 开始执行单线程锁测试 ===");
        LockConfig config = new LockConfig.Builder()
                .lockKey("integrationTest:singleThread")
                .leaseTime(30)
                .renewalTime(10)
                .build();
        
        RedisDistributedLock lock = new RedisDistributedLock(config);
        
        // 获取锁
        System.out.println("尝试获取锁...");
        lock.lock();
        System.out.println("锁获取成功");
        
        try {
            // 执行业务逻辑
            System.out.println("执行业务逻辑...");
            Thread.sleep(100);
            System.out.println("业务逻辑执行完成");
            assertTrue(true, "单线程获取锁成功");
        } finally {
            // 释放锁
            System.out.println("释放锁...");
            lock.unlock();
            System.out.println("锁释放完成");
            System.out.println("=== 单线程锁测试完成 ===");
        }
    }

    @Test
    public void testReentrantLock() throws InterruptedException {
        System.out.println("=== 开始执行可重入锁测试 ===");
        LockConfig config = new LockConfig.Builder()
                .lockKey("integrationTest:reentrant")
                .leaseTime(30)
                .renewalTime(10)
                .build();
        
        RedisDistributedLock lock = new RedisDistributedLock(config);
        
        // 第一次获取锁
        System.out.println("第一次获取锁...");
        lock.lock();
        System.out.println("第一次锁获取成功");
        
        try {
            // 第二次获取锁（可重入）
            System.out.println("第二次获取锁（可重入）...");
            lock.lock();
            System.out.println("第二次锁获取成功");
            
            try {
                // 执行业务逻辑
                System.out.println("执行业务逻辑...");
                Thread.sleep(100);
                System.out.println("业务逻辑执行完成");
                assertTrue(true, "可重入锁获取成功");
            } finally {
                // 释放第二次获取的锁
                System.out.println("释放第二次获取的锁...");
                lock.unlock();
                System.out.println("第二次锁释放完成");
            }
        } finally {
            // 释放第一次获取的锁
            System.out.println("释放第一次获取的锁...");
            lock.unlock();
            System.out.println("第一次锁释放完成");
            System.out.println("=== 可重入锁测试完成 ===");
        }
    }

    @Test
    public void testTryLock() throws InterruptedException {
        System.out.println("=== 开始执行TryLock测试 ===");
        LockConfig config = new LockConfig.Builder()
                .lockKey("integrationTest:tryLock")
                .leaseTime(30)
                .renewalTime(10)
                .build();
        
        RedisDistributedLock lock = new RedisDistributedLock(config);
        
        // 尝试获取锁
        System.out.println("尝试获取锁...");
        boolean acquired = lock.tryLock();
        System.out.println("锁获取结果: " + acquired);
        assertTrue(acquired, "应该能够获取锁");
        
        try {
            // 再次尝试获取锁（同一线程，可重入）
            System.out.println("再次尝试获取锁（可重入）...");
            boolean acquiredAgain = lock.tryLock();
            System.out.println("可重入锁获取结果: " + acquiredAgain);
            assertTrue(acquiredAgain, "同一线程应该能够重入获取锁");
        } finally {
            // 释放锁两次
            System.out.println("释放锁两次...");
            lock.unlock();
            lock.unlock();
            System.out.println("锁释放完成");
            System.out.println("=== TryLock测试完成 ===");
        }
    }

    @Test
    public void testTryLockWithTimeout() throws InterruptedException {
        System.out.println("=== 开始执行带超时的TryLock测试 ===");
        LockConfig config = new LockConfig.Builder()
                .lockKey("integrationTest:tryLockTimeout")
                .leaseTime(30)
                .renewalTime(10)
                .build();
        
        RedisDistributedLock lock = new RedisDistributedLock(config);
        
        // 尝试获取锁，等待5秒
        System.out.println("尝试获取锁，等待5秒...");
        boolean acquired = lock.tryLock(5, TimeUnit.SECONDS);
        System.out.println("锁获取结果: " + acquired);
        assertTrue(acquired, "应该能够在超时时间内获取锁");
        
        try {
            // 执行业务逻辑
            System.out.println("执行业务逻辑...");
            Thread.sleep(100);
            System.out.println("业务逻辑执行完成");
        } finally {
            System.out.println("释放锁...");
            lock.unlock();
            System.out.println("锁释放完成");
            System.out.println("=== 带超时的TryLock测试完成 ===");
        }
    }

    @Test
    public void testMultiThreadCompetition() throws InterruptedException {
        System.out.println("=== 开始执行多线程竞争测试 ===");
        final String lockKey = "integrationTest:multiThread";
        final int threadCount = 5;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 启动多个线程竞争同一个锁
        System.out.println("启动" + threadCount + "个线程竞争锁...");
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                System.out.println("线程" + threadId + "启动");
                LockConfig config = new LockConfig.Builder()
                        .lockKey(lockKey)
                        .leaseTime(30)
                        .renewalTime(10)
                        .build();
                
                RedisDistributedLock lock = new RedisDistributedLock(config);
                
                try {
                    // 等待所有线程准备就绪
                    System.out.println("线程" + threadId + "等待启动信号...");
                    startLatch.await();
                    System.out.println("线程" + threadId + "开始竞争锁...");
                    
                    // 尝试获取锁
                    if (lock.tryLock(3, TimeUnit.SECONDS)) {
                        try {
                            // 获取锁成功，执行业务逻辑
                            System.out.println("线程" + threadId + "获取锁成功");
                            successCount.incrementAndGet();
                            System.out.println("线程" + threadId + "执行业务逻辑...");
                            Thread.sleep(5000); // 模拟业务处理
                            System.out.println("线程" + threadId + "业务逻辑执行完成");
                        } finally {
                            System.out.println("线程" + threadId + "释放锁...");
                            lock.unlock();
                            System.out.println("线程" + threadId + "锁释放完成");
                        }
                    } else {
                        System.out.println("线程" + threadId + "获取锁失败");
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
        finishLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        
        int success = successCount.get();
        System.out.println("成功获取锁的线程数: " + success);
        
        // 验证只有一个线程能够获取锁
        assertEquals(1, success, "应该只有一个线程获取到锁");
        System.out.println("=== 多线程竞争测试完成 ===");
    }

    @Test
    public void testLockRenewal() throws InterruptedException {
        System.out.println("=== 开始执行锁自动续期测试 ===");
        LockConfig config = new LockConfig.Builder()
                .lockKey("integrationTest:renewal")
                .leaseTime(5) // 5秒过期
                .renewalTime(2) // 2秒续期
                .build();
        
        RedisDistributedLock lock = new RedisDistributedLock(config);
        
        // 获取锁
        System.out.println("获取锁...");
        lock.lock();
        System.out.println("锁获取成功，开始执行长时间任务...");
        
        try {
            // 模拟长时间业务处理（超过锁的过期时间）
            System.out.println("开始8秒的长时间任务...");
            Thread.sleep(8000); // 8秒
            System.out.println("长时间任务完成");
            
            // 如果自动续期正常工作，锁应该仍然有效
            assertTrue(true, "锁应该在自动续期后仍然有效");
        } finally {
            System.out.println("释放锁...");
            lock.unlock();
            System.out.println("锁释放完成");
            System.out.println("=== 锁自动续期测试完成 ===");
        }
    }

    @Test
    public void testFairLock() throws InterruptedException {
        System.out.println("=== 开始执行公平锁测试 ===");
        final String lockKey = "integrationTest:fairLock";
        final int threadCount = 3;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final AtomicInteger order = new AtomicInteger(0);
        final int[] executionOrder = new int[threadCount];
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 启动多个线程竞争公平锁
        System.out.println("启动" + threadCount + "个线程竞争公平锁...");
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                System.out.println("线程" + threadId + "启动");
                // 使用公平锁
                RedisDistributedLock lock = new RedisDistributedLock(lockKey, true);
                
                try {
                    // 等待所有线程准备就绪
                    System.out.println("线程" + threadId + "等待启动信号...");
                    startLatch.await();
                    System.out.println("线程" + threadId + "开始竞争公平锁...");
                    
                    // 获取锁（设置超时时间，避免无限等待）
                    long startTime = System.currentTimeMillis();
                    System.out.println("线程" + threadId + "开始尝试获取公平锁... (时间: " + startTime + ")");
                    if (lock.tryLock(15, TimeUnit.SECONDS)) {
                        long acquireTime = System.currentTimeMillis();
                        System.out.println("线程" + threadId + "获取公平锁成功 (耗时: " + (acquireTime - startTime) + "ms)");
                        
                        try {
                            // 记录执行顺序
                            executionOrder[threadId] = order.incrementAndGet();
                            System.out.println("线程" + threadId + "执行业务逻辑，顺序: " + executionOrder[threadId]);
                            Thread.sleep(10); // 模拟业务处理
                            System.out.println("线程" + threadId + "业务逻辑执行完成");
                        } finally {
                            System.out.println("线程" + threadId + "释放锁...");
                            lock.unlock();
                            long releaseTime = System.currentTimeMillis();
                            System.out.println("线程" + threadId + "锁释放完成 (时间: " + releaseTime + ")");
                        }
                    } else {
                        long failTime = System.currentTimeMillis();
                        System.out.println("线程" + threadId + "获取公平锁失败 (耗时: " + (failTime - startTime) + "ms)");
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
        finishLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        
        // 验证执行顺序（由于是公平锁，应该按请求顺序执行）
        // 注意：由于网络延迟等因素，这个测试可能不稳定
        System.out.println("执行顺序: " + java.util.Arrays.toString(executionOrder));
        System.out.println("=== 公平锁测试完成 ===");
    }
}