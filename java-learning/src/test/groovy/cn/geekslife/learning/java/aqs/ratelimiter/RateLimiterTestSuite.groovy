package cn.geekslife.learning.java.aqs.ratelimiter

import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.TokenBucketAlgorithmSpockTest
import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.SlidingWindowAlgorithmSpockTest
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite
import spock.lang.Specification

/**
 * AQS限流器完整测试套件
 * 包含所有单元测试、集成测试和性能测试
 */
@Suite
@SelectClasses([
    // 基础功能测试
    RateLimiterSpockTest,
    AQSRateLimiterSpockTest,
    
    // 算法实现测试
    TokenBucketAlgorithmSpockTest,
    SlidingWindowAlgorithmSpockTest,
    
    // 集成测试
    RateLimiterIntegrationSpockTest,
    
    // 性能测试
    RateLimiterPerformanceSpockTest
])
class RateLimiterTestSuite extends Specification {
    // 测试套件类，用于组织和运行所有测试
}