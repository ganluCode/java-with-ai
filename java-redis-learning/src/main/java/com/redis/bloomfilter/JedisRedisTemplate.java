package com.redis.bloomfilter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Map;

/**
 * Jedis适配器类，模拟StringRedisTemplate的部分功能
 */
public class JedisRedisTemplate {
    
    private JedisPool jedisPool;
    
    public JedisRedisTemplate(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
    
    /**
     * 设置位图中的指定位
     * @param key 键
     * @param offset 偏移量
     * @param value 值
     * @return 操作前的位值
     */
    public Boolean setBit(String key, long offset, boolean value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.setbit(key, offset, value);
        }
    }
    
    /**
     * 获取位图中的指定位
     * @param key 键
     * @param offset 偏移量
     * @return 位值
     */
    public Boolean getBit(String key, long offset) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.getbit(key, offset);
        }
    }
    
    /**
     * 检查键是否存在
     * @param key 键
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        }
    }
    
    /**
     * 删除键
     * @param key 键
     * @return 删除的键数量
     */
    public Long delete(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key);
        }
    }
    
    /**
     * Hash操作：设置多个字段值
     * @param key 键
     * @param hash 键值对映射
     */
    public void opsForHashPutAll(String key, Map<String, String> hash) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, hash);
        }
    }
    
    /**
     * Hash操作：获取字段值
     * @param key 键
     * @param field 字段
     * @return 字段值
     */
    public Object opsForHashGet(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, field);
        }
    }
    
    /**
     * Hash操作：设置字段值
     * @param key 键
     * @param field 字段
     * @param value 值
     */
    public void opsForHashPut(String key, String field, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, field, value);
        }
    }
}