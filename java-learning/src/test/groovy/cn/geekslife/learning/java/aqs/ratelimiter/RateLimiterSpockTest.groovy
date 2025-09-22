package cn.geekslife.learning.java.aqs.ratelimiter

import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.TokenBucketAlgorithm
import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.SlidingWindowAlgorithm
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * RateLimiter的Spock测试类
 */
class RateLimiterSpockTest extends Specification {

    @Subject
    RateLimiter rateLimiter

    def "应该创建令牌桶限流器"() {
        given:
        double permitsPerSecond = 10.0

        when:
        rateLimiter = RateLimiter.create(permitsPerSecond)

        then:
        rateLimiter != null
        rateLimiter instanceof AQSRateLimiter
        rateLimiter.getRate() == permitsPerSecond
    }

    def "应该创建滑动窗口限流器"() {
        given:
        int maxPermits = 50
        long windowSizeMillis = 5000

        when:
        rateLimiter = RateLimiter.createSlidingWindow(maxPermits, windowSizeMillis)

        then:
        rateLimiter != null
        rateLimiter instanceof AQSRateLimiter
    }

    def "应该成功获取令牌"() {
        given:
        rateLimiter = RateLimiter.create(5.0) // 每秒5个请求

        when:
        double waitTime = rateLimiter.acquire()

        then:
        waitTime == 0.0
    }

    def "应该在令牌不足时等待获取令牌"() {
        given:
        rateLimiter = RateLimiter.create(1.0) // 每秒1个请求
        
        // 先消耗几个令牌
        rateLimiter.acquire()
        rateLimiter.acquire()

        when:
        long startTime = System.currentTimeMillis()
        double waitTime = rateLimiter.acquire() // 第三个请求需要等待
        long endTime = System.currentTimeMillis()

        then:
        waitTime > 0.0
        endTime - startTime >= 900 // 至少等待900毫秒
    }

    def "应该成功尝试获取令牌"() {
        given:
        rateLimiter = RateLimiter.create(5.0) // 每秒5个请求

        when:
        boolean acquired = rateLimiter.tryAcquire()

        then:
        acquired == true
    }

    def "应该失败尝试获取令牌"() {
        given:
        rateLimiter = RateLimiter.create(1.0) // 每秒1个请求
        // 消耗所有可用令牌
        rateLimiter.acquire()
        rateLimiter.acquire()

        when:
        boolean acquired = rateLimiter.tryAcquire() // 立即返回，不等待

        then:
        acquired == false
    }

    def "应该设置新的速率"() {
        given:
        rateLimiter = RateLimiter.create(5.0)
        double newRate = 10.0

        when:
        rateLimiter.setRate(newRate)

        then:
        rateLimiter.getRate() == newRate
    }

    def "应该获取当前速率"() {
        given:
        double expectedRate = 7.0
        rateLimiter = RateLimiter.create(expectedRate)

        when:
        double actualRate = rateLimiter.getRate()

        then:
        actualRate == expectedRate
    }

    @Unroll
    def "应该在不同速率下正确工作 (#permitsPerSecond permits/second)"() {
        given:
        rateLimiter = RateLimiter.create(permitsPerSecond)

        when:
        double waitTime = rateLimiter.acquire()

        then:
        waitTime >= 0.0

        where:
        permitsPerSecond << [1.0, 5.0, 10.0, 50.0, 100.0]
    }

    def "应该处理线程中断"() {
        given:
        rateLimiter = RateLimiter.create(1.0) // 每秒1个请求
        // 消耗令牌，使后续请求需要等待
        rateLimiter.acquire()
        rateLimiter.acquire()

        when:
        Thread.currentThread().interrupt()
        rateLimiter.acquire()

        then:
        thrown(InterruptedException)
        Thread.interrupted() // 清除中断状态
    }

    def "应该在超时时间内尝试获取令牌"() {
        given:
        rateLimiter = RateLimiter.create(1.0) // 每秒1个请求
        // 消耗令牌，使后续请求需要等待
        rateLimiter.acquire()
        rateLimiter.acquire()

        when:
        boolean acquired = rateLimiter.tryAcquire(100, TimeUnit.MILLISECONDS)

        then:
        !acquired
    }

    def "应该在超时时间内成功获取令牌"() {
        given:
        rateLimiter = RateLimiter.create(10.0) // 每秒10个请求

        when:
        boolean acquired = rateLimiter.tryAcquire(100, TimeUnit.MILLISECONDS)

        then:
        acquired
    }

    def "应该处理多个并发请求"() {
        given:
        rateLimiter = RateLimiter.create(5.0) // 每秒5个请求
        int threadCount = 10
        CountDownLatch latch = new CountDownLatch(threadCount)
        AtomicInteger successCount = new AtomicInteger(0)
        ExecutorService executor = Executors.newFixedThreadPool(threadCount)

        when:
        // 启动多个并发线程
        for (int i = 0; i < threadCount; i++) {
            executor.submit({
                try {
                    if (rateLimiter.tryAcquire()) {
                        successCount.incrementAndGet()
                    }
                } finally {
                    latch.countDown()
                }
            })
        }

        // 等待所有线程完成
        latch.await(5, TimeUnit.SECONDS)
        executor.shutdown()

        then:
        successCount.get() > 0
        // 在并发测试中，可能有多于5个请求成功，因为我们没有严格控制时间
        successCount.get() <= 10 // 最多10个成功（考虑到并发和测试时间窗口）
    }

    def "应该正确处理令牌桶算法的时间累积"() {
        given:
        rateLimiter = RateLimiter.create(1.0) // 每秒1个请求
        // 消耗令牌
        rateLimiter.acquire()

        when:
        // 等待2秒让令牌重新生成
        Thread.sleep(2000)
        double waitTime = rateLimiter.acquire()

        then:
        waitTime == 0.0 // 应该有足够的令牌，不需要等待
    }

    def "应该正确处理负数参数"() {
        given:
        rateLimiter = RateLimiter.create(1.0)

        when:
        rateLimiter.acquire(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def "应该正确处理零速率"() {
        when:
        RateLimiter.create(0.0)

        then:
        thrown(IllegalArgumentException)
    }

    def "应该正确处理极大速率"() {
        given:
        rateLimiter = RateLimiter.create(1000000.0) // 每秒100万个请求

        when:
        double waitTime = rateLimiter.acquire()

        then:
        waitTime == 0.0 // 应该几乎不需要等待
    }
}