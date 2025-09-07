# 给你一个亿Redis keys 统计双方的共同好友

## 问题分析

在处理统计双方共同好友的问题时，我们需要考虑以下几个关键问题和挑战：

1. **数据量巨大**：亿级Redis keys意味着需要处理大量的用户好友关系数据，对存储系统的容量和性能都提出了很高要求。

2. **计算复杂度高**：如果使用传统方式计算两个用户的交集，需要遍历两个用户的好友列表，时间复杂度较高。

3. **内存消耗**：如果为每对用户都维护一个交集结果，内存消耗会非常大。

4. **实时性要求**：需要快速计算并返回共同好友列表，在用户每次请求时快速响应。

5. **数据一致性**：在分布式环境下，如何保证好友关系数据的一致性和准确性。

## 解决方案架构

### 数据结构设计
- 使用Set数据结构存储每个用户的好友列表
- 每个用户对应一个Set key，key的命名规则为 `user:friends:{userId}`
- Set中的每个元素代表该用户的一个好友ID

### 核心思路
- 利用Redis的集合操作命令SINTER计算两个Set的交集
- 通过SINTERCARD获取交集的元素数量
- 使用SINTERSTORE将交集结果存储到新的key中以供后续使用

### 系统组件
- 好友关系服务：维护用户好友关系数据
- Redis集群：存储用户好友关系数据
- 共同好友计算服务：计算并返回用户间的共同好友

## 详细实现方案

### 1. 好友关系存储

使用Redis Set存储用户的好友关系：

```java
/**
 * 添加好友关系
 * @param userId 用户ID
 * @param friendId 好友ID
 */
public void addFriend(String userId, String friendId) {
    String key = "user:friends:" + userId;
    redisTemplate.opsForSet().add(key, friendId);
}

/**
 * 批量添加好友关系
 * @param userId 用户ID
 * @param friendIds 好友ID列表
 */
public void addFriends(String userId, List<String> friendIds) {
    String key = "user:friends:" + userId;
    redisTemplate.opsForSet().add(key, friendIds.toArray(new String[0]));
}
```

### 2. 计算共同好友

使用Redis的SINTER命令计算两个用户的交集：

```java
/**
 * 计算两个用户的共同好友
 * @param userId1 用户1ID
 * @param userId2 用户2ID
 * @return 共同好友列表
 */
public Set<String> getCommonFriends(String userId1, String userId2) {
    String key1 = "user:friends:" + userId1;
    String key2 = "user:friends:" + userId2;
    
    return redisTemplate.opsForSet().intersect(key1, key2);
}

/**
 * 计算多个用户的共同好友
 * @param userIds 用户ID列表
 * @return 共同好友列表
 */
public Set<String> getCommonFriends(List<String> userIds) {
    List<String> keys = new ArrayList<>();
    for (String userId : userIds) {
        keys.add("user:friends:" + userId);
    }
    
    return redisTemplate.opsForSet().intersect(keys);
}
```

### 3. 获取共同好友数量

使用SINTERCARD命令获取交集的元素数量，避免传输大量数据：

```java
/**
 * 获取两个用户的共同好友数量
 * @param userId1 用户1ID
 * @param userId2 用户2ID
 * @return 共同好友数量
 */
public Long getCommonFriendsCount(String userId1, String userId2) {
    String key1 = "user:friends:" + userId1;
    String key2 = "user:friends:" + userId2;
    
    return redisTemplate.opsForSet().intersectAndStore(
        Arrays.asList(key1, key2), 
        "temp:common:friends"
    );
}
```

### 4. 优化的共同好友计算

对于大数据量场景，使用Lua脚本优化计算过程：

```java
/**
 * 使用Lua脚本计算共同好友（优化版）
 * @param userId1 用户1ID
 * @param userId2 用户2ID
 * @return 共同好友列表
 */
public Set<String> getCommonFriendsOptimized(String userId1, String userId2) {
    String script = "local key1 = KEYS[1]\n" +
                   "local key2 = KEYS[2]\n" +
                   "local common_friends = redis.call('SINTER', key1, key2)\n" +
                   "return common_friends";
                   
    List<String> keys = Arrays.asList(
        "user:friends:" + userId1,
        "user:friends:" + userId2
    );
    
    return (Set<String>) redisTemplate.execute(
        new DefaultRedisScript<>(script, Set.class),
        keys
    );
}
```

## Redis数据结构选择和优化策略

### 1. 为什么选择Set

在处理共同好友统计的场景中，我们选择Set作为主要的数据结构，原因如下：

#### 优势：
- **天然的集合特性**：Set天然支持集合运算，如交集、并集、差集等操作
- **元素唯一性**：Set中的元素是唯一的，避免了重复好友关系
- **高效的集合操作**：Redis提供了丰富的集合操作命令，如SINTER、SUNION、SDIFF等
- **良好的扩展性**：Set可以轻松支持亿级用户的好友关系数据

#### 内存占用分析：
- 每个好友ID假设为10字节
- 平均每个用户有500个好友
- 每个用户好友关系占用约5KB内存
- 1亿用户总共需要约500GB内存

### 2. Set优化策略

