package com.example.redis.lock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 分布式锁场景测试
 * 
 * 模拟实际业务场景中使用分布式锁的情况
 */
@Disabled("需要Redis服务器运行，且配置正确")
public class DistributedLockScenarioTest {

    /**
     * 场景1: 模拟电商秒杀场景
     * 多个用户同时抢购同一商品，使用分布式锁确保库存扣减的原子性
     */
    @Test
    public void testSeckillScenario() throws InterruptedException {
        System.out.println("=== 开始执行电商秒杀场景测试 ===");
        // 商品库存
        final AtomicInteger stock = new AtomicInteger(10);
        final String lockKey = "seckill:product:1001";
        final int threadCount = 20;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 模拟20个用户同时抢购
        System.out.println("启动" + threadCount + "个用户模拟抢购...");
        for (int i = 0; i < threadCount; i++) {
            final int userId = 10000 + i;
            executor.submit(() -> {
                System.out.println("用户" + userId + "准备抢购");
                LockConfig config = new LockConfig.Builder()
                        .lockKey(lockKey)
                        .leaseTime(30)
                        .renewalTime(10)
                        .build();
                
                RedisDistributedLock lock = new RedisDistributedLock(config);
                
                try {
                    // 等待所有线程准备就绪
                    System.out.println("用户" + userId + "等待抢购开始...");
                    startLatch.await();
                    
                    // 获取分布式锁
                    System.out.println("用户" + userId + "尝试获取锁...");
                    if (lock.tryLock(5, TimeUnit.SECONDS)) {
                        System.out.println("用户" + userId + "获取锁成功");
                        try {
                            // 检查库存
                            int currentStock = stock.get();
                            System.out.println("用户" + userId + "检查库存: " + currentStock);
                            if (currentStock > 0) {
                                // 扣减库存
                                int remaining = stock.decrementAndGet();
                                successCount.incrementAndGet();
                                System.out.println("用户" + userId + "抢购成功，剩余库存: " + remaining);
                            } else {
                                System.out.println("用户" + userId + "抢购失败，库存不足");
                            }
                            
                            // 模拟业务处理时间
                            System.out.println("用户" + userId + "处理订单中...");
                            Thread.sleep(100);
                            System.out.println("用户" + userId + "订单处理完成");
                        } finally {
                            System.out.println("用户" + userId + "释放锁");
                            lock.unlock();
                        }
                    } else {
                        System.out.println("用户" + userId + "获取锁失败");
                    }
                } catch (InterruptedException e) {
                    System.out.println("用户" + userId + "被中断");
                    Thread.currentThread().interrupt();
                } finally {
                    System.out.println("用户" + userId + "抢购结束");
                    finishLatch.countDown();
                }
            });
        }
        
        // 启动所有线程
        System.out.println("发送抢购开始信号...");
        startLatch.countDown();
        
        // 等待所有线程完成
        System.out.println("等待所有用户抢购完成...");
        finishLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        
        // 验证结果
        int finalStock = stock.get();
        int successfulUsers = successCount.get();
        
        System.out.println("最终库存: " + finalStock);
        System.out.println("成功抢购用户数: " + successfulUsers);
        
        // 应该只有10个用户抢购成功，库存应该为0
        assertEquals(0, finalStock, "库存应该被扣减到0");
        assertEquals(10, successfulUsers, "应该只有10个用户抢购成功");
        System.out.println("=== 电商秒杀场景测试完成 ===");
    }

