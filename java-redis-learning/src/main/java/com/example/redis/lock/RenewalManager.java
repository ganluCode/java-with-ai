package com.example.redis.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Map;

/**
 * 自动续期管理器
 */
public class RenewalManager {
    private final ScheduledExecutorService scheduler;
    private final Map<String, ScheduledFuture<?>> renewalTasks;
    
    public RenewalManager() {
        this.scheduler = Executors.newScheduledThreadPool(4);
        this.renewalTasks = new ConcurrentHashMap<>();
    }
    
    /**
     * 启动续期任务
     * @param lockKey 锁的键名
     * @param renewalTask 续期任务
     * @param period 续期周期（秒）
     */
    public void startRenewal(String lockKey, Runnable renewalTask, long period) {
        // 如果已经存在续期任务，先取消
        stopRenewal(lockKey);
        
        // 启动新的续期任务
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(
            renewalTask, 
            period, 
            period, 
            TimeUnit.SECONDS
        );
        
        // 保存续期任务
        renewalTasks.put(lockKey, future);
        
        System.out.println("启动锁[" + lockKey + "]的自动续期任务");
    }
    
    /**
     * 停止续期任务
     * @param lockKey 锁的键名
     */
    public void stopRenewal(String lockKey) {
        ScheduledFuture<?> future = renewalTasks.remove(lockKey);
        if (future != null && !future.isDone()) {
            future.cancel(true);
            System.out.println("停止锁[" + lockKey + "]的自动续期任务");
        }
    }
    
    /**
     * 关闭续期管理器
     */
    public void shutdown() {
        // 取消所有续期任务
        for (Map.Entry<String, ScheduledFuture<?>> entry : renewalTasks.entrySet()) {
            entry.getValue().cancel(true);
        }
        renewalTasks.clear();
        
        // 关闭调度器
        scheduler.shutdown();
        
        System.out.println("续期管理器已关闭");
    }
}