package com.example.redis.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import io.lettuce.core.pubsub.RedisPubSubAdapter;

/**
 * 锁管理器
 * 管理锁实例和订阅状态
 */
public class LockManager {
    private final LettuceClient lettuceClient;
    private final Map<String, RedisPubSubAdapter<String, String>> lockSubscribers = new ConcurrentHashMap<>();
    private final Map<String, RedisDistributedLock> lockInstances = new ConcurrentHashMap<>();
    
    public LockManager(LettuceClient lettuceClient) {
        this.lettuceClient = lettuceClient;
    }
    
    /**
     * 获取锁实例
     * @param lockKey 锁的键名
     * @return RedisDistributedLock锁实例
     */
    public RedisDistributedLock getLock(String lockKey) {
        return lockInstances.get(lockKey);
    }
    
    /**
     * 销毁锁实例
     * @param lockKey 锁的键名
     */
    public void destroyLock(String lockKey) {
        lockInstances.remove(lockKey);
    }
    
    /**
     * 添加锁实例
     * @param lockKey 锁的键名
     * @param lock 锁实例
     */
    public void addLock(String lockKey, RedisDistributedLock lock) {
        lockInstances.put(lockKey, lock);
    }
    
    /**
     * 获取锁
     * @param lockKey 锁的键名
     * @param lockValue 锁的值
     * @param leaseTime 锁的过期时间（秒）
     * @return 是否获取成功
     */
    public boolean acquireLock(String lockKey, String lockValue, long leaseTime) {
        return lettuceClient.tryAcquire(lockKey, lockValue, leaseTime);
    }
    
    /**
     * 检查锁是否存在
     * @param lockKey 锁的键名
     * @return 锁是否存在
     */
    public boolean exists(String lockKey) {
        return lettuceClient.exists(lockKey);
    }
    
    /**
     * 释放锁
     * @param lockKey 锁的键名
     * @param lockValue 锁的值
     * @param leaseTime 锁的过期时间（秒）
     * @return 是否释放成功
     */
    public boolean releaseLock(String lockKey, String lockValue, long leaseTime) {
        // 发布锁释放通知
        String releaseNotificationChannel = "lock:release:" + lockKey;
        String releaseMessage = "{\"lockKey\":\"" + lockKey + "\",\"releaseTime\":" + System.currentTimeMillis() + "}";
        long subscribers = lettuceClient.publish(releaseNotificationChannel, releaseMessage);
        System.out.println("LockManager发布锁释放通知到频道: " + releaseNotificationChannel + "，订阅者数量: " + subscribers);
        
        return lettuceClient.release(lockKey, lockValue, leaseTime);
    }
    
    /**
     * 续期锁
     * @param lockKey 锁的键名
     * @param lockValue 锁的值
     * @param leaseTime 锁的过期时间（秒）
     * @return 是否续期成功
     */
    public boolean renewLock(String lockKey, String lockValue, long leaseTime) {
        return lettuceClient.renew(lockKey, lockValue, leaseTime);
    }
    
    /**
     * 订阅锁释放通知
     * @param lockKey 锁的键名
     * @param listener 消息监听器
     */
    public void subscribeToLockRelease(String lockKey, RedisPubSubAdapter<String, String> listener) {
        String releaseNotificationChannel = "lock:release:" + lockKey;
        lettuceClient.subscribe(releaseNotificationChannel, listener);
        lockSubscribers.put(lockKey, listener);
        System.out.println("LockManager订阅锁释放通知频道: " + releaseNotificationChannel);
    }
    
    /**
     * 取消订阅锁释放通知
     * @param lockKey 锁的键名
     */
    public void unsubscribeFromLockRelease(String lockKey) {
        String releaseNotificationChannel = "lock:release:" + lockKey;
        RedisPubSubAdapter<String, String> listener = lockSubscribers.remove(lockKey);
        if (listener != null) {
            lettuceClient.unsubscribe(releaseNotificationChannel);
            System.out.println("LockManager取消订阅锁释放通知频道: " + releaseNotificationChannel);
        }
    }
    
    /**
     * 获取订阅者数量
     * @param lockKey 锁的键名
     * @return 订阅者数量
     */
    public long getSubscriberCount(String lockKey) {
        String releaseNotificationChannel = "lock:release:" + lockKey;
        // 这里只是一个示例，实际的订阅者数量需要通过Redis命令获取
        return lockSubscribers.containsKey(lockKey) ? 1 : 0;
    }
}