    /**
     * 场景2: 模拟分布式任务调度
     * 多个服务实例同时启动，只有一个实例应该执行初始化任务
     */
    @Test
    public void testDistributedTaskScheduling() throws InterruptedException {
        System.out.println("=== 开始执行分布式任务调度场景测试 ===");
        final String lockKey = "scheduler:init:task";
        final int serviceInstanceCount = 5;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(serviceInstanceCount);
        final AtomicInteger executedInstanceCount = new AtomicInteger(0);
        final AtomicReference<String> executingInstance = new AtomicReference<>();
        
        ExecutorService executor = Executors.newFixedThreadPool(serviceInstanceCount);
        
        // 模拟5个服务实例同时启动
        System.out.println("启动" + serviceInstanceCount + "个服务实例...");
        for (int i = 1; i <= serviceInstanceCount; i++) {
            final int instanceId = i;
            executor.submit(() -> {
                System.out.println("服务实例" + instanceId + "启动");
                LockConfig config = new LockConfig.Builder()
                        .lockKey(lockKey)
                        .leaseTime(60)
                        .renewalTime(20)
                        .build();
                
                RedisDistributedLock lock = new RedisDistributedLock(config);
                
                try {
                    // 等待所有实例准备就绪
                    System.out.println("服务实例" + instanceId + "等待启动信号...");
                    startLatch.await();
                    
                    // 尝试获取锁执行初始化任务
                    System.out.println("服务实例" + instanceId + "尝试获取初始化任务锁...");
                    if (lock.tryLock(3, TimeUnit.SECONDS)) {
                        System.out.println("服务实例" + instanceId + "获取锁成功，开始执行初始化任务");
                        try {
                            // 只有一个实例应该执行到这里
                            executingInstance.set("Instance-" + instanceId);
                            executedInstanceCount.incrementAndGet();
                            
                            System.out.println("服务实例" + instanceId + "获得锁，开始执行初始化任务");
                            
                            // 模拟初始化任务执行
                            System.out.println("服务实例" + instanceId + "执行初始化任务中...");
                            Thread.sleep(2000);
                            
                            System.out.println("服务实例" + instanceId + "完成初始化任务");
                        } finally {
                            System.out.println("服务实例" + instanceId + "释放初始化任务锁");
                            lock.unlock();
                        }
                    } else {
                        System.out.println("服务实例" + instanceId + "未能获得锁，跳过初始化任务");
                    }
                } catch (InterruptedException e) {
                    System.out.println("服务实例" + instanceId + "被中断");
                    Thread.currentThread().interrupt();
                } finally {
                    System.out.println("服务实例" + instanceId + "完成");
                    finishLatch.countDown();
                }
            });
        }
        
        // 启动所有实例
        System.out.println("发送启动信号...");
        startLatch.countDown();
        
        // 等待所有实例完成
        System.out.println("等待所有服务实例完成...");
        finishLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        
        // 验证结果
        int executedCount = executedInstanceCount.get();
        String executorInstance = executingInstance.get();
        
        System.out.println("执行初始化任务的实例: " + executorInstance);
        System.out.println("执行初始化任务的实例数: " + executedCount);
        
        // 应该只有一个实例执行了初始化任务
        assertEquals(1, executedCount, "应该只有一个实例执行初始化任务");
        assertNotNull(executorInstance, "应该有实例执行了初始化任务");
        System.out.println("=== 分布式任务调度场景测试完成 ===");
    }

