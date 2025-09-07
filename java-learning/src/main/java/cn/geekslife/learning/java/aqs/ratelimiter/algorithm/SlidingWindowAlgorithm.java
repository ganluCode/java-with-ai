package cn.geekslife.learning.java.aqs.ratelimiter.algorithm;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 滑动窗口算法实现
 */
public class SlidingWindowAlgorithm implements RateLimiterAlgorithm {
    // 窗口大小(毫秒)
    private final long windowSizeMillis;
    
    // 最大请求数
    private final int maxPermits;
    
    // 请求时间戳队列
    private final Queue<Long> requestTimestamps;
    
    // 预留的请求数量（用于跟踪已预留但尚未添加到窗口的请求）
    private int reservedPermits = 0;
    
    /**
     * 构造函数
     * @param maxPermits 最大请求数
     * @param windowSizeMillis 窗口大小(毫秒)
     */
    public SlidingWindowAlgorithm(int maxPermits, long windowSizeMillis) {
        if (maxPermits <= 0) {
            throw new IllegalArgumentException("Max permits must be positive");
        }
        if (windowSizeMillis <= 0) {
            throw new IllegalArgumentException("Window size must be positive");
        }
        this.maxPermits = maxPermits;
        this.windowSizeMillis = windowSizeMillis;
        this.requestTimestamps = new ConcurrentLinkedQueue<>();
    }
    
    /**
     * 预留令牌
     * @param permits 令牌数量
     * @return 需要等待的时间(微秒)
     */
    @Override
    public long reserve(int permits) {
        if (permits < 0) {
            throw new IllegalArgumentException("Permits must be non-negative");
        }
        
        long now = System.currentTimeMillis();
        synchronized (this) {
            // 清理过期的请求
            cleanupExpiredRequests(now);
            
            // 检查是否超过限制（包括已预留的请求数量）
            if (requestTimestamps.size() + reservedPermits + permits > maxPermits) {
                // 计算需要等待的时间
                Long oldestTimestamp = requestTimestamps.peek();
                if (oldestTimestamp != null) {
                    long waitTime = oldestTimestamp + windowSizeMillis - now;
                    return Math.max(waitTime, 0) * 1000; // 转换为微秒
                }
                return windowSizeMillis * 1000; // 转换为微秒
            }
            
            // 增加预留计数
            reservedPermits += permits;
            
            // 添加请求时间戳
            for (int i = 0; i < permits; i++) {
                requestTimestamps.offer(now);
            }
            
            // 减少预留计数
            reservedPermits -= permits;
            
            return 0L; // 不需要等待
        }
    }
    
    /**
     * 取消预留的令牌
     * @param permits 令牌数量
     * @param waitMicros 等待时间
     */
    @Override
    public void cancelReservation(int permits, long waitMicros) {
        synchronized (this) {
            // 移除已添加的请求时间戳
            for (int i = 0; i < permits; i++) {
                requestTimestamps.poll();
            }
        }
    }
    
    /**
     * 更新令牌数
     * @param nowMicros 当前时间(微秒)
     */
    @Override
    public void updatePermits(long nowMicros) {
        long now = nowMicros / 1000; // 转换为毫秒
        cleanupExpiredRequests(now);
    }
    
    /**
     * 清理过期的请求
     * @param now 当前时间(毫秒)
     */
    private void cleanupExpiredRequests(long now) {
        long windowStart = now - windowSizeMillis;
        while (!requestTimestamps.isEmpty() && requestTimestamps.peek() <= windowStart) {
            requestTimestamps.poll();
        }
    }
}