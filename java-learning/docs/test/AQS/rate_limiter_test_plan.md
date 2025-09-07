# AQS限流器测试文档

## 1. 概述

本文档详细描述了AQS限流器的测试策略和测试用例设计，确保限流器实现的功能正确性和性能稳定性。

## 2. 测试目标

1. **功能正确性**：验证限流器的基本功能是否按预期工作
2. **并发安全性**：验证在高并发环境下限流器的线程安全性
3. **性能测试**：评估限流器在不同负载下的性能表现
4. **边界条件**：测试极端情况下的行为
5. **异常处理**：验证错误输入和异常情况的处理

## 3. 测试环境

- Java版本：17
- 测试框架：Spock 2.3 + Groovy 3.0.17
- 构建工具：Maven 3.x
- 操作系统：跨平台支持

## 4. 测试分类

### 4.1 单元测试
- 验证单个类和方法的功能
- 测试基本的获取和释放操作
- 验证算法实现的正确性

### 4.2 集成测试
- 验证多个组件协同工作
- 测试完整的限流流程
- 验证API接口的正确性

### 4.3 并发测试
- 验证多线程环境下的正确性
- 测试高并发场景下的性能
- 验证线程安全性和一致性

### 4.4 性能测试
- 测试不同负载下的响应时间
- 验证吞吐量和资源利用率
- 评估不同算法的性能差异

## 5. 测试用例设计

### 5.1 RateLimiter类测试

#### 5.1.1 创建限流器测试
```
Feature: 创建限流器
  Scenario: 使用令牌桶算法创建限流器
    Given 设置每秒允许10个请求
    When 调用RateLimiter.create(10.0)
    Then 返回AQSRateLimiter实例
    And 实例不为null
    And 速率设置正确

  Scenario: 使用滑动窗口算法创建限流器
    Given 设置窗口大小5秒，最大请求数50
    When 调用RateLimiter.createSlidingWindow(50, 5000)
    Then 返回AQSRateLimiter实例
    And 实例不为null
```

#### 5.1.2 获取令牌测试
```
Feature: 获取令牌
  Scenario: 成功获取令牌
    Given 创建令牌桶限流器，每秒允许5个请求
    When 调用acquire()方法
    Then 立即返回，等待时间为0

  Scenario: 需要等待获取令牌
    Given 创建令牌桶限流器，每秒允许1个请求
    And 已经连续获取5个令牌
    When 再次调用acquire()方法
    Then 等待约1秒后返回

  Scenario: 尝试获取令牌成功
    Given 创建令牌桶限流器，每秒允许5个请求
    When 调用tryAcquire()方法
    Then 返回true

  Scenario: 尝试获取令牌失败
    Given 创建令牌桶限流器，每秒允许1个请求
    And 已经连续获取5个令牌且未等待
    When 调用tryAcquire()方法
    Then 返回false
```

#### 5.1.3 速率控制测试
```
Feature: 速率控制
  Scenario: 设置新的速率
    Given 创建令牌桶限流器，初始速率每秒5个请求
    When 调用setRate(10.0)方法
    Then 新速率为每秒10个请求

  Scenario: 获取当前速率
    Given 创建令牌桶限流器，设置速率为每秒7个请求
    When 调用getRate()方法
    Then 返回7.0
```

### 5.2 TokenBucketAlgorithm类测试

#### 5.2.1 令牌生成测试
```
Feature: 令牌生成
  Scenario: 令牌随时间生成
    Given 创建令牌桶算法，每秒生成2个令牌
    And 初始令牌数为0
    When 等待2秒
    Then 令牌数约为4个

  Scenario: 令牌数不超过最大值
    Given 创建令牌桶算法，每秒生成1个令牌，最大令牌数为5
    And 初始令牌数为5
    When 等待10秒
    Then 令牌数仍为5
```

#### 5.2.2 令牌消耗测试
```
Feature: 令牌消耗
  Scenario: 消耗令牌
    Given 创建令牌桶算法，每秒生成1个令牌
    And 初始有3个令牌
    When 消耗2个令牌
    Then 剩余1个令牌

  Scenario: 令牌不足时需要等待
    Given 创建令牌桶算法，每秒生成1个令牌
    And 初始令牌数为0
    When 消耗1个令牌
    Then 需要等待1秒
```

### 5.3 SlidingWindowAlgorithm类测试

#### 5.3.1 窗口管理测试
```
Feature: 窗口管理
  Scenario: 请求在窗口内
    Given 创建滑动窗口算法，窗口大小5秒，最大请求数10
    And 当前时间戳为10000毫秒
    When 添加3个请求，时间戳分别为9000, 9500, 9800毫秒
    Then 所有请求都在窗口内

  Scenario: 请求超出窗口
    Given 创建滑动窗口算法，窗口大小5秒，最大请求数10
    And 当前时间戳为10000毫秒
    When 添加请求，时间戳为4000毫秒
    Then 请求被清理出窗口
```

