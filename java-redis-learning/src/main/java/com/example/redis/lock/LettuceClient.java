package com.example.redis.lock;

import com.example.redis.config.RedisConfig;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.RedisPubSubAdapter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * 基于Lettuce的Redis客户端封装
 * 支持发布/订阅机制的增强版本
 */
public class LettuceClient {
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> syncCommands;
    private StatefulRedisPubSubConnection<String, String> pubSubConnection;
    private RedisConfig redisConfig;
    private final Map<String, RedisPubSubAdapter<String, String>> subscribers = new ConcurrentHashMap<>();
    
    public LettuceClient() {
        this.redisConfig = new RedisConfig();
        initConnection();
    }
    
    /**
     * 初始化Redis连接
     */
    private void initConnection() {
        try {
            // 创建Redis URI
            RedisURI.Builder uriBuilder = RedisURI.Builder
                .redis(redisConfig.getHost(), redisConfig.getPort())
                .withDatabase(redisConfig.getDatabase())
                .withTimeout(java.time.Duration.ofMillis(redisConfig.getTimeout()));
            
            // 如果有密码，设置密码
            if (redisConfig.getPassword() != null && !redisConfig.getPassword().isEmpty()) {
                uriBuilder.withPassword(redisConfig.getPassword().toCharArray());
            }
            
            RedisURI redisURI = uriBuilder.build();
            
            // 创建Redis客户端
            redisClient = RedisClient.create(redisURI);
            
            // 建立连接
            connection = redisClient.connect();
            
            // 获取同步命令对象
            syncCommands = connection.sync();
            
            // 建立发布/订阅连接
            pubSubConnection = redisClient.connectPubSub();
            
            System.out.println("Lettuce Redis客户端连接成功");
        } catch (Exception e) {
            System.err.println("Lettuce Redis客户端连接失败: " + e.getMessage());
        }
    }
    
    /**
     * 尝试获取锁
     * @param key 锁的键名
     * @param value 锁的值（通常是线程标识）
     * @param leaseTime 锁的过期时间（秒）
     * @return 是否获取成功
     */
    public boolean tryAcquire(String key, String value, long leaseTime) {
        try {
            // Lua脚本实现原子性的加锁操作
            String script = 
                "if redis.call('exists', KEYS[1]) == 0 then " +
                "    redis.call('hset', KEYS[1], ARGV[2], 1) " +
                "    redis.call('pexpire', KEYS[1], ARGV[1]) " +
                "    return 1 " +
                "else " +
                "    if redis.call('hexists', KEYS[1], ARGV[2]) == 1 then " +
                "        redis.call('hincrby', KEYS[1], ARGV[2], 1) " +
                "        redis.call('pexpire', KEYS[1], ARGV[1]) " +
                "        return 1 " +
                "    else " +
                "        return 0 " +
                "    end " +
                "end";
            
            Object result = syncCommands.eval(script, ScriptOutputType.INTEGER, 
                new String[]{key}, 
                String.valueOf(leaseTime * 1000), 
                value);
            
            return result != null && (Long) result == 1;
        } catch (Exception e) {
            System.err.println("获取锁失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 释放锁
     * @param key 锁的键名
     * @param value 锁的值（通常是线程标识）
     * @param leaseTime 锁的过期时间（秒）
     * @return 是否释放成功
     */
    public boolean release(String key, String value, long leaseTime) {
        try {
            // Lua脚本实现原子性的解锁操作
            String script = 
                "if redis.call('hexists', KEYS[1], ARGV[2]) == 0 then " +
                "    return 0 " +
                "else " +
                "    local counter = redis.call('hincrby', KEYS[1], ARGV[2], -1) " +
                "    if counter > 0 then " +
                "        redis.call('pexpire', KEYS[1], ARGV[1]) " +
                "        return 1 " +
                "    else " +
                "        redis.call('del', KEYS[1]) " +
                "        return 1 " +
                "    end " +
                "end";
            
            Object result = syncCommands.eval(script, ScriptOutputType.INTEGER, 
                new String[]{key}, 
                String.valueOf(leaseTime * 1000), 
                value);
            
            return result != null && (Long) result == 1;
        } catch (Exception e) {
            System.err.println("释放锁失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 续期锁
     * @param key 锁的键名
     * @param value 锁的值（通常是线程标识）
     * @param leaseTime 锁的过期时间（秒）
     * @return 是否续期成功
     */
    public boolean renew(String key, String value, long leaseTime) {
        try {
            // Lua脚本实现原子性的续期操作
            String script = 
                "if redis.call('hexists', KEYS[1], ARGV[2]) == 1 then " +
                "    redis.call('pexpire', KEYS[1], ARGV[1]) " +
                "    return 1 " +
                "else " +
                "    return 0 " +
                "end";
            
            Object result = syncCommands.eval(script, ScriptOutputType.INTEGER, 
                new String[]{key}, 
                String.valueOf(leaseTime * 1000), 
                value);
            
            return result != null && (Long) result == 1;
        } catch (Exception e) {
            System.err.println("续期锁失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 发布消息到指定频道
     * @param channel 频道名称
     * @param message 消息内容
     * @return 发布的订阅者数量
     */
    public long publish(String channel, String message) {
        try {
            return syncCommands.publish(channel, message);
        } catch (Exception e) {
            System.err.println("发布消息失败: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * 订阅指定频道
     * @param channel 频道名称
     * @param listener 消息监听器
     */
    public void subscribe(String channel, RedisPubSubAdapter<String, String> listener) {
        try {
            // 注册监听器
            pubSubConnection.addListener(listener);
            
            // 订阅频道
            pubSubConnection.sync().subscribe(channel);
            
            // 保存订阅者信息
            subscribers.put(channel, listener);
            
            System.out.println("订阅频道成功: " + channel);
        } catch (Exception e) {
            System.err.println("订阅频道失败: " + e.getMessage());
        }
    }
    
    /**
     * 取消订阅指定频道
     * @param channel 频道名称
     */
    public void unsubscribe(String channel) {
        try {
            RedisPubSubAdapter<String, String> listener = subscribers.remove(channel);
            if (listener != null) {
                // 移除监听器
                pubSubConnection.removeListener(listener);
            }
            
            // 取消订阅
            pubSubConnection.sync().unsubscribe(channel);
            
            System.out.println("取消订阅频道: " + channel);
        } catch (Exception e) {
            System.err.println("取消订阅频道失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查键是否存在
     * @param key 键名
     * @return 键是否存在
     */
    public boolean exists(String key) {
        try {
            return syncCommands.exists(key) > 0;
        } catch (Exception e) {
            System.err.println("检查键存在失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 关闭连接
     */
    public void close() {
        if (connection != null) {
            connection.close();
        }
        if (pubSubConnection != null) {
            pubSubConnection.close();
        }
        if (redisClient != null) {
            redisClient.shutdown();
        }
        System.out.println("Lettuce Redis客户端连接已关闭");
    }
    
    /**
     * 获取同步命令对象
     * @return 同步命令对象
     */
    public RedisCommands<String, String> getSyncCommands() {
        return syncCommands;
    }
    
    /**
     * 获取发布/订阅连接对象
     * @return 发布/订阅连接对象
     */
    public StatefulRedisPubSubConnection<String, String> getPubSubConnection() {
        return pubSubConnection;
    }
}