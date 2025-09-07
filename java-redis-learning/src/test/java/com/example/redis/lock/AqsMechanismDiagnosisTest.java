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
 * AQS工作机制诊断测试
 * 
 * 用于诊断AQS在分布式锁中的工作机制
 */
@Disabled("需要Redis服务器运行在localhost:6379")
public class AqsMechanismDiagnosisTest {

    @Test
    public void testAqsWaitingMechanism() throws InterruptedException {
        System.out.println("=== 开始执行AQS等待机制诊断测试 ===");
        final String lockKey = "diagnosis:test:aqs";
        final int threadCount = 3;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        System.out.println("启动" + threadCount + "个线程进行诊断测试...");
        
        // 第一个线程 - 获取锁并保持一段时间
        executor.submit(() -> {
            System.out.println("线程0启动");
            RedisDistributedLock lock = new RedisDistributedLock(lockKey, true);
            
            try {
                System.out.println("线程0等待启动信号...");
                startLatch.await();
                System.out.println("线程0开始竞争锁...");
                
                // 获取锁
                System.out.println("线程0开始尝试获取锁...");
                if (lock.tryLock(5, TimeUnit.SECONDS)) {
                    System.out.println("线程0获取锁成功");
                    successCount.incrementAndGet();
                    
                    try {
                        System.out.println("线程0执行业务逻辑...");
                        Thread.sleep(2000); // 保持锁2秒
                        System.out.println("线程0业务逻辑执行完成");
                    } finally {
                        System.out.println("线程0释放锁...");
                        lock.unlock();
                        System.out.println("线程0锁释放完成");
                    }
                } else {
                    System.out.println("线程0获取锁失败");
                }
            } catch (InterruptedException e) {
                System.out.println("线程0被中断");
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("线程0完成");
                finishLatch.countDown();
            }
        });
        
        // 第二个线程 - 等待第一个线程释放锁
        executor.submit(() -> {
            System.out.println("线程1启动");
            RedisDistributedLock lock = new RedisDistributedLock(lockKey, true);
            
            try {
                System.out.println("线程1等待启动信号...");
                startLatch.await();
                System.out.println("线程1开始竞争锁...");
                
                // 等待一小段时间，确保第一个线程已经获取锁
                Thread.sleep(100);
                
                // 尝试获取锁
                System.out.println("线程1开始尝试获取锁...");
                long startTime = System.currentTimeMillis();
                if (lock.tryLock(10, TimeUnit.SECONDS)) {
                    long endTime = System.currentTimeMillis();
                    System.out.println("线程1获取锁成功 (等待时间: " + (endTime - startTime) + "ms)");
                    successCount.incrementAndGet();
                    
                    try {
                        System.out.println("线程1执行业务逻辑...");
                        Thread.sleep(1000); // 保持锁1秒
                        System.out.println("线程1业务逻辑执行完成");
                    } finally {
                        System.out.println("线程1释放锁...");
                        lock.unlock();
                        System.out.println("线程1锁释放完成");
                    }
                } else {
                    long endTime = System.currentTimeMillis();
                    System.out.println("线程1获取锁失败 (等待时间: " + (endTime - startTime) + "ms)");
                }
            } catch (InterruptedException e) {
                System.out.println("线程1被中断");
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("线程1完成");
                finishLatch.countDown();
            }
        });
        
        // 第三个线程 - 等待第二个线程释放锁
        executor.submit(() -> {
            System.out.println("线程2启动");
            RedisDistributedLock lock = new RedisDistributedLock(lockKey, true);
            
            try {
                System.out.println("线程2等待启动信号...");
                startLatch.await();
                System.out.println("线程2开始竞争锁...");
                
                // 等待一段时间，确保前两个线程已经开始执行
                Thread.sleep(200);
                
                // 尝试获取锁
                System.out.println("线程2开始尝试获取锁...");
                long startTime = System.currentTimeMillis();
                if (lock.tryLock(10, TimeUnit.SECONDS)) {
                    long endTime = System.currentTimeMillis();
                    System.out.println("线程2获取锁成功 (等待时间: " + (endTime - startTime) + "ms)");
                    successCount.incrementAndGet();
                    
                    try {
                        System.out.println("线程2执行业务逻辑...");
                        Thread.sleep(500); // 保持锁0.5秒
                        System.out.println("线程2业务逻辑执行完成");
                    } finally {
                        System.out.println("线程2释放锁...");
                        lock.unlock();
                        System.out.println("线程2锁释放完成");
                    }
                } else {
                    long endTime = System.currentTimeMillis();
                    System.out.println("线程2获取锁失败 (等待时间: " + (endTime - startTime) + "ms)");
                }
            } catch (InterruptedException e) {
                System.out.println("线程2被中断");
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("线程2完成");
                finishLatch.countDown();
            }
        });
        
        // 启动所有线程
        System.out.println("发送启动信号...");
        startLatch.countDown();
        
        // 等待所有线程完成
        System.out.println("等待所有线程完成...");
        finishLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        
        int success = successCount.get();
        System.out.println("成功获取锁的线程数: " + success);
        System.out.println("=== AQS等待机制诊断测试完成 ===");
    }

    @Test
    public void testMultipleThreadsCompeting() throws InterruptedException {
        System.out.println("=== 开始执行多线程竞争诊断测试 ===");
        final String lockKey = "diagnosis:test:competition";
        final int threadCount = 5;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final long[] acquisitionTimes = new long[threadCount];
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        System.out.println("启动" + threadCount + "个线程同时竞争锁...");
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                System.out.println("线程" + threadId + "启动");
                RedisDistributedLock lock = new RedisDistributedLock(lockKey, true);
                
                try {
                    System.out.println("线程" + threadId + "等待启动信号...");
                    startLatch.await();
                    System.out.println("线程" + threadId + "开始竞争锁...");
                    
                    // 所有线程几乎同时开始尝试获取锁
                    long startTime = System.currentTimeMillis();
                    System.out.println("线程" + threadId + "开始尝试获取锁... (时间: " + startTime + ")");
                    
                    if (lock.tryLock(15, TimeUnit.SECONDS)) {
                        long acquireTime = System.currentTimeMillis();
                        acquisitionTimes[threadId] = acquireTime - startTime;
                        System.out.println("线程" + threadId + "获取锁成功 (等待时间: " + acquisitionTimes[threadId] + "ms)");
                        successCount.incrementAndGet();
                        
                        try {
                            System.out.println("线程" + threadId + "执行业务逻辑...");
                            Thread.sleep(500); // 每个线程保持锁0.5秒
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
        
        int success = successCount.get();
        System.out.println("成功获取锁的线程数: " + success);
        System.out.println("各线程获取锁的等待时间:");
        for (int i = 0; i < threadCount; i++) {
            System.out.println("  线程" + i + ": " + acquisitionTimes[i] + "ms");
        }
        System.out.println("=== 多线程竞争诊断测试完成 ===");
    }
}