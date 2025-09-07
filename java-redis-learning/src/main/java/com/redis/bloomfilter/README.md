# Redis布隆过滤器实现

## 简介

这是一个基于Redis和Lettuce客户端实现的布隆过滤器。布隆过滤器是一种空间效率很高的概率型数据结构，用于判断一个元素是否在一个集合中。

## 特性

- 基于Redis Bitmap实现，内存效率高
- 支持动态扩容
- 提供高效的添加和查询操作
- 内置重试机制，提高稳定性
- 可配置的误判率

## 核心类

### RedisBloomFilter
主要的布隆过滤器实现类，提供以下功能：

1. **add(item)** - 添加元素
2. **mightContain(item)** - 查询元素是否存在
3. **addAll(items)** - 批量添加元素
4. **mightContainAll(items)** - 批量查询元素
5. **getInfo()** - 获取过滤器信息
6. **clear()** - 清空过滤器

### LettuceRedisTemplate
Redis操作适配器类，封装了Lettuce客户端的操作方法。

## 使用方法

### 1. 创建布隆过滤器实例

```java
// 创建布隆过滤器实例（推荐方式）
// 参数: 名称, 预期容量, 误判率, Redis主机, Redis端口
RedisBloomFilter bloomFilter = new RedisBloomFilter("my_filter", 1000000, 0.01, "localhost", 6379);

// 或者使用LettuceRedisTemplate
LettuceRedisTemplate lettuceTemplate = new LettuceRedisTemplate("localhost", 6379);
RedisBloomFilter bloomFilter2 = new RedisBloomFilter("my_filter2", 1000000, 0.01, lettuceTemplate);
```

### 2. 添加元素

```java
boolean added = bloomFilter.add("user_12345");
```

### 3. 查询元素

```java
boolean mightExist = bloomFilter.mightContain("user_12345");
```

### 4. 批量操作

```java
// 批量添加
String[] users = {"user_1001", "user_1002", "user_1003"};
boolean[] results = bloomFilter.addAll(users);

// 批量查询
boolean[] existResults = bloomFilter.mightContainAll(users);
```

## 设计原理

### 参数计算
- 位图大小: m = -(n * ln(p)) / (ln(2)^2)
- 哈希函数个数: k = (m/n) * ln(2)

其中:
- n: 预期元素数量
- p: 误判率

### 哈希算法
使用双重哈希法生成多个哈希值:
```
hash_i = (hash1 + i * hash2) % bitmap_size
```

## 注意事项

1. 布隆过滤器存在一定的误判率
2. 不支持删除元素操作
3. 需要Redis服务正常运行
4. 建议根据实际需求合理设置容量和误判率

## 运行演示

```bash
# 确保Redis服务正在运行
redis-server

# 运行演示程序
java com.redis.bloomfilter.BloomFilterDemo
```