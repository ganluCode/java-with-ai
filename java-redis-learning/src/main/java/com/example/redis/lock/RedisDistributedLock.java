package com.example.redis.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import io.lettuce.core.pubsub.RedisPubSubAdapter;

/**
 * 基于Redis和AQS实现的分布式锁
 * 
 * 特性：
 * 1. 可重入性：同一线程可以多次获取锁
 * 2. 自动续期：防止业务执行时间过长导致锁失效
 * 3. 公平性：支持公平锁和非公平锁
 * 4. 高可用：基于Redis的分布式特性
 * 5. 实时通知：基于Redis发布/订阅机制的锁释放通知
 * 6. Watchdog监控：提供全面的监控和自动恢复机制
 */
public class RedisDistributedLock extends AbstractQueuedSynchronizer implements java.util.concurrent.locks.Lock {
    
    private final String lockKey;
    private final LockConfig config;
    private final LettuceClient lettuceClient;
    private final RenewalManager renewalManager;
    private final LockManager lockManager;
    private final Watchdog watchdog; // 添加Watchdog监控组件
    private final ThreadLocal<String> lockValueThreadLocal = new ThreadLocal<>();
    private final String releaseNotificationChannel;
    private RedisPubSubAdapter<String, String> subscriptionListener;
    private final AtomicBoolean lockReleasedNotification = new AtomicBoolean(false);
    private volatile Thread waitingThread;
    
    /**
     * 构造函数（非公平锁）
     * @param lockKey 锁的键名
     */
    public RedisDistributedLock(String lockKey) {
        this(lockKey, false);
    }
    
    /**
     * 构造函数
     * @param lockKey 锁的键名
     * @param fair 是否公平锁
     */
    public RedisDistributedLock(String lockKey, boolean fair) {
        this(new LockConfig.Builder().lockKey(lockKey).fair(fair).build());
    }
    
    /**
     * 构造函数
     * @param config 锁配置
     */
    public RedisDistributedLock(LockConfig config) {
        this.lockKey = config.getLockKey();
        this.config = config;
        this.lettuceClient = new LettuceClient();
        this.renewalManager = new RenewalManager();
        this.lockManager = new LockManager(lettuceClient);
        this.watchdog = new Watchdog(); // 初始化Watchdog
        this.releaseNotificationChannel = "lock:release:" + lockKey;
        
        // 初始化订阅监听器
        initSubscriptionListener();
        
        // 如果启用了发布/订阅机制，订阅锁释放通知
        if (config.isEnablePubSub()) {
            System.out.println("启用发布/订阅机制，订阅锁释放通知频道: " + releaseNotificationChannel);
            lockManager.subscribeToLockRelease(lockKey, subscriptionListener);
        }
    }
    
    /**
     * 初始化订阅监听器
     */
    private void initSubscriptionListener() {
        this.subscriptionListener = new RedisPubSubAdapter<String, String>() {
            @Override
            public void message(String channel, String message) {
                if (releaseNotificationChannel.equals(channel)) {
                    System.out.println("收到锁释放通知: " + message + " (线程: " + Thread.currentThread().getId() + ")");
                    // 锁释放通知到达，设置标志
                    lockReleasedNotification.set(true);
                    // 唤醒等待的线程
                    Thread waiting = waitingThread;
                    if (waiting != null) {
                        LockSupport.unpark(waiting);
                    }
                }
            }
        };
    }
    
