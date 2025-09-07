# Redis分布式锁

基于Redis和AQS(AbstractQueuedSynchronizer)实现的高性能分布式锁。

## 特性

- **高可用性**: 基于Redis的分布式特性，确保在分布式环境中的可用性
- **可重入性**: 支持同一线程多次获取同一把锁
- **自动续期**: 支持锁的自动续期，防止业务执行时间过长导致锁失效
- **公平性**: 支持公平锁和非公平锁两种模式
- **高性能**: 基于Lettuce的异步特性，提供高性能的锁操作
- **容错性**: 完善的异常处理机制，确保系统稳定性

## 快速开始

### 1. 添加依赖

在`pom.xml`中添加以下依赖：

```xml
<dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
    <version>6.2.6.RELEASE</version>
</dependency>
```

### 2. 配置Redis连接

在`src/main/resources/redis.properties`中配置Redis连接信息：

```properties
# Redis Configuration
redis.host=localhost
redis.port=6379
redis.database=0
redis.password=
redis.timeout=2000
```

### 3. 基本使用

```java
import com.example.redis.lock.RedisDistributedLock;
import com.example.redis.lock.LockConfig;

// 方式1: 简单使用
RedisDistributedLock lock = new RedisDistributedLock("myLock");
lock.lock();
try {
    // 执行业务逻辑
    doSomething();
} finally {
    lock.unlock();
}

// 方式2: 带超时的锁获取
if (lock.tryLock(5, TimeUnit.SECONDS)) {
    try {
        // 执行业务逻辑
        doSomething();
    } finally {
        lock.unlock();
    }
} else {
    // 获取锁失败处理
    handleLockFailure();
}

// 方式3: 配置化使用
LockConfig config = LockConfig.builder()
    .lockKey("myLock")
    .leaseTime(30)
    .renewalTime(10)
    .fair(true)
    .build();

RedisDistributedLock lock = new RedisDistributedLock(config);
```

## 核心组件

### RedisDistributedLock
核心锁实现类，继承自AQS，提供完整的分布式锁功能。

### LockConfig
锁配置类，支持构建者模式配置锁参数。

### LettuceClient
基于Lettuce的Redis客户端封装，提供原子性的锁操作。

### LockManager
锁管理器，负责锁的生命周期管理。

### RenewalManager
自动续期管理器，负责锁的自动续期任务调度。

## 使用场景

### 1. 电商秒杀
```java
// 商品库存扣减
AtomicInteger stock = new AtomicInteger(100);
RedisDistributedLock lock = new RedisDistributedLock("seckill:product:1001");

if (lock.tryLock(3, TimeUnit.SECONDS)) {
    try {
        if (stock.get() > 0) {
            stock.decrementAndGet();
            // 处理订单
        }
    } finally {
        lock.unlock();
    }
}
```

### 2. 分布式任务调度
```java
// 确保只有一个实例执行初始化任务
RedisDistributedLock lock = new RedisDistributedLock("scheduler:init:task");

if (lock.tryLock(5, TimeUnit.SECONDS)) {
    try {
        // 执行初始化任务
        initializeSystem();
    } finally {
        lock.unlock();
    }
}
```

### 3. 缓存更新
```java
// 防止缓存击穿
RedisDistributedLock lock = new RedisDistributedLock("cache:update:user:1001");

if (lock.tryLock(2, TimeUnit.SECONDS)) {
    try {
        // 查询数据库更新缓存
        updateUserCache();
    } finally {
        lock.unlock();
    }
}
```

## 配置说明

### LockConfig参数

| 参数 | 说明 | 默认值 |
|------|------|--------|
| lockKey | 锁的键名 | "defaultLock" |
| leaseTime | 锁的过期时间（秒） | 30 |
| renewalTime | 自动续期时间间隔（秒） | 10 |
| fair | 是否公平锁 | false |

### Redis配置参数

| 参数 | 说明 | 默认值 |
|------|------|--------|
| redis.host | Redis服务器地址 | "localhost" |
| redis.port | Redis服务器端口 | 6379 |
| redis.database | Redis数据库索引 | 0 |
| redis.password | Redis密码 | "" |
| redis.timeout | 连接超时时间（毫秒） | 2000 |

## 性能优化

1. **连接池**: LettuceClient内部使用连接池管理Redis连接
2. **异步操作**: 基于Lettuce的异步特性减少线程阻塞
3. **Lua脚本**: 使用Lua脚本保证Redis操作的原子性
4. **自动续期**: 智能的自动续期机制避免频繁的Redis操作

## 异常处理

系统提供完善的异常处理机制：

1. **网络异常**: 自动重试和恢复机制
2. **Redis异常**: 故障转移和数据一致性保障
3. **业务异常**: 确保锁的正确释放

## 测试

项目包含完整的单元测试和集成测试：

```bash
# 运行单元测试
mvn test

# 运行集成测试（需要Redis服务器）
mvn test -Dtest=*IntegrationTest
```

## 注意事项

1. 确保Redis服务器正常运行
2. 合理设置锁的过期时间和续期时间
3. 在finally块中释放锁，确保锁不会因为异常而无法释放
4. 避免长时间持有锁，影响系统性能
5. 在分布式环境中，确保所有节点的系统时间同步