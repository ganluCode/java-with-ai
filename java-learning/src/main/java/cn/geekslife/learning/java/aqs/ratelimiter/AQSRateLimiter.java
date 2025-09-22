package cn.geekslife.learning.java.aqs.ratelimiter;

import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.RateLimiterAlgorithm;
import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.TokenBucketAlgorithm;
import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.SlidingWindowAlgorithm;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 基于AQS的限流器实现
 */
public class AQSRateLimiter extends RateLimiter {
    // AQS同步器
    private final Sync sync;
    
    // 限流算法
    private final RateLimiterAlgorithm algorithm;
    
    // 算法类型
    private final AlgorithmType algorithmType;
    
    /**
     * 算法类型枚举
     */
    private enum AlgorithmType {
        TOKEN_BUCKET,
        SLIDING_WINDOW
    }
    
    /**
     * 构造函数
     * @param algorithm 限流算法实现
     */
    public AQSRateLimiter(RateLimiterAlgorithm algorithm) {
        this.sync = new Sync();
        this.algorithm = algorithm;
        this.algorithmType = determineAlgorithmType(algorithm);
    }
    
    /**
     * 确定算法类型
     * @param algorithm 算法实现
     * @return 算法类型
     */
    private AlgorithmType determineAlgorithmType(RateLimiterAlgorithm algorithm) {
        if (algorithm instanceof TokenBucketAlgorithm) {
            return AlgorithmType.TOKEN_BUCKET;
        } else if (algorithm instanceof SlidingWindowAlgorithm) {
            return AlgorithmType.SLIDING_WINDOW;
        } else {
            // 对于测试中的模拟对象，返回默认类型
            return AlgorithmType.TOKEN_BUCKET;
        }
    }
    
    /**
     * 获取一个令牌，可能会阻塞直到获取成功
     * @return 等待的时间(秒)
     * @throws InterruptedException 如果线程被中断
     */
    @Override
    public double acquire() throws InterruptedException {
        return acquire(1);
    }
    
    /**
     * 获取指定数量的令牌，可能会阻塞直到获取成功
     * @param permits 令牌数量
     * @return 等待的时间(秒)
     * @throws InterruptedException 如果线程被中断
     */
    @Override
    public double acquire(int permits) throws InterruptedException {
        // 检查线程是否已被中断
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Thread was interrupted before acquiring permits");
        }
        
        long waitMicros = algorithm.reserve(permits);
        if (waitMicros > 0) {
            sync.acquireSharedInterruptibly(permits);
            // 检查在等待期间线程是否被中断
            if (Thread.currentThread().isInterrupted()) {
                // 取消预留并抛出中断异常
                algorithm.cancelReservation(permits, waitMicros);
                throw new InterruptedException("Thread was interrupted while waiting for permits");
            }
            algorithm.sleepMicrosUninterruptibly(waitMicros);
        }
        return 1.0 * waitMicros / TimeUnit.SECONDS.toMicros(1);
    }
    
    /**
     * 尝试获取一个令牌，不会阻塞
     * @return 是否获取成功
     */
    @Override
    public boolean tryAcquire() {
        return tryAcquire(1);
    }
    
    /**
     * 尝试获取指定数量的令牌，不会阻塞
     * @param permits 令牌数量
     * @return 是否获取成功
     */
    @Override
    public boolean tryAcquire(int permits) {
        return tryAcquire(permits, 0, TimeUnit.MICROSECONDS);
    }
    
    /**
     * 在指定时间内尝试获取一个令牌
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    @Override
    public boolean tryAcquire(long timeout, TimeUnit unit) {
        return tryAcquire(1, timeout, unit);
    }
    
    /**
     * 在指定时间内尝试获取指定数量的令牌
     * @param permits 令牌数量
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    @Override
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        long timeoutMicros = Math.max(unit.toMicros(timeout), 0);
        long waitMicros = algorithm.reserve(permits);
        if (waitMicros > timeoutMicros) {
            // 如果需要等待的时间超过了超时时间，取消获取并返回false
            algorithm.cancelReservation(permits, waitMicros);
            return false;
        }
        try {
            if (!sync.tryAcquireSharedNanos(permits, timeoutMicros - waitMicros)) {
                // 如果获取失败，取消预留
                algorithm.cancelReservation(permits, waitMicros);
                return false;
            }
        } catch (InterruptedException e) {
            // 恢复中断状态
            Thread.currentThread().interrupt();
            algorithm.cancelReservation(permits, waitMicros);
            return false;
        }
        // 等待所需时间
        if (waitMicros > 0) {
            algorithm.sleepMicrosUninterruptibly(waitMicros);
        }
        return true;
    }
    
    /**
     * 设置每秒生成令牌数
     * @param permitsPerSecond 每秒生成令牌数
     */
    @Override
    public void setRate(double permitsPerSecond) {
        switch (algorithmType) {
            case TOKEN_BUCKET:
                ((TokenBucketAlgorithm) algorithm).setRate(permitsPerSecond);
                break;
            default:
                throw new UnsupportedOperationException("当前算法不支持设置速率");
        }
    }
    
    /**
     * 获取每秒生成令牌数
     * @return 每秒生成令牌数
     */
    @Override
    public double getRate() {
        switch (algorithmType) {
            case TOKEN_BUCKET:
                return ((TokenBucketAlgorithm) algorithm).getRate();
            default:
                throw new UnsupportedOperationException("当前算法不支持获取速率");
        }
    }
    
    /**
     * AQS同步器实现
     */
    private static class Sync extends AbstractQueuedSynchronizer {
        Sync() {
            // 初始化状态
            setState(0);
        }
        
        /**
         * 尝试获取共享锁
         * @param permits 令牌数量
         * @return 负数表示失败，非负数表示成功
         */
        @Override
        protected int tryAcquireShared(int permits) {
            // 在基于AQS的限流器中，我们使用AQS来管理线程排队
            // 由于令牌检查已在algorithm.reserve方法中完成，这里总是返回成功
            return 1;
        }
        
        /**
         * 尝试释放共享锁
         * @param permits 令牌数量
         * @return 是否需要传播
         */
        @Override
        protected boolean tryReleaseShared(int permits) {
            // 唤醒等待的线程
            return true;
        }
    }
}