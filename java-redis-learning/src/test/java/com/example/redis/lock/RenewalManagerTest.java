package com.example.redis.lock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * RenewalManager单元测试
 */
public class RenewalManagerTest {

    private RenewalManager renewalManager;

    @BeforeEach
    public void setUp() {
        renewalManager = new RenewalManager();
    }

    @AfterEach
    public void tearDown() {
        if (renewalManager != null) {
            try {
                renewalManager.shutdown();
            } catch (Exception e) {
                // 忽略关闭异常
            }
        }
    }

    @Test
    public void testConstructor() {
        RenewalManager manager = new RenewalManager();
        assertNotNull(manager);
    }

    @Test
    public void testStartRenewal() {
        try {
            Runnable renewalTask = mock(Runnable.class);
            renewalManager.startRenewal("testKey", renewalTask, 1);
            // 验证方法可以正常调用
            assertTrue(true);
        } catch (Exception e) {
            fail("startRenewal方法不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testStopRenewal() {
        try {
            renewalManager.stopRenewal("testKey");
            // 验证方法可以正常调用
            assertTrue(true);
        } catch (Exception e) {
            fail("stopRenewal方法不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testShutdown() {
        try {
            renewalManager.shutdown();
            // 验证方法可以正常调用
            assertTrue(true);
        } catch (Exception e) {
            fail("shutdown方法不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testRenewalTaskExecution() throws InterruptedException {
        // 创建一个计数器来跟踪任务执行次数
        AtomicInteger executionCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);
        
        Runnable renewalTask = () -> {
            executionCount.incrementAndGet();
            latch.countDown();
        };
        
        try {
            // 启动一个快速执行的续期任务（100毫秒周期）
            renewalManager.startRenewal("testKey", renewalTask, 1);
            
            // 等待任务执行
            boolean executed = latch.await(2, TimeUnit.SECONDS);
            
            // 验证任务被执行
            assertTrue(executed, "续期任务应该被执行");
            assertEquals(1, executionCount.get(), "续期任务应该执行一次");
        } finally {
            // 停止续期任务
            renewalManager.stopRenewal("testKey");
        }
    }

    @Test
    public void testStopRenewalWhenTaskExists() {
        try {
            Runnable renewalTask = mock(Runnable.class);
            renewalManager.startRenewal("testKey", renewalTask, 1);
            
            // 停止续期任务
            renewalManager.stopRenewal("testKey");
            
            // 验证方法可以正常调用
            assertTrue(true);
        } catch (Exception e) {
            fail("stopRenewal方法不应该抛出异常: " + e.getMessage());
        }
    }
}