#### 分片存储策略
为了进一步优化性能和避免单个key过大，可以采用分片存储策略：

```java
/**
 * 获取分片key
 * @param userId 用户ID
 * @param shardCount 分片数量
 * @return 分片key
 */
private String getShardKey(String userId, int shardCount) {
    // 根据用户ID的hash值分片
    int shardIndex = userId.hashCode() % shardCount;
    return "user:friends:" + userId + ":shard:" + shardIndex;
}
```

#### 压缩存储策略
对于好友数量特别多的用户，可以考虑使用Bitmap压缩存储：

```java
/**
 * 使用Bitmap压缩存储好友关系
 * @param userId 用户ID
 * @param friendId 好友ID
 */
public void addFriendCompressed(String userId, String friendId) {
    String key = "user:friends:bitmap:" + userId;
    // 使用好友ID的hash值作为offset
    long offset = Math.abs(friendId.hashCode());
    redisTemplate.opsForValue().setBit(key, offset, true);
}
```

### 3. Redis集群优化

#### Key分布优化
为了确保数据在Redis集群中均匀分布，避免热点问题：

1. **合理的key设计**：使用 `{user:friends}` 作为hash tag，确保同一用户的好友数据分布在同一个slot中。
2. **数据预分片**：根据用户ID的hash值将用户分布到不同的Redis实例中。

#### 连接池优化
针对高并发场景，需要合理配置Redis连接池：

```java
// Lettuce连接池配置示例
LettucePoolingClientConfiguration config = LettucePoolingClientConfiguration.builder()
    .poolConfig(new GenericObjectPoolConfig())
    .build();

// 连接池配置
GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
poolConfig.setMaxTotal(200);          // 最大连接数
poolConfig.setMaxIdle(50);            // 最大空闲连接数
poolConfig.setMinIdle(10);            // 最小空闲连接数
poolConfig.setMaxWaitMillis(2000);    // 获取连接的最大等待时间
```

### 4. 性能优化措施

#### Pipeline优化
对于批量操作场景，使用Pipeline减少网络开销：

```java
/**
 * 批量获取用户好友数量
 */
public Map<String, Long> batchGetFriendCounts(List<String> userIds) {
    Map<String, Long> result = new HashMap<>();
    
    // 使用Pipeline批量操作
    List<Object> responses = redisTemplate.executePipelined(new RedisCallback<Object>() {
        @Override
        public Object doInRedis(RedisConnection connection) throws DataAccessException {
            for (String userId : userIds) {
                String key = "user:friends:" + userId;
                connection.sCard(key.getBytes());
            }
            return null;
        }
    });
    
    // 处理结果
    for (int i = 0; i < userIds.size(); i++) {
        result.put(userIds.get(i), (Long) responses.get(i));
    }
    
    return result;
}
```

#### Lua脚本优化
将复杂的计算逻辑放在Lua脚本中执行，减少网络交互：

```lua
-- 计算共同好友的Lua脚本
local key1 = KEYS[1]
local key2 = KEYS[2]
local limit = tonumber(ARGV[1]) or 100

-- 获取交集
local common_friends = redis.call('SINTER', key1, key2)

-- 限制返回数量
if limit > 0 and #common_friends > limit then
    local limited_result = {}
    for i = 1, limit do
        table.insert(limited_result, common_friends[i])
    end
    return limited_result
end

return common_friends
```

## 5. 容灾和数据持久化

### 数据备份策略
- 定期执行BGSAVE进行RDB持久化
- 启用AOF持久化确保数据不丢失
- 建立主从复制架构提供高可用性

### 监控和告警
- 监控Redis内存使用情况
- 监控连接数和命令执行延迟
- 设置内存使用阈值告警
- 监控集合大小，防止某些用户好友数量过多

## 6. 性能对比分析

针对亿级用户共同好友统计场景，我们对比了不同的实现方案：

| 方案 | 时间复杂度 | 空间复杂度 | 网络开销 | 适用场景 |
|------|-----------|-----------|----------|----------|
| 客户端计算交集 | O(n+m) | O(n+m) | 高 | 小数据量 |
| Redis SINTER | O(n+m) | O(1) | 低 | 中等数据量 |
| Lua脚本优化 | O(n+m) | O(1) | 最低 | 大数据量、高并发 |

**推荐使用Redis SINTER或Lua脚本方案的原因：**
1. **最佳性能**：利用Redis原生集合操作，避免数据传输
2. **内存效率**：不需要在客户端存储好友列表
3. **原子性保障**：Redis操作具有原子性
4. **扩展性好**：支持分布式部署

## 7. 总结

通过使用Redis的Set数据结构结合集合操作命令，我们可以高效地处理亿级用户共同好友统计问题。该方案具有以下优势：

1. **计算效率高**：利用Redis原生集合操作，时间复杂度为O(n+m)
2. **内存效率好**：不需要在应用服务器存储好友列表
3. **扩展性强**：可以轻松支持亿级用户规模
4. **实现简单**：利用Redis原生命令，代码实现简洁

通过合理的优化策略，该方案可以稳定支撑大规模用户场景下的共同好友统计需求。