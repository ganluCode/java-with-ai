package com.example.redis.lock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LockConfig单元测试
 */
public class LockConfigTest {

    @Test
    public void testDefaultBuilder() {
        LockConfig config = new LockConfig.Builder().build();
        
        assertEquals("defaultLock", config.getLockKey());
        assertEquals(30, config.getLeaseTime());
        assertEquals(10, config.getRenewalTime());
        assertFalse(config.isFair());
    }

    @Test
    public void testCustomBuilder() {
        LockConfig config = new LockConfig.Builder()
                .lockKey("customLock")
                .leaseTime(60)
                .renewalTime(20)
                .fair(true)
                .build();
        
        assertEquals("customLock", config.getLockKey());
        assertEquals(60, config.getLeaseTime());
        assertEquals(20, config.getRenewalTime());
        assertTrue(config.isFair());
    }

    @Test
    public void testBuilderChaining() {
        LockConfig.Builder builder = new LockConfig.Builder();
        
        LockConfig.Builder result1 = builder.lockKey("testLock");
        assertSame(builder, result1, "Builder方法应该返回this");
        
        LockConfig.Builder result2 = builder.leaseTime(45);
        assertSame(builder, result2, "Builder方法应该返回this");
        
        LockConfig.Builder result3 = builder.renewalTime(15);
        assertSame(builder, result3, "Builder方法应该返回this");
        
        LockConfig.Builder result4 = builder.fair(true);
        assertSame(builder, result4, "Builder方法应该返回this");
    }

    @Test
    public void testGetters() {
        LockConfig config = new LockConfig.Builder()
                .lockKey("getterTest")
                .leaseTime(100)
                .renewalTime(50)
                .fair(false)
                .build();
        
        assertEquals("getterTest", config.getLockKey());
        assertEquals(100, config.getLeaseTime());
        assertEquals(50, config.getRenewalTime());
        assertFalse(config.isFair());
    }
}