    @Override
    protected boolean tryAcquire(int acquires) {
        // 获取当前线程
        Thread current = Thread.currentThread();
        
        // 获取当前锁的状态
        int c = getState();
        
        // 获取锁的持有线程
        Thread owner = getExclusiveOwnerThread();
        
        // 如果当前线程就是锁的持有线程，说明是重入
        if (owner == current) {
            // 增加重入次数
            int nextc = c + acquires;
            if (nextc < 0) {
                throw new Error("Maximum lock count exceeded");
            }
            setState(nextc);
            return true;
        }
        
        // 对于分布式锁，公平性主要通过Redis保证
        // AQS的hasQueuedPredecessors主要用于本地线程排队
        // 在分布式环境下，我们让所有线程都有机会尝试获取Redis锁
        
        // 生成唯一的锁值
        String lockValue = lockValueThreadLocal.get();
        if (lockValue == null) {
            lockValue = UUID.randomUUID().toString() + ":" + current.getId();
            lockValueThreadLocal.set(lockValue);
        }
        
        // 检查是否收到过锁释放通知
        boolean lockReleased = lockReleasedNotification.get();
        if (lockReleased) {
            System.out.println("线程" + current.getId() + "检测到锁释放通知，立即重试获取锁...");
            // 重置锁释放通知标志
            lockReleasedNotification.set(false);
        }
        
        // 尝试通过Redis获取锁
        boolean acquired = lockManager.acquireLock(lockKey, lockValue, config.getLeaseTime());
        System.out.println("线程" + current.getId() + "尝试获取Redis锁: " + acquired + " (时间: " + System.currentTimeMillis() + ")");
        
        if (acquired) {
            // 获取锁成功，设置持有线程和状态
            setState(acquires);
            setExclusiveOwnerThread(current);
            
            // 启动自动续期任务
            if (config.getRenewalTime() > 0) {
                final String finalLockKey = lockKey;
                final String finalLockValue = lockValue;
                final long leaseTime = config.getLeaseTime();
                final long renewalTime = config.getRenewalTime();
                renewalManager.startRenewal(finalLockKey, 
                    () -> lettuceClient.renew(finalLockKey, finalLockValue, leaseTime), renewalTime);
            }
            
            // 启动Watchdog监控
            if (config.getWatchdogPeriod() > 0) {
                watchdog.startWatchdog(this, config.getWatchdogPeriod());
            }
            
            return true;
        } else {
            // 获取锁失败，订阅锁释放通知频道（如果尚未订阅）
            // 注意：这里我们不立即订阅，而是在适当的时候订阅以避免过多连接
            System.out.println("线程" + current.getId() + "获取Redis锁失败，等待锁释放通知...");
            // 记录等待线程并park等待通知
            waitingThread = current;
            try {
                LockSupport.park(this);
                System.out.println("线程" + current.getId() + "被唤醒，重新尝试获取锁...");
            } finally {
                waitingThread = null;
            }
            // 被唤醒后重新尝试获取锁
            return tryAcquire(acquires);
        }
    }
    
    @Override
    protected boolean tryRelease(int releases) {
        // 如果当前线程不是锁的持有线程，抛出异常
        if (Thread.currentThread() != getExclusiveOwnerThread()) {
            throw new IllegalMonitorStateException("Current thread not owner");
        }
        
        // 获取锁值
        String lockValue = lockValueThreadLocal.get();
        if (lockValue == null) {
            throw new IllegalMonitorStateException("Lock value not found");
        }
        
        boolean free = false;
        int c = getState() - releases;
        
        // 如果状态为0，说明锁完全释放
        if (c == 0) {
            free = true;
            
            // 停止Watchdog监控
            if (watchdog != null && watchdog.isRunning()) {
                watchdog.stopWatchdog();
            }
            
            // 停止自动续期任务
            renewalManager.stopRenewal(lockKey);
            
            // 释放Redis中的锁
            lockManager.releaseLock(lockKey, lockValue, config.getLeaseTime());
            
            // 发布锁释放通知
            String releaseMessage = "{\"lockKey\":\"" + lockKey + "\",\"releaseTime\":" + System.currentTimeMillis() + "}";
            long subscribers = lettuceClient.publish(releaseNotificationChannel, releaseMessage);
            System.out.println("发布锁释放通知到频道: " + releaseNotificationChannel + "，订阅者数量: " + subscribers);
            
            // 清除持有线程和锁值
            setExclusiveOwnerThread(null);
            lockValueThreadLocal.remove();
        }
        
        // 更新状态
        setState(c);
        return free;
    }
    
    @Override
    public void lock() {
        // 调用AQS的acquire方法，会调用tryAcquire
        acquire(1);
    }
    
    @Override
    public void lockInterruptibly() throws InterruptedException {
        // 调用AQS的acquireInterruptibly方法
        acquireInterruptibly(1);
    }
    
    @Override
    public boolean tryLock() {
        // 调用AQS的tryAcquire方法
        return tryAcquire(1);
    }
    
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // 调用AQS的tryAcquireNanos方法，该方法会在超时时间内循环调用我们重写的tryAcquire方法
        return tryAcquireNanos(1, unit.toNanos(time));
    }
    
    @Override
    public void unlock() {
        // 调用AQS的release方法，会调用tryRelease
        release(1);
    }
    
    @Override
    public java.util.concurrent.locks.Condition newCondition() {
        // 返回一个新的条件对象
        return new java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject();
    }
    
    /**
     * 获取锁的键名
     * @return 锁的键名
     */
    public String getLockKey() {
        return lockKey;
    }
    
    /**
     * 获取锁配置
     * @return 锁配置
     */
    public LockConfig getConfig() {
        return config;
    }
    
    /**
     * 获取锁值
     * @return 锁值
     */
    public String getLockValue() {
        return lockValueThreadLocal.get();
    }
    
    /**
     * 获取Lettuce客户端
     * @return Lettuce客户端
     */
    public LettuceClient getLettuceClient() {
        return lettuceClient;
    }
    
    /**
     * 获取ExclusiveOwnerThread
     * @return 持有锁的线程
     */
    public Thread getOwnerThread() {
        return super.getExclusiveOwnerThread();
    }
}