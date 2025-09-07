package com.example.redis.lock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * LockManager单元测试
 */
public class LockManagerTest {

    private LockManager lockManager;
    private LettuceClient lettuceClient;

    @BeforeEach
    public void setUp() {
        lettuceClient = mock(LettuceClient.class);
        lockManager = new LockManager(lettuceClient);
    }

    @AfterEach
    public void tearDown() {
        // 清理资源
    }

    @Test
    public void testConstructor() {
        assertNotNull(lockManager);
    }

    @Test
    public void testAcquireLock() {
        when(lettuceClient.tryAcquire(anyString(), anyString(), anyLong())).thenReturn(true);
        
        boolean result = lockManager.acquireLock("testKey", "testValue", 30);
        
        assertTrue(result);
        verify(lettuceClient).tryAcquire("testKey", "testValue", 30);
    }

    @Test
    public void testReleaseLock() {
        when(lettuceClient.release(anyString(), anyString(), anyLong())).thenReturn(true);
        
        boolean result = lockManager.releaseLock("testKey", "testValue", 30);
        
        assertTrue(result);
        verify(lettuceClient).release("testKey", "testValue", 30);
    }

    @Test
    public void testRenewLock() {
        when(lettuceClient.renew(anyString(), anyString(), anyLong())).thenReturn(true);
        
        boolean result = lockManager.renewLock("testKey", "testValue", 30);
        
        assertTrue(result);
        verify(lettuceClient).renew("testKey", "testValue", 30);
    }
}