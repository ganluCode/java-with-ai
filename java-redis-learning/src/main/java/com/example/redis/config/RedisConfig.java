package com.example.redis.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Redis配置读取工具类
 */
public class RedisConfig {
    
    private String host;
    private int port;
    private int database;
    private String password;
    private int timeout;
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    
    public RedisConfig() {
        loadConfig();
    }
    
    /**
     * 从配置文件加载Redis配置
     */
    private void loadConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("redis.properties")) {
            if (input == null) {
                System.out.println("无法找到redis.properties配置文件");
                return;
            }
            props.load(input);
            
            this.host = props.getProperty("redis.host", "localhost");
            this.port = Integer.parseInt(props.getProperty("redis.port", "6379"));
            this.database = Integer.parseInt(props.getProperty("redis.database", "0"));
            this.password = props.getProperty("redis.password", "");
            this.timeout = Integer.parseInt(props.getProperty("redis.timeout", "2000"));
            this.maxTotal = Integer.parseInt(props.getProperty("redis.maxTotal", "20"));
            this.maxIdle = Integer.parseInt(props.getProperty("redis.maxIdle", "10"));
            this.minIdle = Integer.parseInt(props.getProperty("redis.minIdle", "5"));
            
        } catch (IOException e) {
            System.err.println("读取Redis配置文件失败: " + e.getMessage());
        }
    }
    
    // Getter方法
    public String getHost() {
        return host;
    }
    
    public int getPort() {
        return port;
    }
    
    public int getDatabase() {
        return database;
    }
    
    public String getPassword() {
        return password;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public int getMaxTotal() {
        return maxTotal;
    }
    
    public int getMaxIdle() {
        return maxIdle;
    }
    
    public int getMinIdle() {
        return minIdle;
    }
    
    @Override
    public String toString() {
        return "RedisConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", database=" + database +
                ", password='" + password + '\'' +
                ", timeout=" + timeout +
                ", maxTotal=" + maxTotal +
                ", maxIdle=" + maxIdle +
                ", minIdle=" + minIdle +
                '}';
    }
}