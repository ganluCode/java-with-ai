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
 * Redis分布式锁发布/订阅机制测试
 * 
 * 测试基于Redis发布/订阅机制的锁释放通知功能
 */
@Disabled("需要Redis服务器运行在localhost:6379")
public class RedisDistributedLockPubSubTest {

    @Test
    public void testBasicPubSubLockNotification() throws InterruptedException {
        System.out.println("=== 开始执行基础发布/订阅锁通知测试 ===");
        final String lockKey = "pubsub:test:basic";
        final int threadCount = 3;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final long[] acquisitionTimes = new long[threadCount];
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 启动多个线程竞争同一个锁
        System.out.println("启动" + threadCount + "个线程竞争锁...");
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                System.out.println("线程" + threadId + "启动");
                
                // 创建启用发布/订阅机制的锁配置
                LockConfig config = new LockConfig.Builder()
                        .lockKey(lockKey)
                        .leaseTime(30)
                        .renewalTime(10)
                        .enablePubSub(true) // 启用发布/订阅机制
                        .build();
                
                RedisDistributedLock lock = new RedisDistributedLock(config);
                
                try {
                    // 等待所有线程准备就绪
                    System.out.println("线程" + threadId + "等待启动信号...");
                    startLatch.await();
                    
                    // 尝试获取锁，设置较短的超时时间
                    long startTime = System.currentTimeMillis();
                    System.out.println("线程" + threadId + "开始尝试获取锁... (时间: " + startTime + ")");
                    
                    if (lock.tryLock(15, TimeUnit.SECONDS)) {
                        long acquireTime = System.currentTimeMillis();
                        acquisitionTimes[threadId] = acquireTime - startTime;
                        
                        System.out.println("线程" + threadId + "获取锁成功 (等待时间: " + acquisitionTimes[threadId] + "ms)");
                        successCount.incrementAndGet();
                        
                        try {
                            // 执行业务逻辑
                            System.out.println("线程" + threadId + "执行业务逻辑...");
                            Thread.sleep(1000); // 模拟业务处理1秒
                            System.out.println("线程" + threadId + "业务逻辑执行完成");
                        } finally {
                            System.out.println("线程" + threadId + "释放锁...");
                            lock.unlock();
                            System.out.println("线程" + threadId + "锁释放完成");
                        }
                    } else {
                        long failTime = System.currentTimeMillis();
                        acquisitionTimes[threadId] = failTime - startTime;
                        System.out.println("线程" + threadId + "获取锁失败 (等待时间: " + acquisitionTimes[threadId] + "ms)");
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
        
        // 验证结果
        int success = successCount.get();
        System.out.println("成功获取锁的线程数: " + success);
        System.out.println("各线程获取锁的等待时间:");
        for (int i = 0; i < threadCount; i++) {
            System.out.println("  线程" + i + ": " + acquisitionTimes[i] + "ms");
        }
        
        // 应该所有线程都能获取到锁
        assertEquals(threadCount, success, "所有线程都应该能获取到锁");
        
        System.out.println("=== 基础发布/订阅锁通知测试完成 ===");
    }

    @Test
    public void testPubSubVsTraditionalLock() throws InterruptedException {
        System.out.println("=== 开始执行发布/订阅机制 vs 传统机制对比测试 ===");
        
        // 测试启用发布/订阅机制的锁
        testLockWithPubSubEnabled();
        
        // 测试禁用发布/订阅机制的锁
        testLockWithPubSubDisabled();
        
        System.out.println("=== 发布/订阅机制 vs 传统机制对比测试完成 ===");
    }
    
    private void testLockWithPubSubEnabled() throws InterruptedException {
        System.out.println("--- 测试启用发布/订阅机制 ---");
        final String lockKey = "pubsub:test:enabled";
        final int threadCount = 2;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final long[] acquisitionTimes = new long[threadCount];
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 第一个线程 - 获取锁并保持一段时间
        executor.submit(() -> {
            LockConfig config = new LockConfig.Builder()
                    .lockKey(lockKey)
                    .leaseTime(30)
                    .renewalTime(10)
                    .enablePubSub(true) // 启用发布/订阅机制
                    .build();
            
            RedisDistributedLock lock = new RedisDistributedLock(config);
            
            try {
                startLatch.await();
                
                if (lock.tryLock(5, TimeUnit.SECONDS)) {
                    successCount.incrementAndGet();
                    System.out.println("启用PubSub线程0获取锁成功");
                    Thread.sleep(2000); // 保持锁2秒
                    lock.unlock();
                    System.out.println("启用PubSub线程0释放锁完成");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                finishLatch.countDown();
            }
        });
        
        // 第二个线程 - 等待第一个线程释放锁
        executor.submit(() -> {
            LockConfig config = new LockConfig.Builder()
                    .lockKey(lockKey)
                    .leaseTime(30)
                    .renewalTime(10)
                    .enablePubSub(true) // 启用发布/订阅机制
                    .build();
            
            RedisDistributedLock lock = new RedisDistributedLock(config);
            
            try {
                startLatch.await();
                Thread.sleep(100); // 确保第一个线程先获取锁
                
                long startTime = System.currentTimeMillis();
                if (lock.tryLock(10, TimeUnit.SECONDS)) {
                    long acquireTime = System.currentTimeMillis();
                    acquisitionTimes[1] = acquireTime - startTime;
                    successCount.incrementAndGet();
                    System.out.println("启用PubSub线程1获取锁成功 (等待时间: " + acquisitionTimes[1] + "ms)");
                    lock.unlock();
                    System.out.println("启用PubSub线程1释放锁完成");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                finishLatch.countDown();
            }
        });
        
        startLatch.countDown();
        finishLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        
        System.out.println("启用发布/订阅机制 - 等待时间: " + acquisitionTimes[1] + "ms");
    }
    
    private void testLockWithPubSubDisabled() throws InterruptedException {
        System.out.println("--- 测试禁用发布/订阅机制 ---");
        final String lockKey = "pubsub:test:disabled";
        final int threadCount = 2;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final long[] acquisitionTimes = new long[threadCount];
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 第一个线程 - 获取锁并保持一段时间
        executor.submit(() -> {
            LockConfig config = new LockConfig.Builder()
                    .lockKey(lockKey)
                    .leaseTime(30)
                    .renewalTime(10)
                    .enablePubSub(false) // 禁用发布/订阅机制
                    .build();
            
            RedisDistributedLock lock = new RedisDistributedLock(config);
            
            try {
                startLatch.await();
                
                if (lock.tryLock(5, TimeUnit.SECONDS)) {
                    successCount.incrementAndGet();
                    System.out.println("禁用PubSub线程0获取锁成功");
                    Thread.sleep(2000); // 保持锁2秒
                    lock.unlock();
                    System.out.println("禁用PubSub线程0释放锁完成");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                finishLatch.countDown();
            }
        });
        
        // 第二个线程 - 等待第一个线程释放锁
        executor.submit(() -> {
            LockConfig config = new LockConfig.Builder()
                    .lockKey(lockKey)
                    .leaseTime(30)
                    .renewalTime(10)
                    .enablePubSub(false) // 禁用发布/订阅机制
                    .build();
            
            RedisDistributedLock lock = new RedisDistributedLock(config);
            
            try {
                startLatch.await();
                Thread.sleep(100); // 确保第一个线程先获取锁
                
                long startTime = System.currentTimeMillis();
                if (lock.tryLock(10, TimeUnit.SECONDS)) {
                    long acquireTime = System.currentTimeMillis();
                    acquisitionTimes[1] = acquireTime - startTime;
                    successCount.incrementAndGet();
                    System.out.println("禁用PubSub线程1获取锁成功 (等待时间: " + acquisitionTimes[1] + "ms)");
                    lock.unlock();
                    System.out.println("禁用PubSub线程1释放锁完成");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                finishLatch.countDown();
            }
        });
        
        startLatch.countDown();
        finishLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        
        System.out.println("禁用发布/订阅机制 - 等待时间: " + acquisitionTimes[1] + "ms");
    }

    @Test
    public void testHighConcurrencyWithPubSub() throws InterruptedException {
        System.out.println("=== 开始执行高并发发布/订阅机制测试 ===");
        final String lockKey = "pubsub:test:highconcurrency";
        final int threadCount = 10;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final long[] acquisitionTimes = new long[threadCount];
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 启动多个线程竞争同一个锁
        System.out.println("启动" + threadCount + "个线程进行高并发测试...");
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                // 创建启用发布/订阅机制的锁配置
                LockConfig config = new LockConfig.Builder()
                        .lockKey(lockKey)
                        .leaseTime(30)
                        .renewalTime(10)
                        .enablePubSub(true) // 启用发布/订阅机制
                        .build();
                
                RedisDistributedLock lock = new RedisDistributedLock(config);
                
                try {
                    startLatch.await();
                    
                    // 尝试获取锁
                    long startTime = System.currentTimeMillis();
                    if (lock.tryLock(20, TimeUnit.SECONDS)) {
                        long acquireTime = System.currentTimeMillis();
                        acquisitionTimes[threadId] = acquireTime - startTime;
                        
                        successCount.incrementAndGet();
                        System.out.println("线程" + threadId + "获取锁成功 (等待时间: " + acquisitionTimes[threadId] + "ms)");
                        
                        try {
                            // 执行业务逻辑
                            Thread.sleep(500); // 模拟业务处理0.5秒
                        } finally {
                            lock.unlock();
                            System.out.println("线程" + threadId + "释放锁完成");
                        }
                    } else {
                        long failTime = System.currentTimeMillis();
                        acquisitionTimes[threadId] = failTime - startTime;
                        System.out.println("线程" + threadId + "获取锁失败 (等待时间: " + acquisitionTimes[threadId] + "ms)");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }
        
        // 启动所有线程
        startLatch.countDown();
        
        // 等待所有线程完成
        finishLatch.await(120, TimeUnit.SECONDS);
        executor.shutdown();
        
        // 验证结果
        int success = successCount.get();
        System.out.println("高并发测试结果:");
        System.out.println("  成功获取锁的线程数: " + success + "/" + threadCount);
        System.out.println("  各线程获取锁的等待时间:");
        for (int i = 0; i < threadCount; i++) {
            System.out.println("    线程" + i + ": " + acquisitionTimes[i] + "ms");
        }
        
        // 应该所有线程都能获取到锁
        assertEquals(threadCount, success, "所有线程都应该能获取到锁");
        
        System.out.println("=== 高并发发布/订阅机制测试完成 ===");
    }
}