#### 5.3.2 限流控制测试
```
Feature: 限流控制
  Scenario: 达到限流阈值
    Given 创建滑动窗口算法，窗口大小5秒，最大请求数3
    When 在同一窗口内添加4个请求
    Then 第4个请求被拒绝

  Scenario: 窗口滑动后允许新请求
    Given 创建滑动窗口算法，窗口大小5秒，最大请求数3
    And 在时间戳1000添加3个请求
    When 时间前进到6000毫秒
    And 添加新请求
    Then 新请求被允许
```

### 5.4 AQSRateLimiter类测试

#### 5.4.1 AQS集成测试
```
Feature: AQS集成
  Scenario: 多线程等待和唤醒
    Given 创建AQS限流器，每秒允许1个请求
    And 5个线程同时请求令牌
    When 执行获取操作
    Then 只有1个线程立即获得令牌
    And 其他4个线程进入等待队列
    And 随着时间推移，线程依次被唤醒

  Scenario: 线程中断处理
    Given 创建AQS限流器，每秒允许1个请求
    And 线程在等待获取令牌
    When 中断等待线程
    Then 线程抛出InterruptedException
```

#### 5.4.2 算法切换测试
```
Feature: 算法切换
  Scenario: 使用令牌桶算法
    Given 创建AQS限流器，使用令牌桶算法实现
    When 执行获取操作
    Then 按照令牌桶算法进行限流

  Scenario: 使用滑动窗口算法
    Given 创建AQS限流器，使用滑动窗口算法实现
    When 执行获取操作
    Then 按照滑动窗口算法进行限流
```

### 5.5 并发测试

#### 5.5.1 高并发获取测试
```
Feature: 高并发获取
  Scenario: 100个并发请求
    Given 创建令牌桶限流器，每秒允许50个请求
    When 100个线程同时请求令牌
    Then 前50个请求立即获得令牌
    And 后50个请求等待适当时间后获得令牌
    And 所有请求最终都能获得令牌

  Scenario: 不同优先级请求
    Given 创建令牌桶限流器，每秒允许10个请求
    When 高优先级和低优先级请求混合
    Then 按照FIFO顺序处理请求
```

#### 5.5.2 长时间运行测试
```
Feature: 长时间运行
  Scenario: 持续1小时的负载测试
    Given 创建令牌桶限流器，每秒允许100个请求
    When 持续1小时每秒发送100个请求
    Then 系统稳定运行
    And 内存使用正常
    And 响应时间稳定
```

## 6. 性能基准测试

### 6.1 吞吐量测试
```
Feature: 吞吐量测试
  Scenario: 不同速率下的吞吐量
    Given 不同配置的令牌桶限流器
    When 发送大量请求
    Then 记录每秒处理请求数
    And 验证符合设定的速率限制
```

### 6.2 延迟测试
```
Feature: 延迟测试
  Scenario: 单次获取延迟
    Given 创建令牌桶限流器
    When 测量单次acquire()调用时间
    Then 平均延迟小于1毫秒

  Scenario: 批量获取延迟
    Given 创建令牌桶限流器
    When 连续调用多次acquire()方法
    Then 测量整体延迟分布
```

## 7. 边界条件测试

### 7.1 极端参数测试
```
Feature: 极端参数
  Scenario: 零速率
    Given 速率设置为0
    When 创建限流器
    Then 抛出IllegalArgumentException

  Scenario: 极高速率
    Given 速率设置为1000000
    When 创建限流器并发送请求
    Then 系统能正确处理高频请求
```

### 7.2 异常情况测试
```
Feature: 异常情况
  Scenario: 负数令牌请求
    Given 创建限流器
    When 请求-1个令牌
    Then 抛出IllegalArgumentException

  Scenario: 线程中断
    Given 线程在等待获取令牌
    When 中断线程
    Then 正确处理InterruptedException
```

## 8. 测试报告

### 8.1 覆盖率报告
- 代码行覆盖率目标：≥ 80%
- 分支覆盖率目标：≥ 75%
- 方法覆盖率目标：≥ 85%

### 8.2 性能报告
- 平均响应时间：< 1毫秒
- 95%响应时间：< 5毫秒
- 吞吐量：≥ 10000 req/sec

### 8.3 稳定性报告
- 连续运行时间：≥ 24小时
- 内存泄漏检测：无明显增长趋势
- 错误率：< 0.01%

## 9. 测试执行计划

### 9.1 自动化测试
- 每次代码提交触发单元测试
- 每日定时执行完整测试套件
- 每周执行性能基准测试

### 9.2 手动测试
- 新功能发布前的手工验证
- 回归测试
- 兼容性测试

## 10. 测试工具和框架

### 10.1 主要工具
- **Spock**: BDD测试框架
- **JUnit 5**: 基础测试支持
- **Mockito**: Mock对象框架
- **JaCoCo**: 代码覆盖率工具

### 10.2 辅助工具
- **JMeter**: 性能测试
- **Gatling**: 负载测试
- **VisualVM**: 性能监控

## 11. 测试维护

### 11.1 测试代码维护
- 随产品代码同步更新
- 定期审查测试用例的有效性
- 移除过时的测试

### 11.2 测试环境维护
- 保持测试环境与生产环境一致性
- 定期更新测试工具版本
- 维护测试数据的准确性