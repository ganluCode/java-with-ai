package com.redis.bloomfilter;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.Map;

/**
 * Lettuce适配器类，模拟StringRedisTemplate的部分功能
 */
public class LettuceRedisTemplate {
    
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> syncCommands;
    
    public LettuceRedisTemplate(String host, int port) {
        RedisURI redisURI = RedisURI.Builder
            .redis(host, port)
            .build();
        
        this.redisClient = RedisClient.create(redisURI);
        this.connection = redisClient.connect();
        this.syncCommands = connection.sync();
    }
    
    public LettuceRedisTemplate(RedisClient redisClient) {
        this.redisClient = redisClient;
        this.connection = redisClient.connect();
        this.syncCommands = connection.sync();
    }
    
    /**
     * 设置位图中的指定位
     * @param key 键
     * @param offset 偏移量
     * @param value 值
     * @return 操作前的位值
     */
    public boolean setBit(String key, long offset, boolean value) {
        // 直接调用方法
        Long result = syncCommands.setbit(key, offset, value ? 1 : 0);
        return result != null && result == 1L;
    }
    
    public boolean getBit(String key, long offset) {
        // 使用三元运算符处理类型转换
        Long result = syncCommands.getbit(key, offset);
        return result != null && result == 1L;
    }
    
    /**
     * 检查键是否存在
     * @param key 键
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        return syncCommands.exists(key) > 0;
    }
    
    /**
     * 删除键
     * @param key 键
     * @return 删除的键数量
     */
    public Long delete(String key) {
        return syncCommands.del(key);
    }
    
    /**
     * Hash操作：设置多个字段值
     * @param key 键
     * @param hash 键值对映射
     */
    public void opsForHashPutAll(String key, Map<String, String> hash) {
        syncCommands.hmset(key, hash);
    }
    
    /**
     * Hash操作：获取字段值
     * @param key 键
     * @param field 字段
     * @return 字段值
     */
    public Object opsForHashGet(String key, String field) {
        return syncCommands.hget(key, field);
    }
    
    /**
     * Hash操作：设置字段值
     * @param key 键
     * @param field 字段
     * @param value 值
     */
    public void opsForHashPut(String key, String field, String value) {
        syncCommands.hset(key, field, value);
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
    }
    
    /**
     * 获取同步命令对象
     * @return RedisCommands对象
     */
    public RedisCommands<String, String> getSyncCommands() {
        return syncCommands;
    }
}