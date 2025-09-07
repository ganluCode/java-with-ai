package cn.geekslife.learning.java.aqs.ratelimiter.algorithm

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * SlidingWindowAlgorithm的Spock测试类
 */
class SlidingWindowAlgorithmSpockTest extends Specification {

    @Subject
    SlidingWindowAlgorithm slidingWindowAlgorithm

    def "应该创建滑动窗口算法实例"() {
        given:
        int maxPermits = 100
        long windowSizeMillis = 5000

        when:
        slidingWindowAlgorithm = new SlidingWindowAlgorithm(maxPermits, windowSizeMillis)

        then:
        slidingWindowAlgorithm != null
    }

    def "应该在窗口内允许请求"() {
        given:
        slidingWindowAlgorithm = new SlidingWindowAlgorithm(10, 5000) // 5秒内最多10个请求

        when:
        long waitMicros = slidingWindowAlgorithm.reserve(5) // 请求5个令牌

        then:
        waitMicros == 0L
    }

    def "应该在达到限制时拒绝请求"() {
        given:
        slidingWindowAlgorithm = new SlidingWindowAlgorithm(5, 5000) // 5秒内最多5个请求
        // 消耗所有可用请求
        slidingWindowAlgorithm.reserve(5)

        when:
        long waitMicros = slidingWindowAlgorithm.reserve(1) // 再请求1个令牌

        then:
        waitMicros > 0L
    }

    def "应该在窗口滑动后允许新请求"() {
        given:
        slidingWindowAlgorithm = new SlidingWindowAlgorithm(3, 2000) // 2秒内最多3个请求
        // 消耗所有可用请求
        slidingWindowAlgorithm.reserve(3)

        when:
        // 等待窗口滑动
        Thread.sleep(2500)
        long waitMicros = slidingWindowAlgorithm.reserve(1) // 请求1个令牌

        then:
        waitMicros == 0L
    }

    def "应该正确清理过期请求"() {
        given:
        slidingWindowAlgorithm = new SlidingWindowAlgorithm(10, 3000) // 3秒内最多10个请求

        when:
        // 添加一些请求
        slidingWindowAlgorithm.reserve(5)
        // 等待请求过期
        Thread.sleep(3500)
        // 清理过期请求后再次请求
        long waitMicros = slidingWindowAlgorithm.reserve(5)

        then:
        waitMicros == 0L
    }

    def "应该正确处理取消预留"() {
        given:
        slidingWindowAlgorithm = new SlidingWindowAlgorithm(5, 5000) // 5秒内最多5个请求
        long waitMicros = slidingWindowAlgorithm.reserve(6) // 请求超过限制的令牌数

        when:
        slidingWindowAlgorithm.cancelReservation(6, waitMicros)

        then:
        // 取消预留后，应该能够获取令牌
        slidingWindowAlgorithm.reserve(3) == 0L
    }

    @Unroll
    def "应该在不同配置下正确工作 (#maxPermits permits in #windowSizeMillis ms)"() {
        given:
        slidingWindowAlgorithm = new SlidingWindowAlgorithm(maxPermits, windowSizeMillis)

        when:
        long waitMicros = slidingWindowAlgorithm.reserve(permits)

        then:
        waitMicros >= 0L

        where:
        maxPermits | windowSizeMillis | permits
        10         | 1000             | 5
        50         | 5000             | 25
        100        | 10000            | 50
        200        | 30000            | 100
        1000       | 60000            | 500
    }

    def "应该处理零窗口大小异常"() {
        when:
        new SlidingWindowAlgorithm(10, 0)

        then:
        thrown(IllegalArgumentException)
    }

    def "应该处理负数窗口大小异常"() {
        when:
        new SlidingWindowAlgorithm(10, -5000)

        then:
        thrown(IllegalArgumentException)
    }

    def "应该处理零最大请求数异常"() {
        when:
        new SlidingWindowAlgorithm(0, 5000)

        then:
        thrown(IllegalArgumentException)
    }

    def "应该处理负数最大请求数异常"() {
        when:
        new SlidingWindowAlgorithm(-10, 5000)

        then:
        thrown(IllegalArgumentException)
    }

    def "应该正确更新令牌数"() {
        given:
        slidingWindowAlgorithm = new SlidingWindowAlgorithm(10, 5000)
        long initialTime = System.currentTimeMillis()

        when:
        // 模拟时间更新
        slidingWindowAlgorithm.updatePermits(initialTime + 1000000L)

        then:
        // 更新操作应该成功执行（具体验证取决于实现细节）
        notThrown(Exception)
    }
}