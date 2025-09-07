package com.example.redis.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis配置读取测试类
 */
public class RedisConfigTest {
    
    @Test
    public void testLoadConfig() {
        RedisConfig config = new RedisConfig();
        
        // 验证配置是否正确加载
        assertNotNull(config.getHost(), "Host should not be null");
        assertEquals("router", config.getHost(), "Host should be localhost");
        assertEquals(6379, config.getPort(), "Port should be 6379");
        assertEquals(15, config.getDatabase(), "Database should be 1 for test environment");
        assertEquals("", config.getPassword(), "Password should be empty");
        assertEquals(2000, config.getTimeout(), "Timeout should be 2000");
        assertEquals(10, config.getMaxTotal(), "MaxTotal should be 10 for test environment");
        assertEquals(5, config.getMaxIdle(), "MaxIdle should be 5 for test environment");
        assertEquals(2, config.getMinIdle(), "MinIdle should be 2 for test environment");
    }
    
    @Test
    public void testToString() {
        RedisConfig config = new RedisConfig();
        String configStr = config.toString();
        
        // 验证toString方法
        assertTrue(configStr.contains("RedisConfig"), "ToString should contain class name");
        assertTrue(configStr.contains("router"), "ToString should contain host");
    }
}