package com.example.redis.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Lettuce Redis客户端使用示例
 */
public class LettuceRedisClientExample {
    
    public static void main(String[] args) {
        // 创建Redis客户端实例
        LettuceRedisClient redisClient = new LettuceRedisClient();
        
        try {
            // 检查连接状态
            if (redisClient.isConnected()) {
                System.out.println("Redis连接成功！");
            } else {
                System.out.println("Redis连接失败！");
                return;
            }
            
            // ==================== String 操作示例 ====================
            System.out.println("\n=== String 操作示例 ===");
            String stringKey = "example:string:key";
            String stringValue = "Hello, Redis!";
            
            // 设置字符串值
            String setResult = redisClient.set(stringKey, stringValue);
            System.out.println("设置字符串结果: " + setResult);
            
            // 获取字符串值
            String getResult = redisClient.get(stringKey);
            System.out.println("获取字符串值: " + getResult);
            
            // 设置带过期时间的字符串值
            String expireKey = "example:expire:key";
            String expireValue = "This will expire in 10 seconds";
            String setexResult = redisClient.setex(expireKey, expireValue, 10);
            System.out.println("设置带过期时间的字符串结果: " + setexResult);
            
            // ==================== Hash 操作示例 ====================
            System.out.println("\n=== Hash 操作示例 ===");
            String hashKey = "example:hash:user";
            
            // 设置哈希字段
            redisClient.hset(hashKey, "name", "张三");
            redisClient.hset(hashKey, "age", "25");
            redisClient.hset(hashKey, "email", "zhangsan@example.com");
            System.out.println("设置哈希字段完成");
            
            // 获取哈希字段值
            String name = redisClient.hget(hashKey, "name");
            System.out.println("用户姓名: " + name);
            
            // 获取哈希所有字段
            Map<String, String> userMap = redisClient.hgetall(hashKey);
            System.out.println("用户信息: " + userMap);
            
            // ==================== List 操作示例 ====================
            System.out.println("\n=== List 操作示例 ===");
            String listKey = "example:list:tasks";
            
            // 向列表添加元素
            redisClient.lpush(listKey, "任务3");
            redisClient.lpush(listKey, "任务2");
            redisClient.rpush(listKey, "任务4");
            redisClient.lpush(listKey, "任务1");
            System.out.println("向列表添加任务完成");
            
            // 获取列表所有元素
            List<String> tasks = redisClient.lrange(listKey, 0, -1);
            System.out.println("所有任务: " + tasks);
            
            // 从列表弹出元素
            String leftPop = redisClient.lpop(listKey);
            System.out.println("从左侧弹出任务: " + leftPop);
            
            String rightPop = redisClient.rpop(listKey);
            System.out.println("从右侧弹出任务: " + rightPop);
            
            // ==================== Set 操作示例 ====================
            System.out.println("\n=== Set 操作示例 ===");
            String setKey = "example:set:tags";
            
            // 向集合添加元素
            redisClient.sadd(setKey, "Java", "Redis", "Lettuce", "Spring");
            System.out.println("向集合添加标签完成");
            
            // 获取集合所有元素
            Set<String> tags = redisClient.smembers(setKey);
            System.out.println("所有标签: " + tags);
            
            // 检查元素是否在集合中
            boolean hasJava = redisClient.sismember(setKey, "Java");
            System.out.println("是否包含Java标签: " + hasJava);
            
            boolean hasPython = redisClient.sismember(setKey, "Python");
            System.out.println("是否包含Python标签: " + hasPython);
            
            // ==================== 通用操作示例 ====================
            System.out.println("\n=== 通用操作示例 ===");
            
            // 获取所有键
            Set<String> allKeys = redisClient.keys("*");
            System.out.println("数据库中的所有键数量: " + allKeys.size());
            
            // 检查特定键是否存在
            boolean keyExists = redisClient.exists(stringKey);
            System.out.println("键 '" + stringKey + "' 是否存在: " + keyExists);
            
            System.out.println("\nRedis客户端使用示例完成！");
            
        } catch (Exception e) {
            System.err.println("Redis操作过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭连接
            redisClient.close();
        }
    }
}