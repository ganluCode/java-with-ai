# Java AQS限流器实现

## 项目概述

本项目实现了一个基于AQS(AbstractQueuedSynchronizer)的高并发限流器，支持多种限流算法：

1. **令牌桶算法** - 以固定速率生成令牌，请求消耗令牌
2. **滑动窗口算法** - 限制时间窗口内的请求数量

## 功能特性

- 高并发支持：基于AQS实现线程安全
- 多种限流算法：支持令牌桶和滑动窗口算法
- 灵活配置：可动态调整限流参数
- 多种获取模式：支持阻塞、非阻塞和超时获取
- 算法可扩展：通过接口抽象，易于添加新算法

## 核心类结构

```
RateLimiter (抽象类)
├── AQSRateLimiter (基于AQS的实现)
│
RateLimiterAlgorithm (接口)
├── TokenBucketAlgorithm (令牌桶算法)
└── SlidingWindowAlgorithm (滑动窗口算法)
```

## 使用示例

### 令牌桶算法
```java
// 创建令牌桶限流器，每秒允许10个请求
RateLimiter rateLimiter = RateLimiter.create(10.0);

// 获取一个令牌，可能会阻塞
double waitTime = rateLimiter.acquire();

// 尝试获取一个令牌，不阻塞
boolean acquired = rateLimiter.tryAcquire();

// 尝试在指定时间内获取令牌
boolean acquired = rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS);
```

### 滑动窗口算法
```java
// 创建滑动窗口限流器，10秒内最多允许50个请求
RateLimiter rateLimiter = RateLimiter.createSlidingWindow(50, 10000);
```

## 运行演示

```bash
# 编译项目
mvn compile

# 编译测试
mvn test-compile

# 运行演示
mvn exec:java -Dexec.mainClass="cn.geekslife.learning.java.aqs.ratelimiter.RateLimiterDemo" -Dexec.classpathScope=test
```

## 设计文档

详细的设计文档请参考：[限流器设计文档](docs/design/AQS/rate_limiter_design.md)