package com.example.redis;

import com.example.redis.config.RedisConfig;

/**
 * Redis配置使用示例
 */
public class RedisConfigExample {
    
    public static void main(String[] args) {
        // 创建Redis配置实例
        RedisConfig config = new RedisConfig();
        
        // 输出配置信息
        System.out.println("Redis配置信息:");
        System.out.println("主机地址: " + config.getHost());
        System.out.println("端口号: " + config.getPort());
        System.out.println("数据库索引: " + config.getDatabase());
        System.out.println("连接超时: " + config.getTimeout() + "ms");
        System.out.println("最大连接数: " + config.getMaxTotal());
        System.out.println("最大空闲连接数: " + config.getMaxIdle());
        System.out.println("最小空闲连接数: " + config.getMinIdle());
        System.out.println();
        System.out.println("完整配置: " + config.toString());
    }
}