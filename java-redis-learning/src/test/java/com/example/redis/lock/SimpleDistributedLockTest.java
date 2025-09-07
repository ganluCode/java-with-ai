package com.example.redis.lock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 简化版分布式锁测试
 * 
 * 用于调试锁的基本行为
 */
@Disabled("需要Redis服务器运行在localhost:6379")
public class SimpleDistributedLockTest {

    @Test
    public void testBasicLockUnlock() throws InterruptedException {
        System.out.println("=== 开始执行基础锁测试 ===");
        final String lockKey = "simple:test:basic";
        
        // 创建两个锁实例（模拟两个不同的线程）
        RedisDistributedLock lock1 = new RedisDistributedLock(lockKey, true);
        RedisDistributedLock lock2 = new RedisDistributedLock(lockKey, true);
        
        try {
            // 第一个锁获取锁
            System.out.println("线程1尝试获取锁...");
            boolean acquired1 = lock1.tryLock(5, TimeUnit.SECONDS);
            System.out.println("线程1获取锁结果: " + acquired1);
            assertTrue(acquired1, "线程1应该能获取到锁");
            
            if (acquired1) {
                System.out.println("线程1获取锁成功，执行业务逻辑...");
                Thread.sleep(100); // 模拟业务处理
                System.out.println("线程1业务逻辑执行完成");
            }
            
            // 第二个锁尝试获取锁（应该失败，因为锁被占用）
            System.out.println("线程2尝试获取锁...");
            boolean acquired2 = lock2.tryLock(2, TimeUnit.SECONDS); // 短超时时间
            System.out.println("线程2获取锁结果: " + acquired2);
            
            // 释放第一个锁
            System.out.println("线程1释放锁...");
            lock1.unlock();
            System.out.println("线程1锁释放完成");
            
            // 第二个锁再次尝试获取锁（应该成功）
            if (!acquired2) {
                System.out.println("线程2再次尝试获取锁...");
                acquired2 = lock2.tryLock(5, TimeUnit.SECONDS);
                System.out.println("线程2再次获取锁结果: " + acquired2);
            }
            
            if (acquired2) {
                System.out.println("线程2获取锁成功，执行业务逻辑...");
                Thread.sleep(100); // 模拟业务处理
                System.out.println("线程2业务逻辑执行完成");
                lock2.unlock();
                System.out.println("线程2释放锁...");
            }
            
        } finally {
            // 确保锁被释放
            try {
                lock1.unlock();
            } catch (Exception e) {
                // 忽略
            }
            try {
                lock2.unlock();
            } catch (Exception e) {
                // 忽略
            }
        }
        
        System.out.println("=== 基础锁测试完成 ===");
    }

    @Test
    public void testSequentialExecution() throws InterruptedException {
        System.out.println("=== 开始执行顺序执行测试 ===");
        final String lockKey = "simple:test:sequential";
        final int threadCount = 3;
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final long[] executionOrder = new long[threadCount];
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 顺序执行测试
        System.out.println("顺序执行" + threadCount + "个任务...");
        for (int i = 0; i < threadCount; i++) {
            final int taskId = i;
            executor.submit(() -> {
                RedisDistributedLock lock = new RedisDistributedLock(lockKey, true);
                
                try {
                    System.out.println("任务" + taskId + "开始执行...");
                    
                    if (lock.tryLock(10, TimeUnit.SECONDS)) {
                        try {
                            executionOrder[taskId] = System.currentTimeMillis();
                            System.out.println("任务" + taskId + "获取锁成功，执行业务逻辑...");
                            Thread.sleep(50); // 模拟业务处理
                            System.out.println("任务" + taskId + "业务逻辑执行完成");
                        } finally {
                            lock.unlock();
                            System.out.println("任务" + taskId + "释放锁完成");
                        }
                    } else {
                        System.out.println("任务" + taskId + "获取锁失败");
                    }
                } catch (Exception e) {
                    System.out.println("任务" + taskId + "执行异常: " + e.getMessage());
                } finally {
                    finishLatch.countDown();
                    System.out.println("任务" + taskId + "完成");
                }
            });
            
            // 确保任务按顺序启动
            Thread.sleep(10);
        }
        
        // 等待所有任务完成
        finishLatch.await(60, TimeUnit.SECONDS);
        executor.shutdown();
        
        // 输出执行顺序
        System.out.println("执行顺序时间戳:");
        for (int i = 0; i < threadCount; i++) {
            System.out.println("  任务" + i + ": " + executionOrder[i]);
        }
        
        System.out.println("=== 顺序执行测试完成 ===");
    }
}