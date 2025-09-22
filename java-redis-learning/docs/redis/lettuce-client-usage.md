# Lettuce Redis 客户端使用指南

## 概述

LettuceRedisClient 是一个基于 Lettuce 库封装的 Redis 客户端工具类，提供了简化的 Redis 操作接口。该工具类支持 Redis 的主要数据结构操作，包括 String、Hash、List、Set 等。

## 主要特性

1. **自动配置**: 基于 redis.properties 配置文件自动初始化连接
2. **连接管理**: 自动管理 Redis 连接的创建和关闭
3. **异常处理**: 提供统一的异常处理机制
4. **丰富接口**: 支持 Redis 的主要数据结构操作

## 使用方法

### 1. 添加依赖

确保在 pom.xml 中添加了 Lettuce 依赖：

```xml
<dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
    <version>6.2.6.RELEASE</version>
</dependency>
```

### 2. 配置文件

在 `src/main/resources/` 目录下创建 `redis.properties` 文件：

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
// 创建客户端实例
LettuceRedisClient redisClient = new LettuceRedisClient();

try {
    // String 操作
    redisClient.set("key", "value");
    String value = redisClient.get("key");
    
    // Hash 操作
    redisClient.hset("user", "name", "张三");
    String name = redisClient.hget("user", "name");
    
    // List 操作
    redisClient.lpush("tasks", "task1");
    String task = redisClient.lpop("tasks");
    
    // Set 操作
    redisClient.sadd("tags", "Java", "Redis");
    Set<String> tags = redisClient.smembers("tags");
    
} finally {
    // 关闭连接
    redisClient.close();
}
```

## API 参考

### String 操作

- `set(key, value)`: 设置字符串值
- `setex(key, value, expireSeconds)`: 设置字符串值并指定过期时间
- `get(key)`: 获取字符串值
- `del(key)`: 删除键
- `exists(key)`: 检查键是否存在
- `expire(key, seconds)`: 设置键的过期时间

### Hash 操作

- `hset(key, field, value)`: 设置哈希字段值
- `hget(key, field)`: 获取哈希字段值
- `hdel(key, fields...)`: 删除哈希字段
- `hgetall(key)`: 获取哈希的所有字段和值

### List 操作

- `lpush(key, value)`: 在列表左侧插入元素
- `rpush(key, value)`: 在列表右侧插入元素
- `lpop(key)`: 从列表左侧弹出元素
- `rpop(key)`: 从列表右侧弹出元素
- `lrange(key, start, stop)`: 获取列表指定范围的元素

### Set 操作

- `sadd(key, members...)`: 向集合添加元素
- `srem(key, members...)`: 从集合中移除元素
- `smembers(key)`: 获取集合所有元素
- `sismember(key, member)`: 检查元素是否在集合中

### 通用操作

- `keys(pattern)`: 获取所有匹配模式的键
- `flushdb()`: 清空当前数据库
- `isConnected()`: 检查连接是否有效
- `close()`: 关闭连接

## 注意事项

1. **连接管理**: 使用完毕后务必调用 `close()` 方法关闭连接
2. **异常处理**: 网络异常或Redis服务器未启动时会抛出 RuntimeException
3. **线程安全**: LettuceRedisClient 实例不是线程安全的，建议在多线程环境中使用连接池
4. **资源清理**: 建议使用 try-finally 或 try-with-resources 语句确保资源被正确释放

## 运行示例

要运行示例代码，请确保：

1. Redis 服务器正在运行（默认端口 6379）
2. 配置文件正确设置
3. 网络连接正常

运行示例：
```bash
mvn compile exec:java -Dexec.mainClass="com.example.redis.client.LettuceRedisClientExample"
```