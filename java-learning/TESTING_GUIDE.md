# AQS限流器测试指南

## 测试结构

本项目使用Spock测试框架编写了全面的测试套件，涵盖单元测试、集成测试和性能测试。

### 测试类别

1. **单元测试** - 针对单个类和方法的功能测试
   - `RateLimiterSpockTest` - RateLimiter类测试
   - `AQSRateLimiterSpockTest` - AQSRateLimiter类测试
   - `TokenBucketAlgorithmSpockTest` - 令牌桶算法测试
   - `SlidingWindowAlgorithmSpockTest` - 滑动窗口算法测试

2. **集成测试** - 验证多个组件协同工作的测试
   - `RateLimiterIntegrationSpockTest` - 系统集成测试

3. **性能测试** - 评估系统性能和稳定性的测试
   - `RateLimiterPerformanceSpockTest` - 性能基准测试

4. **测试套件** - 组织和运行所有测试
   - `RateLimiterTestSuite` - 完整测试套件

## 运行测试

### 运行所有测试

```bash
# 运行所有测试
mvn test

# 或者使用Maven Surefire插件
mvn surefire:test
```

### 运行特定测试类

```bash
# 运行单个测试类
mvn test -Dtest=RateLimiterSpockTest

# 运行测试套件
mvn test -Dtest=RateLimiterTestSuite

# 运行带有特定标签的测试
mvn test -Dgroups="performance"
```

### 运行测试并生成覆盖率报告

```bash
# 运行测试并生成JaCoCo覆盖率报告
mvn clean test jacoco:report

# 打开HTML覆盖率报告
open target/site/jacoco/index.html
```

## 测试环境要求

- Java 17或更高版本
- Maven 3.x
- Groovy 3.0.17（用于Spock测试）
- Spock 2.3

## 测试配置

测试配置在`pom.xml`中定义，包括：

1. **测试依赖**：
   - Spock Framework 2.3
   - JUnit Jupiter 5.8.1
   - Mockito 5.7.0

2. **测试插件**：
   - Maven Surefire Plugin
   - GMavenPlus Plugin（用于Groovy编译）
   - JaCoCo Plugin（用于代码覆盖率）

## 测试编写规范

### Spock测试结构

使用Spock框架的BDD（行为驱动开发）风格：

```groovy
def "应该在特定条件下执行特定行为"() {
    given:
    // 准备测试条件
    
    when:
    // 执行被测试的行为
    
    then:
    // 验证期望的结果
}
```

### 测试命名约定

- 使用中文描述性名称
- 遵循"应该+动词+条件"的格式
- 确保测试名称能够清晰表达测试意图

### 测试数据管理

- 使用`@Unroll`注解进行参数化测试
- 使用`where:`块定义测试数据
- 保持测试数据的可读性和维护性

## 性能测试说明

性能测试类(`RateLimiterPerformanceSpockTest`)包含以下测试场景：

1. **吞吐量测试** - 测量系统在不同负载下的处理能力
2. **延迟测试** - 测量单次请求的响应时间
3. **并发测试** - 验证系统在高并发环境下的稳定性
4. **长时间运行测试** - 验证系统在长时间运行下的资源管理

## 测试报告

测试执行后会生成以下报告：

1. **JUnit测试报告** - 位于`target/surefire-reports/`
2. **JaCoCo覆盖率报告** - 位于`target/site/jacoco/`
3. **测试执行摘要** - 在控制台输出

## 最佳实践

### 1. 测试隔离
- 每个测试方法应该是独立的
- 避免测试间的依赖关系
- 使用适当的setup和teardown方法

### 2. 断言原则
- 使用明确的断言消息
- 避免过度复杂的断言逻辑
- 优先使用Spock的内置断言

### 3. 性能考虑
- 避免在测试中使用过长的等待时间
- 合理控制并发测试的线程数量
- 及时清理测试资源

### 4. 可维护性
- 保持测试代码的简洁性
- 使用有意义的变量名称
- 定期审查和更新测试用例

## 常见问题

### 1. 测试执行缓慢
```bash
# 跳过测试运行
mvn install -DskipTests

# 或者跳过测试编译
mvn install -Dmaven.test.skip=true
```

### 2. Groovy编译错误
确保GMavenPlus插件正确配置，并且Groovy版本与Spock版本兼容。

### 3. 并发测试失败
检查测试中的线程同步机制，确保共享资源的正确访问。

## 贡献测试

欢迎贡献新的测试用例：

1. Fork项目
2. 创建新的测试分支
3. 编写测试用例
4. 运行所有测试确保兼容性
5. 提交Pull Request