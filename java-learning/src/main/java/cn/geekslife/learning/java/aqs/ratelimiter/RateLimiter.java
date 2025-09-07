package cn.geekslife.learning.java.aqs.ratelimiter;

import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.RateLimiterAlgorithm;
import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.TokenBucketAlgorithm;
import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.SlidingWindowAlgorithm;
import java.util.concurrent.TimeUnit;

/**
 * 限流器抽象类
 */
public abstract class RateLimiter {
    
    /**
     * 创建令牌桶算法限流器
     * @param permitsPerSecond 每秒允许的请求数
     * @return 限流器实例
     */
    public static RateLimiter create(double permitsPerSecond) {
        return new AQSRateLimiter(new TokenBucketAlgorithm(permitsPerSecond));
    }
    
    /**
     * 创建滑动窗口算法限流器
     * @param maxPermits 窗口内最大请求数
     * @param windowSizeMillis 窗口大小(毫秒)
     * @return 限流器实例
     */
    public static RateLimiter createSlidingWindow(int maxPermits, long windowSizeMillis) {
        return new AQSRateLimiter(new SlidingWindowAlgorithm(maxPermits, windowSizeMillis));
    }
    
    /**
     * 获取一个令牌，可能会阻塞直到获取成功
     * @return 等待的时间(秒)
     * @throws InterruptedException 如果线程被中断
     */
    public abstract double acquire() throws InterruptedException;
    
    /**
     * 获取指定数量的令牌，可能会阻塞直到获取成功
     * @param permits 令牌数量
     * @return 等待的时间(秒)
     * @throws InterruptedException 如果线程被中断
     */
    public abstract double acquire(int permits) throws InterruptedException;
    
    /**
     * 尝试获取一个令牌，不会阻塞
     * @return 是否获取成功
     */
    public abstract boolean tryAcquire();
    
    /**
     * 尝试获取指定数量的令牌，不会阻塞
     * @param permits 令牌数量
     * @return 是否获取成功
     */
    public abstract boolean tryAcquire(int permits);
    
    /**
     * 在指定时间内尝试获取一个令牌
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    public abstract boolean tryAcquire(long timeout, TimeUnit unit);
    
    /**
     * 在指定时间内尝试获取指定数量的令牌
     * @param permits 令牌数量
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    public abstract boolean tryAcquire(int permits, long timeout, TimeUnit unit);
    
    /**
     * 设置每秒生成令牌数
     * @param permitsPerSecond 每秒生成令牌数
     */
    public abstract void setRate(double permitsPerSecond);
    
    /**
     * 获取每秒生成令牌数
     * @return 每秒生成令牌数
     */
    public abstract double getRate();
}