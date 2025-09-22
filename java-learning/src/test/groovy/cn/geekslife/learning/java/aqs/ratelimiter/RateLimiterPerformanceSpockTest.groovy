package cn.geekslife.learning.java.aqs.ratelimiter

import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.TokenBucketAlgorithm
import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.SlidingWindowAlgorithm
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Ignore
import spock.lang.Unroll

import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * AQS限流器性能测试类
 */
class RateLimiterPerformanceSpockTest extends Specification {

    @Subject
    RateLimiter rateLimiter

    def "应该测量令牌桶算法的吞吐量"() {
        given:
        rateLimiter = RateLimiter.create(1000.0) // 每秒1000个请求
        int threadCount = 10
        int requestsPerThread = 100
        CountDownLatch latch = new CountDownLatch(threadCount)
        AtomicInteger totalSuccess = new AtomicInteger(0)
        AtomicLong totalTime = new AtomicLong(0)
        ExecutorService executor = Executors.newFixedThreadPool(threadCount)

        when:
        long startTime = System.currentTimeMillis()
        
        // 启动多个并发线程
        for (int i = 0; i < threadCount; i++) {
            executor.submit({
                try {
                    long threadStartTime = System.nanoTime()
                    int successCount = 0
                    
                    // 每个线程发送指定数量的请求
                    for (int j = 0; j < requestsPerThread; j++) {
                        if (rateLimiter.tryAcquire()) {
                            successCount++
                        }
                    }
                    
                    long threadEndTime = System.nanoTime()
                    totalSuccess.addAndGet(successCount)
                    totalTime.addAndGet(threadEndTime - threadStartTime)
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
        int totalRequests = threadCount * requestsPerThread
        int successCount = totalSuccess.get()
        double throughput = successCount / (duration / 1000.0) // 请求/秒
        
        println "性能测试结果:"
        println "  总请求数: ${totalRequests}"
        println "  成功请求数: ${successCount}"
        println "  总耗时: ${duration} ms"
        println "  平均吞吐量: ${throughput.round(2)} req/sec"
        println "  平均响应时间: ${(totalTime.get() / successCount / 1000000).round(2)} ms"
        
        successCount > 0
        throughput > 0
    }

    def "应该比较令牌桶和滑动窗口算法的性能"() {
        given:
        RateLimiter tokenBucketLimiter = RateLimiter.create(500.0) // 令牌桶算法
        RateLimiter slidingWindowLimiter = RateLimiter.createSlidingWindow(500, 1000) // 滑动窗口算法
        int requestCount = 1000

        when:
        // 测试令牌桶算法性能
        long tbStartTime = System.currentTimeMillis()
        int tbSuccess = 0
        for (int i = 0; i < requestCount; i++) {
            if (tokenBucketLimiter.tryAcquire()) {
                tbSuccess++
            }
        }
        long tbEndTime = System.currentTimeMillis()
        long tbDuration = tbEndTime - tbStartTime

        // 测试滑动窗口算法性能
        long swStartTime = System.currentTimeMillis()
        int swSuccess = 0
        for (int i = 0; i < requestCount; i++) {
            if (slidingWindowLimiter.tryAcquire()) {
                swSuccess++
            }
        }
        long swEndTime = System.currentTimeMillis()
        long swDuration = swEndTime - swStartTime

        then:
        println "算法性能对比:"
        println "  令牌桶算法 - 耗时: ${tbDuration} ms, 成功: ${tbSuccess}/${requestCount}"
        println "  滑动窗口算法 - 耗时: ${swDuration} ms, 成功: ${swSuccess}/${requestCount}"
        
        // 两个算法都应该工作正常
        tbSuccess > 0
        swSuccess > 0
    }

    def "应该在高并发下保持稳定性能"() {
        given:
        rateLimiter = RateLimiter.create(100.0) // 每秒100个请求
        int threadCount = 50
        int requestsPerThread = 50
        CountDownLatch latch = new CountDownLatch(threadCount)
        AtomicInteger totalSuccess = new AtomicInteger(0)
        AtomicInteger totalFailures = new AtomicInteger(0)
        ExecutorService executor = Executors.newFixedThreadPool(20)

        when:
        long startTime = System.currentTimeMillis()
        
        // 启动高并发线程
        for (int i = 0; i < threadCount; i++) {
            executor.submit({
                try {
                    int success = 0
                    int failures = 0
                    
                    // 每个线程发送请求
                    for (int j = 0; j < requestsPerThread; j++) {
                        // 尝试在10毫秒内获取令牌
                        if (rateLimiter.tryAcquire(10, TimeUnit.MILLISECONDS)) {
                            success++
                        } else {
                            failures++
                        }
                        
                        // 短暂休眠以模拟真实请求间隔
                        Thread.sleep(5)
                    }
                    
                    totalSuccess.addAndGet(success)
                    totalFailures.addAndGet(failures)
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
        int totalRequests = threadCount * requestsPerThread
        int successCount = totalSuccess.get()
        int failureCount = totalFailures.get()
        double successRate = (successCount / (double) totalRequests) * 100
        
        println "高并发性能测试结果:"
        println "  并发线程数: ${threadCount}"
        println "  每线程请求数: ${requestsPerThread}"
        println "  总请求数: ${totalRequests}"
        println "  成功请求数: ${successCount}"
        println "  失败请求数: ${failureCount}"
        println "  总耗时: ${duration} ms"
        println "  成功率: ${successRate.round(2)}%"
        println "  平均吞吐量: ${(successCount / (duration / 1000.0)).round(2)} req/sec"
        
        // 应该有较高的成功率
        successRate > 80.0
    }

    def "应该测量单次请求的延迟"() {
        given:
        rateLimiter = RateLimiter.create(1000.0) // 每秒1000个请求
        int sampleCount = 1000
        List<Long> latencies = new ArrayList<>(sampleCount)

        when:
        // 测量多次请求的延迟
        for (int i = 0; i < sampleCount; i++) {
            long startTime = System.nanoTime()
            rateLimiter.acquire() // 获取令牌
            long endTime = System.nanoTime()
            latencies.add(endTime - startTime)
        }

        then:
        // 计算延迟统计信息
        Collections.sort(latencies)
        long minLatency = latencies[0] / 1000 // 转换为微秒
        long maxLatency = latencies[latencies.size() - 1] / 1000 // 转换为微秒
        long avgLatency = latencies.sum() / latencies.size() / 1000 // 转换为微秒
        long p50Latency = latencies[latencies.size() / 2] / 1000 // 50%分位数
        long p95Latency = latencies[(int)(latencies.size() * 0.95)] / 1000 // 95%分位数
        long p99Latency = latencies[(int)(latencies.size() * 0.99)] / 1000 // 99%分位数
        
        println "延迟测量结果 (${sampleCount} 次采样):"
        println "  最小延迟: ${minLatency} μs"
        println "  最大延迟: ${maxLatency} μs"
        println "  平均延迟: ${avgLatency} μs"
        println "  50%分位数: ${p50Latency} μs"
        println "  95%分位数: ${p95Latency} μs"
        println "  99%分位数: ${p99Latency} μs"
        
        // 延迟应该在合理范围内
        avgLatency < 10000 // 平均延迟小于10毫秒
        p99Latency < 50000 // 99%分位数延迟小于50毫秒
    }

    def "应该在长时间运行下保持内存稳定"() {
        given:
        rateLimiter = RateLimiter.create(100.0) // 每秒100个请求
        int durationSeconds = 30 // 运行30秒
        long startTime = System.currentTimeMillis()
        long endTime = startTime + (durationSeconds * 1000)
        int requestCount = 0
        int successCount = 0

        when:
        // 长时间运行测试
        while (System.currentTimeMillis() < endTime) {
            requestCount++
            if (rateLimiter.tryAcquire(10, TimeUnit.MILLISECONDS)) {
                successCount++
            }
            
            // 控制请求频率
            if (requestCount % 10 == 0) {
                Thread.sleep(1)
            }
        }

        then:
        long actualDuration = System.currentTimeMillis() - startTime
        double actualRate = successCount / (actualDuration / 1000.0)
        
        println "长时间运行测试结果:"
        println "  目标时长: ${durationSeconds} 秒"
        println "  实际时长: ${actualDuration} ms"
        println "  总请求数: ${requestCount}"
        println "  成功请求数: ${successCount}"
        println "  实际速率: ${actualRate.round(2)} req/sec"
        
        // 应该成功处理大部分请求
        successCount > requestCount * 0.8
    }

    @Unroll
    def "应该在不同速率下保持良好性能 (#rate permits/sec)"() {
        given:
        rateLimiter = RateLimiter.create(rate)
        int requestCount = 500
        int successCount = 0

        when:
        long startTime = System.currentTimeMillis()
        for (int i = 0; i < requestCount; i++) {
            if (rateLimiter.tryAcquire(10, TimeUnit.MILLISECONDS)) {
                successCount++
            }
        }
        long endTime = System.currentTimeMillis()

        then:
        long duration = endTime - startTime
        double throughput = duration > 0 ? successCount / (duration / 1000.0) : successCount * 1000.0
        
        println "速率 ${rate} permits/sec - 成功: ${successCount}/${requestCount}, 吞吐量: ${throughput.round(2)} req/sec"
        
        successCount > 0

        where:
        rate << [10.0, 50.0, 100.0, 500.0, 1000.0]
    }

    def "应该在压力测试下保持系统稳定"() {
        given:
        rateLimiter = RateLimiter.create(1000.0) // 每秒1000个请求
        int burstSize = 100 // 突发请求数
        int burstInterval = 100 // 突发间隔(毫秒)
        int burstCount = 10 // 突发次数

        when:
        List<Long> burstTimes = new ArrayList<>()
        
        for (int i = 0; i < burstCount; i++) {
            long burstStartTime = System.currentTimeMillis()
            int burstSuccess = 0
            
            // 发送突发请求
            for (int j = 0; j < burstSize; j++) {
                if (rateLimiter.tryAcquire(1, TimeUnit.MILLISECONDS)) {
                    burstSuccess++
                }
            }
            
            long burstEndTime = System.currentTimeMillis()
            burstTimes.add(burstEndTime - burstStartTime)
            
            println "突发 ${i+1}: ${burstSuccess}/${burstSize} 成功, 耗时: ${burstEndTime - burstStartTime} ms"
            
            // 等待下次突发
            Thread.sleep(burstInterval)
        }

        then:
        // 计算平均突发处理时间
        double avgBurstTime = burstTimes.sum() / burstTimes.size()
        long minBurstTime = burstTimes.min()
        long maxBurstTime = burstTimes.max()
        
        println "压力测试统计:"
        println "  平均突发处理时间: ${avgBurstTime.round(2)} ms"
        println "  最短突发处理时间: ${minBurstTime} ms"
        println "  最长突发处理时间: ${maxBurstTime} ms"
        
        avgBurstTime < 50 // 平均突发处理时间应小于50毫秒
    }
}