    /**
     * 场景3: 模拟分布式缓存更新
     * 多个服务实例同时需要更新缓存，使用分布式锁确保只有一个实例执行更新
     */
    @Test
    public void testDistributedCacheUpdate() throws InterruptedException {
        System.out.println("=== 开始执行分布式缓存更新场景测试 ===");
        final String lockKey = "cache:update:user:profile";
        final String cacheKey = "user:profile:1001";
        final int serviceInstanceCount = 3;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishLatch = new CountDownLatch(serviceInstanceCount);
        final AtomicInteger updateCount = new AtomicInteger(0);
        final AtomicReference<String> cacheValue = new AtomicReference<>("initial_value");
        
        ExecutorService executor = Executors.newFixedThreadPool(serviceInstanceCount);
        
        // 模拟3个服务实例同时检测到缓存需要更新
        System.out.println("启动" + serviceInstanceCount + "个服务实例检测缓存更新...");
        for (int i = 1; i <= serviceInstanceCount; i++) {
            final int instanceId = i;
            executor.submit(() -> {
                System.out.println("服务实例" + instanceId + "启动缓存更新检测");
                LockConfig config = new LockConfig.Builder()
                        .lockKey(lockKey)
                        .leaseTime(30)
                        .renewalTime(10)
                        .build();
                
                RedisDistributedLock lock = new RedisDistributedLock(config);
                
                try {
                    // 等待所有实例准备就绪
                    System.out.println("服务实例" + instanceId + "等待缓存更新检测信号...");
                    startLatch.await();
                    
                    // 尝试获取锁执行缓存更新
                    System.out.println("服务实例" + instanceId + "尝试获取缓存更新锁...");
                    if (lock.tryLock(2, TimeUnit.SECONDS)) {
                        System.out.println("服务实例" + instanceId + "获取缓存更新锁成功");
                        try {
                            // 检查是否需要更新缓存
                            String currentValue = cacheValue.get();
                            System.out.println("服务实例" + instanceId + "检查缓存值: " + currentValue);
                            if ("initial_value".equals(currentValue)) {
                                // 更新缓存
                                String newValue = "updated_value_" + System.currentTimeMillis();
                                cacheValue.set(newValue);
                                updateCount.incrementAndGet();
                                
                                System.out.println("服务实例" + instanceId + "更新缓存: " + newValue);
                                
                                // 模拟数据库查询和缓存更新时间
                                System.out.println("服务实例" + instanceId + "执行数据库查询和缓存更新...");
                                Thread.sleep(500);
                                System.out.println("服务实例" + instanceId + "缓存更新完成");
                            } else {
                                System.out.println("服务实例" + instanceId + "缓存已更新，无需重复更新");
                            }
                        } finally {
                            System.out.println("服务实例" + instanceId + "释放缓存更新锁");
                            lock.unlock();
                        }
                    } else {
                        System.out.println("服务实例" + instanceId + "未能获得锁，跳过缓存更新");
                    }
                } catch (InterruptedException e) {
                    System.out.println("服务实例" + instanceId + "被中断");
                    Thread.currentThread().interrupt();
                } finally {
                    System.out.println("服务实例" + instanceId + "缓存更新检测完成");
                    finishLatch.countDown();
                }
            });
        }
        
        // 启动所有实例
        System.out.println("发送缓存更新检测信号...");
        startLatch.countDown();
        
        // 等待所有实例完成
        System.out.println("等待所有服务实例完成缓存更新检测...");
        finishLatch.await(20, TimeUnit.SECONDS);
        executor.shutdown();
        
        // 验证结果
        int updatedCount = updateCount.get();
        String finalCacheValue = cacheValue.get();
        
        System.out.println("缓存最终值: " + finalCacheValue);
        System.out.println("执行缓存更新的实例数: " + updatedCount);
        
        // 应该只有一个实例执行了缓存更新
        assertEquals(1, updatedCount, "应该只有一个实例执行缓存更新");
        assertTrue(finalCacheValue.startsWith("updated_value_"), "缓存应该被更新");
        assertNotEquals("initial_value", finalCacheValue, "缓存值应该已更新");
        System.out.println("=== 分布式缓存更新场景测试完成 ===");
    }

