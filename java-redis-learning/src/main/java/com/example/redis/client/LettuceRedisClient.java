package com.example.redis.client;

import com.example.redis.config.RedisConfig;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.KeyScanArgs;
import io.lettuce.core.ScanIterator;
import io.lettuce.core.KeyScanCursor;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Lettuce Redis客户端工具类
 * 提供基本的Redis连接和操作功能
 */
public class LettuceRedisClient {
    
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> syncCommands;
    private RedisConfig config;
    
    /**
     * 构造函数，初始化Redis连接
     */
    public LettuceRedisClient() {
        this.config = new RedisConfig();
        initConnection();
    }
    
    /**
     * 初始化Redis连接
     */
    private void initConnection() {
        try {
            // 创建Redis URI
            RedisURI redisURI = RedisURI.Builder
                .redis(config.getHost(), config.getPort())
                .withDatabase(config.getDatabase())
                .withTimeout(java.time.Duration.ofMillis(config.getTimeout()))
                .build();
            
            // 如果配置了密码，则设置密码
            if (config.getPassword() != null && !config.getPassword().isEmpty()) {
                redisURI.setPassword(config.getPassword());
            }
            
            // 创建Redis客户端
            redisClient = RedisClient.create(redisURI);
            
            // 建立连接
            connection = redisClient.connect();
            
            // 获取同步命令对象
            syncCommands = connection.sync();
            
            System.out.println("Lettuce Redis客户端连接成功");
        } catch (Exception e) {
            System.err.println("Lettuce Redis客户端连接失败: " + e.getMessage());
            throw new RuntimeException("Failed to connect to Redis", e);
        }
    }
    
    /**
     * 获取同步命令对象
     * @return RedisCommands对象
     */
    public RedisCommands<String, String> getSyncCommands() {
        return syncCommands;
    }
    
    /**
     * 关闭连接
     */
    public void close() {
        if (connection != null) {
            connection.close();
        }
        if (redisClient != null) {
            redisClient.shutdown();
        }
        System.out.println("Lettuce Redis客户端连接已关闭");
    }
    
    /**
     * 测试连接是否有效
     * @return 连接状态
     */
    public boolean isConnected() {
        try {
            return connection.isOpen();
        } catch (Exception e) {
            return false;
        }
    }
    
    // ==================== String 操作 ====================
    
    /**
     * 设置字符串值
     * @param key 键
     * @param value 值
     * @return 设置结果
     */
    public String set(String key, String value) {
        return syncCommands.set(key, value);
    }
    
    /**
     * 设置字符串值并指定过期时间
     * @param key 键
     * @param value 值
     * @param expireSeconds 过期时间（秒）
     * @return 设置结果
     */
    public String setex(String key, String value, long expireSeconds) {
        return syncCommands.setex(key, expireSeconds, value);
    }
    
    /**
     * 获取字符串值
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return syncCommands.get(key);
    }
    
    /**
     * 删除键
     * @param key 键
     * @return 删除的键数量
     */
    public Long del(String key) {
        return syncCommands.del(key);
    }
    
    /**
     * 检查键是否存在
     * @param key 键
     * @return 是否存在
     */
    public Boolean exists(String key) {
        return syncCommands.exists(key) > 0;
    }
    
    /**
     * 设置键的过期时间
     * @param key 键
     * @param seconds 过期时间（秒）
     * @return 设置结果
     */
    public Boolean expire(String key, long seconds) {
        return syncCommands.expire(key, seconds);
    }
    
    // ==================== Hash 操作 ====================
    
    /**
     * 设置哈希字段值
     * @param key 哈希键
     * @param field 字段名
     * @param value 值
     * @return 设置结果
     */
    public Boolean hset(String key, String field, String value) {
        return syncCommands.hset(key, field, value);
    }
    
    /**
     * 获取哈希字段值
     * @param key 哈希键
     * @param field 字段名
     * @return 值
     */
    public String hget(String key, String field) {
        return syncCommands.hget(key, field);
    }
    
    /**
     * 删除哈希字段
     * @param key 哈希键
     * @param fields 字段名数组
     * @return 删除的字段数量
     */
    public Long hdel(String key, String... fields) {
        return syncCommands.hdel(key, fields);
    }
    
    /**
     * 获取哈希的所有字段和值
     * @param key 哈希键
     * @return 字段和值的映射
     */
    public java.util.Map<String, String> hgetall(String key) {
        return syncCommands.hgetall(key);
    }
    
    // ==================== List 操作 ====================
    
    /**
     * 在列表左侧插入元素
     * @param key 列表键
     * @param value 值
     * @return 插入后的列表长度
     */
    public Long lpush(String key, String value) {
        return syncCommands.lpush(key, value);
    }
    
    /**
     * 在列表右侧插入元素
     * @param key 列表键
     * @param value 值
     * @return 插入后的列表长度
     */
    public Long rpush(String key, String value) {
        return syncCommands.rpush(key, value);
    }
    
    /**
     * 从列表左侧弹出元素
     * @param key 列表键
     * @return 元素值
     */
    public String lpop(String key) {
        return syncCommands.lpop(key);
    }
    
    /**
     * 从列表右侧弹出元素
     * @param key 列表键
     * @return 元素值
     */
    public String rpop(String key) {
        return syncCommands.rpop(key);
    }
    
    /**
     * 获取列表指定范围的元素
     * @param key 列表键
     * @param start 起始索引
     * @param stop 结束索引
     * @return 元素列表
     */
    public List<String> lrange(String key, long start, long stop) {
        return syncCommands.lrange(key, start, stop);
    }
    
    // ==================== Set 操作 ====================
    
    /**
     * 向集合添加元素
     * @param key 集合键
     * @param members 元素数组
     * @return 添加的元素数量
     */
    public Long sadd(String key, String... members) {
        return syncCommands.sadd(key, members);
    }
    
    /**
     * 从集合中移除元素
     * @param key 集合键
     * @param members 元素数组
     * @return 移除的元素数量
     */
    public Long srem(String key, String... members) {
        return syncCommands.srem(key, members);
    }
    
    /**
     * 获取集合所有元素
     * @param key 集合键
     * @return 元素集合
     */
    public Set<String> smembers(String key) {
        return new HashSet<>(syncCommands.smembers(key));
    }
    
    /**
     * 检查元素是否在集合中
     * @param key 集合键
     * @param member 元素
     * @return 是否存在
     */
    public Boolean sismember(String key, String member) {
        return syncCommands.sismember(key, member);
    }
    
    // ==================== 通用操作 ====================
    
    /**
     * 获取所有匹配模式的键
     * @param pattern 匹配模式
     * @return 键集合
     */
    public Set<String> keys(String pattern) {
        Set<String> result = new HashSet<>();
        ScanIterator<String> iterator = ScanIterator.scan(syncCommands, KeyScanArgs.Builder.matches(pattern));
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }
    
    /**
     * 清空当前数据库
     * @return 清空结果
     */
    public String flushdb() {
        return syncCommands.flushdb();
    }
}