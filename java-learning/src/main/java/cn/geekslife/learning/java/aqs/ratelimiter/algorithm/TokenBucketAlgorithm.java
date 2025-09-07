package cn.geekslife.learning.java.aqs.ratelimiter.algorithm;

import java.util.concurrent.TimeUnit;

/**
 * 令牌桶算法实现
 */
public class TokenBucketAlgorithm implements RateLimiterAlgorithm {
    // 下次可以获取令牌的时间(微秒)
    private volatile long nextFreeTicketMicros = 0L;
    
    // 当前存储的令牌数
    private volatile double storedPermits = 0.0;
    
    // 最大存储令牌数
    private double maxPermits = 0.0;
    
    // 每秒生成令牌数
    private double permitsPerSecond = 0.0;
    
    // 每个令牌生成间隔(微秒)
    private long stableIntervalMicros = 0L;
    
    /**
     * 构造函数
     * @param permitsPerSecond 每秒生成令牌数
     */
    public TokenBucketAlgorithm(double permitsPerSecond) {
        setRate(permitsPerSecond);
    }
    
    /**
     * 设置每秒生成令牌数
     * @param permitsPerSecond 每秒生成令牌数
     */
    public void setRate(double permitsPerSecond) {
        if (permitsPerSecond <= 0) {
            throw new IllegalArgumentException("Rate must be positive");
        }
        synchronized (this) {
            this.permitsPerSecond = permitsPerSecond;
            this.stableIntervalMicros = (long) (TimeUnit.SECONDS.toMicros(1) / permitsPerSecond);
            this.maxPermits = permitsPerSecond; // 默认最大存储令牌数等于每秒生成令牌数
        }
    }
    
    /**
     * 获取每秒生成令牌数
     * @return 每秒生成令牌数
     */
    public double getRate() {
        return permitsPerSecond;
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
        
        long nowMicros = readMicros();
        synchronized (this) {
            // 更新令牌数
            updatePermits(nowMicros);
            
            // 计算需要等待的时间
            long waitMicros = 0L;
            if (storedPermits < permits) {
                waitMicros = (long) ((permits - storedPermits) * stableIntervalMicros);
            }
            
            // 消耗令牌
            storedPermits = Math.max(0, storedPermits - permits);
            nextFreeTicketMicros = Math.max(nextFreeTicketMicros, nowMicros) + waitMicros;
            
            return waitMicros;
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
            // 恢复令牌
            storedPermits = Math.min(maxPermits, storedPermits + permits);
            nextFreeTicketMicros = Math.max(nextFreeTicketMicros - waitMicros, readMicros());
        }
    }
    
    /**
     * 更新令牌数
     * @param nowMicros 当前时间(微秒)
     */
    @Override
    public void updatePermits(long nowMicros) {
        if (nowMicros > nextFreeTicketMicros) {
            double newPermits = (nowMicros - nextFreeTicketMicros) / (double) stableIntervalMicros;
            storedPermits = Math.min(maxPermits, storedPermits + newPermits);
            nextFreeTicketMicros = nowMicros;
        }
    }
}