    /**
     * 场景4: 测试可重入性
     * 在同一个线程中多次获取同一把锁
     */
    @Test
    public void testReentrantLock() throws InterruptedException {
        System.out.println("=== 开始执行可重入锁测试 ===");
        LockConfig config = new LockConfig.Builder()
                .lockKey("reentrant:test")
                .leaseTime(30)
                .renewalTime(10)
                .build();
        
        RedisDistributedLock lock = new RedisDistributedLock(config);
        
        // 第一次获取锁
        System.out.println("第一次尝试获取锁...");
        assertTrue(lock.tryLock(5, TimeUnit.SECONDS), "第一次应该能获取锁");
        System.out.println("第一次获取锁成功");
        
        try {
            // 第二次获取锁（可重入）
            System.out.println("第二次尝试获取锁（可重入）...");
            assertTrue(lock.tryLock(1, TimeUnit.SECONDS), "第二次应该能获取锁（可重入）");
            System.out.println("第二次获取锁成功");
            
            try {
                // 第三次获取锁（可重入）
                System.out.println("第三次尝试获取锁（可重入）...");
                assertTrue(lock.tryLock(1, TimeUnit.SECONDS), "第三次应该能获取锁（可重入）");
                System.out.println("第三次获取锁成功");
                
                // 执行业务逻辑
                System.out.println("执行业务逻辑...");
                Thread.sleep(100);
                System.out.println("业务逻辑执行完成");
                
                // 释放第三次获取的锁
                System.out.println("释放第三次获取的锁...");
                lock.unlock();
                System.out.println("第三次锁释放完成");
                
                // 释放第二次获取的锁
                System.out.println("释放第二次获取的锁...");
                lock.unlock();
                System.out.println("第二次锁释放完成");
                
                // 仍然持有锁，可以继续执行
                // 执行更多业务逻辑
                System.out.println("继续执行更多业务逻辑...");
                Thread.sleep(100);
                System.out.println("更多业务逻辑执行完成");
                
            } finally {
                // 释放第一次获取的锁
                System.out.println("释放第一次获取的锁...");
                lock.unlock();
                System.out.println("第一次锁释放完成");
            }
            
            // 锁已完全释放，其他线程可以获取
            System.out.println("尝试重新获取锁...");
            assertTrue(lock.tryLock(1, TimeUnit.SECONDS), "锁释放后应该能重新获取");
            System.out.println("重新获取锁成功");
            lock.unlock();
            System.out.println("重新获取的锁已释放");
            
        } finally {
            // 确保锁被释放
            try {
                System.out.println("确保锁被完全释放...");
                lock.unlock();
            } catch (IllegalMonitorStateException e) {
                // 忽略，锁已经被释放
                System.out.println("锁已被完全释放");
            }
        }
        
        System.out.println("可重入锁测试通过");
        System.out.println("=== 可重入锁测试完成 ===");
    }

    /**
     * 场景5: 测试自动续期功能
     * 模拟长时间运行的任务，验证锁的自动续期功能
     */
    @Test
    public void testAutoRenewal() throws InterruptedException {
        System.out.println("=== 开始执行自动续期功能测试 ===");
        LockConfig config = new LockConfig.Builder()
                .lockKey("renewal:test")
                .leaseTime(5) // 5秒过期
                .renewalTime(2) // 2秒续期
                .build();
        
        RedisDistributedLock lock = new RedisDistributedLock(config);
        
        // 获取锁
        System.out.println("尝试获取锁...");
        assertTrue(lock.tryLock(5, TimeUnit.SECONDS), "应该能获取锁");
        System.out.println("锁获取成功");
        
        try {
            // 模拟长时间运行的任务（超过锁的过期时间）
            System.out.println("开始执行8秒的长时间任务（超过5秒的过期时间）...");
            Thread.sleep(8000); // 8秒，超过5秒的过期时间
            System.out.println("长时间任务执行完成");
            
            // 如果自动续期正常工作，锁应该仍然有效
            // 尝试再次获取锁应该失败，因为当前线程仍然持有锁
            System.out.println("验证锁是否仍然有效...");
            RedisDistributedLock anotherLock = new RedisDistributedLock(config);
            boolean anotherLockAcquired = anotherLock.tryLock(1, TimeUnit.SECONDS);
            System.out.println("另一个锁获取结果: " + anotherLockAcquired);
            assertFalse(anotherLockAcquired, "其他线程不应该能获取到锁");
            
        } finally {
            System.out.println("释放锁...");
            lock.unlock();
            System.out.println("锁释放完成");
        }
        
        System.out.println("自动续期测试通过");
        System.out.println("=== 自动续期功能测试完成 ===");
    }
}