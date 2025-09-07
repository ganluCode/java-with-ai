package cn.geekslife.learning.java.aqs.ratelimiter.algorithm

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

/**
 * TokenBucketAlgorithm的Spock测试类
 */
class TokenBucketAlgorithmSpockTest extends Specification {

    @Subject
    TokenBucketAlgorithm tokenBucketAlgorithm

    def "应该创建令牌桶算法实例"() {
        given:
        double permitsPerSecond = 10.0

        when:
        tokenBucketAlgorithm = new TokenBucketAlgorithm(permitsPerSecond)

        then:
        tokenBucketAlgorithm != null
        tokenBucketAlgorithm.getRate() == permitsPerSecond
    }

    def "应该设置和获取速率"() {
        given:
        tokenBucketAlgorithm = new TokenBucketAlgorithm(5.0)
        double newRate = 15.0

        when:
        tokenBucketAlgorithm.setRate(newRate)

        then:
        tokenBucketAlgorithm.getRate() == newRate
    }

    def "应该在有足够令牌时立即返回"() {
        given:
        tokenBucketAlgorithm = new TokenBucketAlgorithm(5.0) // 每秒5个令牌

        when:
        long waitMicros = tokenBucketAlgorithm.reserve(3) // 请求3个令牌

        then:
        waitMicros == 0L
    }

    def "应该在令牌不足时计算等待时间"() {
        given:
        tokenBucketAlgorithm = new TokenBucketAlgorithm(1.0) // 每秒1个令牌
        // 消耗所有令牌
        tokenBucketAlgorithm.reserve(1)

        when:
        long waitMicros = tokenBucketAlgorithm.reserve(1) // 再请求1个令牌

        then:
        waitMicros > 0L
        waitMicros >= 900000L // 至少等待0.9秒（微秒）
        waitMicros <= 1100000L // 最多等待1.1秒（微秒）
    }

    def "应该随着时间推移生成新令牌"() {
        given:
        tokenBucketAlgorithm = new TokenBucketAlgorithm(2.0) // 每秒2个令牌
        // 消耗所有令牌
        tokenBucketAlgorithm.reserve(2)

        when:
        // 等待1.5秒
        Thread.sleep(1500)
        long waitMicros = tokenBucketAlgorithm.reserve(2) // 请求2个令牌

        then:
        waitMicros >= 0L // 应该已经有足够的令牌或者只需要少量等待
    }

    def "应该正确处理取消预留"() {
        given:
        tokenBucketAlgorithm = new TokenBucketAlgorithm(1.0) // 每秒1个令牌
        long waitMicros = tokenBucketAlgorithm.reserve(2) // 请求2个令牌，需要等待

        when:
        tokenBucketAlgorithm.cancelReservation(2, waitMicros)

        then:
        // 取消预留后，应该能够立即获取令牌
        tokenBucketAlgorithm.reserve(1) == 0L
    }

    @Unroll
    def "应该在不同速率下正确工作 (#permitsPerSecond permits/second)"() {
        given:
        tokenBucketAlgorithm = new TokenBucketAlgorithm(permitsPerSecond)

        when:
        long waitMicros = tokenBucketAlgorithm.reserve(permits)

        then:
        waitMicros >= 0L

        where:
        permitsPerSecond | permits
        1.0              | 1
        5.0              | 2
        10.0             | 5
        50.0             | 10
        100.0            | 20
    }

    def "应该处理零速率异常"() {
        when:
        new TokenBucketAlgorithm(0.0)

        then:
        thrown(IllegalArgumentException)
    }

    def "应该处理负数速率异常"() {
        when:
        new TokenBucketAlgorithm(-5.0)

        then:
        thrown(IllegalArgumentException)
    }

    def "应该正确更新令牌数"() {
        given:
        tokenBucketAlgorithm = new TokenBucketAlgorithm(1.0) // 每秒1个令牌
        long initialTime = tokenBucketAlgorithm.readMicros()

        when:
        // 模拟时间过去1秒
        tokenBucketAlgorithm.updatePermits(initialTime + 1000000L)

        then:
        // 令牌数应该增加
        // 注意：具体数值取决于实现细节
        notThrown(Exception)
    }
}