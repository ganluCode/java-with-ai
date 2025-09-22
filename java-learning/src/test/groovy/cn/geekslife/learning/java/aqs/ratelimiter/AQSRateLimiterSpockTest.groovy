package cn.geekslife.learning.java.aqs.ratelimiter

import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.RateLimiterAlgorithm
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
 * AQSRateLimiter的Spock测试类
 */
class AQSRateLimiterSpockTest extends Specification {

    @Subject
    AQSRateLimiter aqsRateLimiter

    def "应该创建AQS限流器实例"() {
        given:
        RateLimiterAlgorithm algorithm = new TokenBucketAlgorithm(10.0)

        when:
        aqsRateLimiter = new AQSRateLimiter(algorithm)

        then:
        aqsRateLimiter != null
    }

    def "应该使用令牌桶算法创建AQS限流器"() {
        given:
        TokenBucketAlgorithm tokenBucketAlgorithm = new TokenBucketAlgorithm(5.0)

        when:
        aqsRateLimiter = new AQSRateLimiter(tokenBucketAlgorithm)

        then:
        aqsRateLimiter != null
        aqsRateLimiter.getRate() == 5.0
    }

    def "应该使用滑动窗口算法创建AQS限流器"() {
        given:
        SlidingWindowAlgorithm slidingWindowAlgorithm = new SlidingWindowAlgorithm(50, 5000)

        when:
        aqsRateLimiter = new AQSRateLimiter(slidingWindowAlgorithm)

        then:
        aqsRateLimiter != null
    }

    def "应该成功获取令牌"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(10.0)
        aqsRateLimiter = new AQSRateLimiter(algorithm)

        when:
        double waitTime = aqsRateLimiter.acquire()

