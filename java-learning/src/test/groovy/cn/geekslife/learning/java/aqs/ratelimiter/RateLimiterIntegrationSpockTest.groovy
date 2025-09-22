package cn.geekslife.learning.java.aqs.ratelimiter

import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.TokenBucketAlgorithm
import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.SlidingWindowAlgorithm
import spock.lang.Specification
import spock.lang.Subject

import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * AQS限流器集成测试类
 */
class RateLimiterIntegrationSpockTest extends Specification {

    @Subject
    RateLimiter rateLimiter

    def "应该完整测试令牌桶限流器工作流程"() {
        given:
        double permitsPerSecond = 10.0
        rateLimiter = RateLimiter.create(permitsPerSecond)
        int totalRequests = 30
        int threadCount = 5
        CountDownLatch latch = new CountDownLatch(threadCount)
        AtomicInteger successCount = new AtomicInteger(0)
        AtomicInteger failureCount = new AtomicInteger(0)
        ExecutorService executor = Executors.newFixedThreadPool(threadCount)

        when:
        long startTime = System.currentTimeMillis()
        
        // 启动多个并发线程进行测试
        for (int i = 0; i < threadCount; i++) {
            executor.submit({
                try {
                    for (int j = 0; j < totalRequests / threadCount; j++) {
                        // 尝试获取令牌
                        if (rateLimiter.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                            successCount.incrementAndGet()
                        } else {
                            failureCount.incrementAndGet()
                        }
                        
                        // 模拟业务处理时间
                        Thread.sleep(50)
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                } finally {
                    latch.countDown()
                }
            })
        }

        // 等待所有线程完成
        latch.await(30, TimeUnit.SECONDS)
        long endTime = System.currentTimeMillis()
        executor.shutdown()

        then:
        long duration = endTime - startTime
        int totalProcessed = successCount.get() + failureCount.get()
        double actualRate = successCount.get() / (duration / 1000.0)
        
        println "令牌桶算法集成测试结果:"
        println "  总请求数: ${totalRequests}"
        println "  成功请求数: ${successCount.get()}"
        println "  失败请求数: ${failureCount.get()}"
        println "  总耗时: ${duration} ms"
        println "  实际速率: ${actualRate.round(2)} req/sec"
        println "  目标速率: ${permitsPerSecond} req/sec"
        
        // 验证结果
        totalProcessed == totalRequests
        successCount.get() > 0
        actualRate > 0
    }

    def "应该完整测试滑动窗口限流器工作流程"() {
        given:
        int maxPermits = 20
        long windowSizeMillis = 5000 // 5秒窗口
        rateLimiter = RateLimiter.createSlidingWindow(maxPermits, windowSizeMillis)
        int totalRequests = 50
        int threadCount = 10
        CountDownLatch latch = new CountDownLatch(threadCount)
        AtomicInteger successCount = new AtomicInteger(0)
        AtomicInteger failureCount = new AtomicInteger(0)
        ExecutorService executor = Executors.newFixedThreadPool(threadCount)

        when:
        long startTime = System.currentTimeMillis()
        
        // 启动多个并发线程进行测试
        for (int i = 0; i < threadCount; i++) {
            executor.submit({
                try {
                    for (int j = 0; j < totalRequests / threadCount; j++) {
                        // 尝试获取令牌
                        if (rateLimiter.tryAcquire(200, TimeUnit.MILLISECONDS)) {
                            successCount.incrementAndGet()
                        } else {
                            failureCount.incrementAndGet()
                        }
                        
                        // 模拟业务处理时间
                        Thread.sleep(100)
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                } finally {
                    latch.countDown()
                }
            })
        }

        // 等待所有线程完成
        latch.await(30, TimeUnit.SECONDS)
        long endTime = System.currentTimeMillis()
        executor.shutdown()

        then:
        long duration = endTime - startTime
        int totalProcessed = successCount.get() + failureCount.get()
        
        println "滑动窗口算法集成测试结果:"
        println "  总请求数: ${totalRequests}"
        println "  成功请求数: ${successCount.get()}"
        println "  失败请求数: ${failureCount.get()}"
        println "  总耗时: ${duration} ms"
        println "  窗口大小: ${windowSizeMillis} ms"
        println "  最大请求数: ${maxPermits}"
        
        // 验证结果
        totalProcessed == totalRequests
        successCount.get() > 0
    }

    def "应该在不同算法间正确切换"() {
        given:
        double tokenBucketRate = 5.0
        int slidingWindowMaxPermits = 25
        long slidingWindowWindowSize = 3000 // 3秒窗口

        when:
        // 创建令牌桶限流器
        RateLimiter tokenBucketLimiter = RateLimiter.create(tokenBucketRate)
        
        // 创建滑动窗口限流器
        RateLimiter slidingWindowLimiter = RateLimiter.createSlidingWindow(slidingWindowMaxPermits, slidingWindowWindowSize)
        
        // 测试令牌桶限流器
        int tbSuccess = 0
        for (int i = 0; i < 10; i++) {
            if (tokenBucketLimiter.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                tbSuccess++
            }
        }
        
        // 测试滑动窗口限流器
        int swSuccess = 0
        for (int i = 0; i < 10; i++) {
            if (slidingWindowLimiter.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                swSuccess++
            }
        }

        then:
        println "算法切换测试结果:"
        println "  令牌桶算法成功请求数: ${tbSuccess}"
        println "  滑动窗口算法成功请求数: ${swSuccess}"
        
        // 两种算法都应该能正常工作
        tbSuccess > 0
        swSuccess > 0
    }

    def "应该在长时间运行下保持系统稳定"() {
        given:
        rateLimiter = RateLimiter.create(50.0) // 每秒50个请求
        int durationMinutes = 2 // 运行2分钟
        long startTime = System.currentTimeMillis()
        long endTime = startTime + (durationMinutes * 60 * 1000)
        AtomicInteger totalRequests = new AtomicInteger(0)
        AtomicInteger successRequests = new AtomicInteger(0)
        AtomicInteger failedRequests = new AtomicInteger(0)

        when:
        // 长时间运行测试
        while (System.currentTimeMillis() < endTime) {
            totalRequests.incrementAndGet()
            
            // 尝试获取令牌，超时100毫秒
            if (rateLimiter.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                successRequests.incrementAndGet()
            } else {
                failedRequests.incrementAndGet()
            }
            
            // 控制请求频率
            if (totalRequests.get() % 10 == 0) {
                Thread.sleep(100) // 每10个请求休息100毫秒
            }
        }

        long actualEndTime = System.currentTimeMillis()

        then:
        long actualDuration = actualEndTime - startTime
        int totalReq = totalRequests.get()
        int successReq = successRequests.get()
        int failedReq = failedRequests.get()
        double successRate = (successReq / (double) totalReq) * 100
        
        println "长时间运行集成测试结果:"
        println "  运行时长: ${actualDuration} ms (${(actualDuration / 60000).round(2)} 分钟)"
        println "  总请求数: ${totalReq}"
        println "  成功请求数: ${successReq}"
        println "  失败请求数: ${failedReq}"
        println "  成功率: ${successRate.round(2)}%"
        println "  平均请求速率: ${(totalReq / (actualDuration / 1000.0)).round(2)} req/sec"
        
        // 验证结果
        totalReq > 0
        successRate > 80.0 // 成功率应该大于80%
    }

    def "应该正确处理系统资源"() {
        given:
        List<RateLimiter> limiters = new ArrayList<>()
        int limiterCount = 100

        when:
        // 创建大量限流器实例
        for (int i = 0; i < limiterCount; i++) {
            RateLimiter limiter = RateLimiter.create(10.0 + i) // 不同速率
            limiters.add(limiter)
        }
        
        // 对每个限流器进行测试
        List<Integer> successCounts = new ArrayList<>()
        for (RateLimiter limiter : limiters) {
            int success = 0
            for (int i = 0; i < 5; i++) {
                if (limiter.tryAcquire(10, TimeUnit.MILLISECONDS)) {
                    success++
                }
            }
            successCounts.add(success)
        }

        then:
        int totalSuccess = successCounts.sum()
        println "资源管理集成测试结果:"
        println "  创建限流器实例数: ${limiterCount}"
        println "  总成功请求数: ${totalSuccess}"
        println "  平均每个实例成功数: ${(totalSuccess / limiterCount).round(2)}"
        
        // 验证结果
        limiters.size() == limiterCount
        totalSuccess > 0
    }

    def "应该在高负载下保持正确性"() {
        given:
        rateLimiter = RateLimiter.create(200.0) // 每秒200个请求
        int threadCount = 50
        int requestsPerThread = 20
        CountDownLatch latch = new CountDownLatch(threadCount)
        AtomicInteger totalProcessed = new AtomicInteger(0)
        AtomicInteger totalSuccess = new AtomicInteger(0)
        ExecutorService executor = Executors.newFixedThreadPool(threadCount)

        when:
        long startTime = System.currentTimeMillis()
        
        // 高并发负载测试
        for (int i = 0; i < threadCount; i++) {
            executor.submit({
                try {
                    int processed = 0
                    int success = 0
                    
                    for (int j = 0; j < requestsPerThread; j++) {
                        processed++
                        if (rateLimiter.tryAcquire(50, TimeUnit.MILLISECONDS)) {
                            success++
                        }
                        
                        // 短暂休眠模拟真实场景
                        if (j % 5 == 0) {
                            Thread.sleep(5)
                        }
                    }
                    
                    totalProcessed.addAndGet(processed)
                    totalSuccess.addAndGet(success)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                } finally {
                    latch.countDown()
                }
            })
        }

        // 等待所有线程完成
        latch.await(60, TimeUnit.SECONDS)
        long endTime = System.currentTimeMillis()
        executor.shutdown()

        then:
        long duration = endTime - startTime
        int totalProc = totalProcessed.get()
        int totalSucc = totalSuccess.get()
        double throughput = totalSucc / (duration / 1000.0)
        double successRate = (totalSucc / (double) totalProc) * 100
        
        println "高负载集成测试结果:"
        println "  并发线程数: ${threadCount}"
        println "  每线程请求数: ${requestsPerThread}"
        println "  总处理请求数: ${totalProc}"
        println "  总成功请求数: ${totalSucc}"
        println "  总耗时: ${duration} ms"
        println "  吞吐量: ${throughput.round(2)} req/sec"
        println "  成功率: ${successRate.round(2)}%"
        
        // 验证结果
        totalProc == threadCount * requestsPerThread
        successRate > 80.0 // 成功率应该大于80%
        throughput > 0
    }

    def "应该正确处理边界条件"() {
        given:
        // 测试极高速率
        RateLimiter highRateLimiter = RateLimiter.create(10000.0) // 每秒10000个请求
        
        // 测试极低速率
        RateLimiter lowRateLimiter = RateLimiter.create(0.1) // 每10秒1个请求

        when:
        // 测试极高速率下的响应
        long highRateStartTime = System.nanoTime()
        boolean highRateSuccess = highRateLimiter.tryAcquire()
        long highRateEndTime = System.nanoTime()
        long highRateLatency = highRateEndTime - highRateStartTime
        
        // 测试极低速率下的行为
        boolean lowRateImmediate = lowRateLimiter.tryAcquire() // 立即返回
        Thread.sleep(1000) // 等待1秒
        boolean lowRateAfterWait = lowRateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS) // 等待获取

        then:
        println "边界条件集成测试结果:"
        println "  极高速率延迟: ${highRateLatency / 1000000} ms"
        println "  极低速率立即获取: ${lowRateImmediate}"
        println "  极低速率等待后获取: ${lowRateAfterWait}"
        
        // 验证结果
        highRateSuccess == true // 极高速率应该能立即获取
        highRateLatency < 10000000 // 延迟应该小于10毫秒（纳秒）
    }
}