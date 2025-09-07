package com.example.redis.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁使用示例
 * 
 * 演示如何在实际项目中使用Redis分布式锁
 */
public class DistributedLockExample {
    
    public static void main(String[] args) {
        // 示例1: 基本使用
        basicUsage();
        
        // 示例2: 带配置的使用
        configuredUsage();
        
        // 示例3: 可重入锁使用
        reentrantUsage();
    }
    
    /**
     * 基本使用示例
     */
    public static void basicUsage() {
        System.out.println("=== 基本使用示例 ===");
        
        // 创建分布式锁实例
        RedisDistributedLock lock = new RedisDistributedLock("order:process:12345");
        
        try {
            // 获取锁
            lock.lock();
            System.out.println("获取锁成功，开始处理订单...");
            
            // 执行业务逻辑
            processOrder();
            
        } finally {
            // 释放锁
            lock.unlock();
            System.out.println("订单处理完成，释放锁");
        }
    }
    
    /**
     * 带配置的使用示例
     */
    public static void configuredUsage() {
        System.out.println("\n=== 带配置的使用示例 ===");
        
        // 创建锁配置
        LockConfig config = new LockConfig.Builder()
                .lockKey("user:profile:update:1001")
                .leaseTime(30)  // 锁过期时间30秒
                .renewalTime(10) // 自动续期时间10秒
                .fair(true)      // 使用公平锁
                .build();
        
        // 创建分布式锁实例
        RedisDistributedLock lock = new RedisDistributedLock(config);
        
        try {
            // 尝试在5秒内获取锁
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                System.out.println("获取锁成功，开始更新用户资料...");
                
                // 执行业务逻辑
                updateUserProfile();
                
            } else {
                System.out.println("获取锁失败，超时退出");
            }
        } catch (InterruptedException e) {
            System.err.println("获取锁过程中被中断: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            // 释放锁
            try {
                lock.unlock();
                System.out.println("用户资料更新完成，释放锁");
            } catch (IllegalMonitorStateException e) {
                System.out.println("锁已释放或未持有锁");
            }
        }
    }
    
    /**
     * 可重入锁使用示例
     */
    public static void reentrantUsage() {
        System.out.println("\n=== 可重入锁使用示例 ===");
        
        RedisDistributedLock lock = new RedisDistributedLock("reentrant:example");
        
        try {
            // 第一次获取锁
            lock.lock();
            System.out.println("第一次获取锁成功");
            
            // 在同一线程中再次获取锁（可重入）
            lock.lock();
            System.out.println("第二次获取锁成功（可重入）");
            
            // 执行业务逻辑
            performNestedOperations();
            
            // 释放第二次获取的锁
            lock.unlock();
            System.out.println("释放第二次获取的锁");
            
            // 释放第一次获取的锁
            lock.unlock();
            System.out.println("释放第一次获取的锁");
            
        } finally {
            // 确保锁被完全释放
            try {
                lock.unlock();
            } catch (IllegalMonitorStateException e) {
                // 锁已完全释放
            }
        }
    }
    
    /**
     * 模拟订单处理
     */
    private static void processOrder() {
        try {
            System.out.println("1. 验证订单信息...");
            Thread.sleep(500);
            
            System.out.println("2. 检查库存...");
            Thread.sleep(500);
            
            System.out.println("3. 扣减库存...");
            Thread.sleep(500);
            
            System.out.println("4. 更新订单状态...");
            Thread.sleep(500);
            
            System.out.println("5. 发送确认消息...");
            Thread.sleep(500);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 模拟用户资料更新
     */
    private static void updateUserProfile() {
        try {
            System.out.println("1. 读取用户当前资料...");
            Thread.sleep(300);
            
            System.out.println("2. 验证更新数据...");
            Thread.sleep(300);
            
            System.out.println("3. 更新数据库...");
            Thread.sleep(500);
            
            System.out.println("4. 清除缓存...");
            Thread.sleep(200);
            
            System.out.println("5. 记录操作日志...");
            Thread.sleep(200);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 模拟嵌套操作
     */
    private static void performNestedOperations() {
        try {
            System.out.println("执行外层操作...");
            Thread.sleep(300);
            
            // 嵌套调用需要获取锁的方法
            nestedOperation();
            
            System.out.println("外层操作完成");
            Thread.sleep(200);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 模拟嵌套操作
     */
    private static void nestedOperation() {
        RedisDistributedLock lock = new RedisDistributedLock("reentrant:example");
        
        // 由于是同一线程，这里会成功获取可重入锁
        lock.lock();
        try {
            System.out.println("执行内层操作...");
            Thread.sleep(300);
            System.out.println("内层操作完成");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}