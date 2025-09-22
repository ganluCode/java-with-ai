package com.example.redis.lock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Watchdog功能测试类
 */
public class WatchdogTest {
    
    private Watchdog watchdog;
    private RedisDistributedLock mockLock;
    private LettuceClient mockLettuceClient;
    
    @BeforeEach
    public void setUp() {
        watchdog = new Watchdog();
        mockLock = mock(RedisDistributedLock.class);
        mockLettuceClient = mock(LettuceClient.class);
    }
    
    @Test
    public void testStartAndStopWatchdog() {
        // 准备测试数据
        when(mockLock.getLockKey()).thenReturn("testLock");
        when(mockLock.getLettuceClient()).thenReturn(mockLettuceClient);
        
        // 启动Watchdog
        watchdog.startWatchdog(mockLock, 1); // 1秒周期
        
        // 验证Watchdog已启动
        assertTrue(watchdog.isRunning(), "Watchdog应该正在运行");
        
        // 等待一段时间让健康检查执行
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 停止Watchdog
        watchdog.stopWatchdog();
        
        // 验证Watchdog已停止
        assertFalse(watchdog.isRunning(), "Watchdog应该已停止");
    }
    
    @Test
    public void testCheckRenewalTask() {
        // 调用检查续期任务方法
        watchdog.checkRenewalTask();
        
        // 验证方法可以正常执行（目前只是打印日志）
        assertTrue(true, "检查续期任务方法应该正常执行");
    }
    
    @Test
    public void testCheckLockStatusWithValidLock() {
        // 准备测试数据
        when(mockLock.getLockKey()).thenReturn("testLock");
        when(mockLettuceClient.exists("testLock")).thenReturn(true);
        
        // 设置私有字段
        try {
            java.lang.reflect.Field lockField = Watchdog.class.getDeclaredField("lock");
            lockField.setAccessible(true);
            lockField.set(watchdog, mockLock);
            
            java.lang.reflect.Field clientField = Watchdog.class.getDeclaredField("lettuceClient");
            clientField.setAccessible(true);
            clientField.set(watchdog, mockLettuceClient);
        } catch (Exception e) {
            fail("无法设置测试数据: " + e.getMessage());
        }
        
        // 调用检查锁状态方法
        watchdog.checkLockStatus();
        
        // 验证方法可以正常执行
        assertTrue(true, "检查锁状态方法应该正常执行");
    }
    
    @Test
    public void testCheckLockStatusWithInvalidLock() {
        // 准备测试数据
        when(mockLock.getLockKey()).thenReturn("testLock");
        when(mockLettuceClient.exists("testLock")).thenReturn(false);
        
        // 设置私有字段
        try {
            java.lang.reflect.Field lockField = Watchdog.class.getDeclaredField("lock");
            lockField.setAccessible(true);
            lockField.set(watchdog, mockLock);
            
            java.lang.reflect.Field clientField = Watchdog.class.getDeclaredField("lettuceClient");
            clientField.setAccessible(true);
            clientField.set(watchdog, mockLettuceClient);
        } catch (Exception e) {
            fail("无法设置测试数据: " + e.getMessage());
        }
        
        // 捕获系统输出
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setErr(new java.io.PrintStream(outContent));
        
        // 调用检查锁状态方法
        watchdog.checkLockStatus();
        
        // 验证输出包含错误信息
        String output = outContent.toString();
        assertTrue(output.contains("锁状态异常"), "应该输出锁状态异常信息");
        
        // 恢复系统输出
        System.setErr(System.err);
    }
    
    @Test
    public void testCheckThreadHealthWithValidThread() {
        // 准备测试数据
        Thread mockThread = mock(Thread.class);
        when(mockLock.getOwnerThread()).thenReturn(mockThread);
        when(mockThread.isAlive()).thenReturn(true);
        
        // 设置私有字段
        try {
            java.lang.reflect.Field lockField = Watchdog.class.getDeclaredField("lock");
            lockField.setAccessible(true);
            lockField.set(watchdog, mockLock);
        } catch (Exception e) {
            fail("无法设置测试数据: " + e.getMessage());
        }
        
        // 捕获系统输出
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));
        
        // 调用检查线程健康方法
        watchdog.checkThreadHealth();
        
        // 验证输出包含正常信息
        String output = outContent.toString();
        assertTrue(output.contains("线程健康检查正常"), "应该输出线程健康检查正常信息");
        
        // 恢复系统输出
        System.setOut(System.out);
    }
    
    @Test
    public void testCheckThreadHealthWithInvalidThread() {
        // 准备测试数据
        Thread mockThread = mock(Thread.class);
        when(mockLock.getOwnerThread()).thenReturn(mockThread);
        when(mockThread.isAlive()).thenReturn(false);
        
        // 设置私有字段
        try {
            java.lang.reflect.Field lockField = Watchdog.class.getDeclaredField("lock");
            lockField.setAccessible(true);
            lockField.set(watchdog, mockLock);
        } catch (Exception e) {
            fail("无法设置测试数据: " + e.getMessage());
        }
        
        // 捕获系统输出
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setErr(new java.io.PrintStream(outContent));
        
        // 调用检查线程健康方法
        watchdog.checkThreadHealth();
        
        // 验证输出包含错误信息
        String output = outContent.toString();
        assertTrue(output.contains("线程健康检查失败"), "应该输出线程健康检查失败信息");
        
        // 恢复系统输出
        System.setErr(System.err);
    }
    
    @Test
    public void testAutoRecovery() {
        // 捕获系统输出
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));
        
        // 调用自动恢复方法
        watchdog.autoRecovery();
        
        // 验证输出包含恢复信息
        String output = outContent.toString();
        assertTrue(output.contains("执行自动恢复机制"), "应该输出执行自动恢复机制信息");
        
        // 恢复系统输出
        System.setOut(System.out);
    }
    
    @Test
    @Disabled("需要Redis服务器运行，暂时禁用")
    public void testPerformHealthCheckIntegration() throws InterruptedException {
        // 创建真实的锁和客户端
        LockConfig config = new LockConfig.Builder()
            .lockKey("watchdogTestLock")
            .watchdogPeriod(1)
            .build();
        RedisDistributedLock lock = new RedisDistributedLock(config);
        
        // 启动Watchdog
        watchdog.startWatchdog(lock, 1);
        
        // 等待健康检查执行
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);
        
        // 验证Watchdog正在运行
        assertTrue(watchdog.isRunning(), "Watchdog应该正在运行");
        
        // 停止Watchdog
        watchdog.stopWatchdog();
    }
}