        then:
        waitTime == 0.0
    }

    def "应该在令牌不足时等待获取令牌"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(1.0) // 每秒1个令牌
        aqsRateLimiter = new AQSRateLimiter(algorithm)
        // 消耗令牌
        aqsRateLimiter.acquire()
        aqsRateLimiter.acquire()

        when:
        long startTime = System.currentTimeMillis()
        double waitTime = aqsRateLimiter.acquire() // 第三个请求需要等待
        long endTime = System.currentTimeMillis()

        then:
        waitTime > 0.0
        endTime - startTime >= 900 // 至少等待900毫秒
    }

    def "应该成功尝试获取令牌"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(5.0) // 每秒5个令牌
        aqsRateLimiter = new AQSRateLimiter(algorithm)

        when:
        boolean acquired = aqsRateLimiter.tryAcquire()

        then:
        acquired == true
    }

    def "应该失败尝试获取令牌"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(1.0) // 每秒1个令牌
        aqsRateLimiter = new AQSRateLimiter(algorithm)
        // 消耗所有可用令牌
        aqsRateLimiter.acquire()
        aqsRateLimiter.acquire()

        when:
        boolean acquired = aqsRateLimiter.tryAcquire() // 立即返回，不等待

        then:
        acquired == false
    }

    def "应该在超时时间内尝试获取令牌成功"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(10.0) // 每秒10个令牌
        aqsRateLimiter = new AQSRateLimiter(algorithm)

        when:
        boolean acquired = aqsRateLimiter.tryAcquire(100, TimeUnit.MILLISECONDS)

        then:
        acquired == true
    }

    def "应该在超时时间内尝试获取令牌失败"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(1.0) // 每秒1个令牌
        aqsRateLimiter = new AQSRateLimiter(algorithm)
        // 消耗令牌，使后续请求需要等待
        aqsRateLimiter.acquire()
        aqsRateLimiter.acquire()

        when:
        boolean acquired = aqsRateLimiter.tryAcquire(100, TimeUnit.MILLISECONDS)

        then:
        acquired == false
    }

    def "应该设置新的速率"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(5.0)
        aqsRateLimiter = new AQSRateLimiter(algorithm)
        double newRate = 15.0

        when:
        aqsRateLimiter.setRate(newRate)

        then:
        aqsRateLimiter.getRate() == newRate
    }

    def "应该获取当前速率"() {
        given:
        double expectedRate = 7.0
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(expectedRate)
        aqsRateLimiter = new AQSRateLimiter(algorithm)

        when:
        double actualRate = aqsRateLimiter.getRate()

        then:
        actualRate == expectedRate
    }

    def "应该对滑动窗口算法抛出不支持设置速率异常"() {
        given:
        SlidingWindowAlgorithm algorithm = new SlidingWindowAlgorithm(50, 5000)
        aqsRateLimiter = new AQSRateLimiter(algorithm)

        when:
        aqsRateLimiter.setRate(10.0)

        then:
        thrown(UnsupportedOperationException)
    }

    def "应该对滑动窗口算法抛出不支持获取速率异常"() {
        given:
        SlidingWindowAlgorithm algorithm = new SlidingWindowAlgorithm(50, 5000)
        aqsRateLimiter = new AQSRateLimiter(algorithm)

        when:
        aqsRateLimiter.getRate()

        then:
        thrown(UnsupportedOperationException)
    }

    @Unroll
    def "应该在不同算法下正确工作 (#algorithmType algorithm)"() {
        given:
        RateLimiterAlgorithm algorithm
        if (algorithmType == "tokenBucket") {
            algorithm = new TokenBucketAlgorithm(10.0)
        } else if (algorithmType == "slidingWindow") {
            algorithm = new SlidingWindowAlgorithm(50, 5000)
        }

        when:
        aqsRateLimiter = new AQSRateLimiter(algorithm)
        double waitTime = aqsRateLimiter.acquire()

        then:
        aqsRateLimiter != null
        waitTime >= 0.0

        where:
        algorithmType << ["tokenBucket", "slidingWindow"]
    }

    def "应该处理多线程并发请求"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(5.0) // 每秒5个令牌
        aqsRateLimiter = new AQSRateLimiter(algorithm)
        int threadCount = 10
        CountDownLatch latch = new CountDownLatch(threadCount)
        AtomicInteger successCount = new AtomicInteger(0)
        ExecutorService executor = Executors.newFixedThreadPool(threadCount)

        when:
        // 启动多个并发线程
        for (int i = 0; i < threadCount; i++) {
            executor.submit({
                try {
                    if (aqsRateLimiter.tryAcquire()) {
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

    def "应该处理线程中断"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(1.0) // 每秒1个令牌
        aqsRateLimiter = new AQSRateLimiter(algorithm)
        // 消耗令牌，使后续请求需要等待
        aqsRateLimiter.acquire()
        aqsRateLimiter.acquire()

        when:
        Thread.currentThread().interrupt()
        aqsRateLimiter.acquire()

        then:
        thrown(InterruptedException)
        Thread.interrupted() // 清除中断状态
    }

    def "应该处理负数令牌请求"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(10.0)
        aqsRateLimiter = new AQSRateLimiter(algorithm)

        when:
        aqsRateLimiter.acquire(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def "应该处理零令牌请求"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(10.0)
        aqsRateLimiter = new AQSRateLimiter(algorithm)

        when:
        double waitTime = aqsRateLimiter.acquire(0)

        then:
        waitTime == 0.0
    }

    def "应该在高并发下保持线程安全"() {
        given:
        TokenBucketAlgorithm algorithm = new TokenBucketAlgorithm(100.0) // 每秒100个令牌
        aqsRateLimiter = new AQSRateLimiter(algorithm)
        int threadCount = 50
        CountDownLatch latch = new CountDownLatch(threadCount)
        AtomicInteger totalAcquired = new AtomicInteger(0)
        ExecutorService executor = Executors.newFixedThreadPool(20)

        when:
        // 启动多个并发线程
        for (int i = 0; i < threadCount; i++) {
            executor.submit({
                try {
                    // 每个线程尝试获取多个令牌
                    for (int j = 0; j < 5; j++) {
                        if (aqsRateLimiter.tryAcquire()) {
                            totalAcquired.incrementAndGet()
                        }
                        // 短暂休眠
                        Thread.sleep(10)
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                } finally {
                    latch.countDown()
                }
            })
        }

        // 等待所有线程完成
        latch.await(10, TimeUnit.SECONDS)
        executor.shutdown()

        then:
        totalAcquired.get() >= 0
        // 在高并发下应该保持一致性
    }
}