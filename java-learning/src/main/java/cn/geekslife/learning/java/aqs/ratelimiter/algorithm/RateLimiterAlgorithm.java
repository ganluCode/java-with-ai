package cn.geekslife.learning.java.aqs.ratelimiter.algorithm;

import java.util.concurrent.TimeUnit;

/**
 * 限流算法接口
 */
public interface RateLimiterAlgorithm {
    
    /**
     * 预留令牌
     * @param permits 令牌数量
     * @return 需要等待的时间(微秒)
     */
    long reserve(int permits);
    
    /**
     * 取消预留的令牌
     * @param permits 令牌数量
     * @param waitMicros 等待时间
     */
    void cancelReservation(int permits, long waitMicros);
    
    /**
     * 更新令牌数
     * @param nowMicros 当前时间(微秒)
     */
    void updatePermits(long nowMicros);
    
    /**
     * 读取当前时间(微秒)
     * @return 当前时间(微秒)
     */
    default long readMicros() {
        return System.nanoTime() / 1000;
    }
    
    /**
     * 休眠指定微秒数，不可中断
     * @param micros 微秒数
     */
    default void sleepMicrosUninterruptibly(long micros) {
        try {
            TimeUnit.MICROSECONDS.sleep(micros);
        } catch (InterruptedException e) {
            // 忽略中断异常
            Thread.currentThread().interrupt();
        }
    }
}