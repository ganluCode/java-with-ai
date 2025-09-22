package com.example.redis.lock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import io.lettuce.core.pubsub.RedisPubSubAdapter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis发布/订阅功能综合测试套件
 * 
 * 全面测试LettuceClient的发布/订阅功能
 */
@Disabled("需要Redis服务器运行在localhost:6379")
public class RedisPubSubComprehensiveTest {

    private LettuceClient client1;
    private LettuceClient client2;
    private LettuceClient client3;
    
    @BeforeEach
    public void setUp() {
        client1 = new LettuceClient();
        client2 = new LettuceClient();
        client3 = new LettuceClient();
    }
    
    @AfterEach
    public void tearDown() {
        try {
            if (client1 != null) client1.close();
            if (client2 != null) client2.close();
            if (client3 != null) client3.close();
        } catch (Exception e) {
            System.err.println("清理资源异常: " + e.getMessage());
        }
    }
    
    @Test
    public void testSingleMessagePublishSubscribe() throws InterruptedException {
        System.out.println("=== 测试单条消息发布/订阅 ===");
        final String channel = "test:single:message";
        final String message = "Hello Single Message!";
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<String> receivedMessage = new AtomicReference<>();
        
        // 创建订阅监听器
        RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
            @Override
            public void message(String ch, String msg) {
                if (channel.equals(ch)) {
                    System.out.println("收到消息: " + msg);
                    receivedMessage.set(msg);
                    latch.countDown();
                }
            }
        };
        
        // 订阅频道
        client1.subscribe(channel, listener);
        Thread.sleep(100); // 等待订阅建立
        
        // 发布消息
        long subscribers = client2.publish(channel, message);
        System.out.println("发布消息 '" + message + "' 到频道 '" + channel + "'，订阅者数量: " + subscribers);
        
        // 等待消息接收
        boolean received = latch.await(5, TimeUnit.SECONDS);
        assertTrue(received, "应该在5秒内收到消息");
        assertEquals(message, receivedMessage.get(), "接收到的消息应该与发布的消息一致");
        
        // 取消订阅
        client1.unsubscribe(channel);
        System.out.println("=== 单条消息发布/订阅测试完成 ===");
    }
    
    @Test
    public void testMultipleMessagesPublishSubscribe() throws InterruptedException {
        System.out.println("=== 测试多条消息发布/订阅 ===");
        final String channel = "test:multiple:messages";
        final String[] messages = {"Message 1", "Message 2", "Message 3", "Message 4", "Message 5"};
        final CountDownLatch latch = new CountDownLatch(messages.length);
        final AtomicInteger receivedCount = new AtomicInteger(0);
        
        // 创建订阅监听器
        RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
            @Override
            public void message(String ch, String message) {
                if (channel.equals(ch)) {
                    System.out.println("收到消息 #" + receivedCount.incrementAndGet() + ": " + message);
                    latch.countDown();
                }
            }
        };
        
        // 订阅频道
        client1.subscribe(channel, listener);
        Thread.sleep(100); // 等待订阅建立
        
        // 发布多条消息
        for (String message : messages) {
            long subscribers = client2.publish(channel, message);
            System.out.println("发布消息: " + message + " (订阅者数量: " + subscribers + ")");
        }
        
        // 等待所有消息接收完成
        boolean allReceived = latch.await(10, TimeUnit.SECONDS);
        assertTrue(allReceived, "应该在10秒内收到所有" + messages.length + "条消息");
        assertEquals(messages.length, receivedCount.get(), "应该收到所有" + messages.length + "条消息");
        
        // 取消订阅
        client1.unsubscribe(channel);
        System.out.println("=== 多条消息发布/订阅测试完成 ===");
    }
    
    @Test
    public void testMultipleSubscribers() throws InterruptedException {
        System.out.println("=== 测试多个订阅者 ===");
        final String channel = "test:multiple:subscribers";
        final String message = "Broadcast to Multiple Subscribers";
        final int subscriberCount = 3;
        final CountDownLatch[] latches = new CountDownLatch[subscriberCount];
        final AtomicInteger[] receivedCounts = new AtomicInteger[subscriberCount];
        
        // 初始化数组
        for (int i = 0; i < subscriberCount; i++) {
            latches[i] = new CountDownLatch(1);
            receivedCounts[i] = new AtomicInteger(0);
        }
        
        // 创建多个订阅监听器
        RedisPubSubAdapter<String, String>[] listeners = new RedisPubSubAdapter[subscriberCount];
        for (int i = 0; i < subscriberCount; i++) {
            final int subscriberId = i;
            listeners[i] = new RedisPubSubAdapter<String, String>() {
                @Override
                public void message(String ch, String msg) {
                    if (channel.equals(ch)) {
                        System.out.println("订阅者" + subscriberId + "收到消息: " + msg);
                        receivedCounts[subscriberId].incrementAndGet();
                        latches[subscriberId].countDown();
                    }
                }
            };
        }
        
        // 多个订阅者订阅同一个频道
        for (int i = 0; i < subscriberCount; i++) {
            LettuceClient subscriberClient = getSubscriberClient(i);
            subscriberClient.subscribe(channel, listeners[i]);
        }
        Thread.sleep(200); // 等待所有订阅建立
        
        // 发布消息
        long subscribers = client1.publish(channel, message);
        System.out.println("发布广播消息: " + message + " (订阅者数量: " + subscribers + ")");
        
        // 等待所有订阅者收到消息
        boolean allReceived = true;
        for (int i = 0; i < subscriberCount; i++) {
            boolean received = latches[i].await(5, TimeUnit.SECONDS);
            if (!received) {
                System.out.println("订阅者" + i + "未在5秒内收到消息");
                allReceived = false;
            }
        }
        assertTrue(allReceived, "所有订阅者都应该在5秒内收到消息");
        
        // 验证每个订阅者都收到了消息
        for (int i = 0; i < subscriberCount; i++) {
            assertEquals(1, receivedCounts[i].get(), "订阅者" + i + "应该收到1条消息");
        }
        
        // 取消所有订阅
        for (int i = 0; i < subscriberCount; i++) {
            LettuceClient subscriberClient = getSubscriberClient(i);
            subscriberClient.unsubscribe(channel);
        }
        System.out.println("=== 多个订阅者测试完成 ===");
    }
    
    @Test
    public void testUnsubscribeFunctionality() throws InterruptedException {
        System.out.println("=== 测试取消订阅功能 ===");
        final String channel = "test:unsubscribe:functionality";
        final String message1 = "Message before unsubscribe";
        final String message2 = "Message after unsubscribe";
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final AtomicReference<String> receivedMessage1 = new AtomicReference<>();
        final AtomicReference<String> receivedMessage2 = new AtomicReference<>();
        
        // 创建订阅监听器
        RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
            @Override
            public void message(String ch, String message) {
                if (channel.equals(ch)) {
                    System.out.println("收到消息: " + message);
                    if (message1.equals(message)) {
                        receivedMessage1.set(message);
                        latch1.countDown();
                    } else if (message2.equals(message)) {
                        receivedMessage2.set(message);
                        latch2.countDown();
                    }
                }
            }
        };
        
        // 订阅频道
        client1.subscribe(channel, listener);
        Thread.sleep(100); // 等待订阅建立
        
        // 发布第一条消息
        long subscribers1 = client2.publish(channel, message1);
        System.out.println("发布第一条消息: " + message1 + " (订阅者数量: " + subscribers1 + ")");
        
        // 等待第一条消息接收
        boolean firstReceived = latch1.await(5, TimeUnit.SECONDS);
        assertTrue(firstReceived, "应该收到第一条消息");
        assertEquals(message1, receivedMessage1.get(), "第一条消息应该正确");
        
        // 取消订阅
        client1.unsubscribe(channel);
        System.out.println("取消订阅频道: " + channel);
        Thread.sleep(100); // 等待取消订阅完成
        
        // 发布第二条消息（应该收不到）
        long subscribers2 = client2.publish(channel, message2);
        System.out.println("发布第二条消息: " + message2 + " (订阅者数量: " + subscribers2 + ")");
        
        // 等待一段时间，确保不会收到消息
        Thread.sleep(500);
        
        // 验证没有收到第二条消息
        assertEquals(1, latch2.getCount(), "取消订阅后不应该收到消息");
        assertNull(receivedMessage2.get(), "取消订阅后不应该收到消息");
        
        System.out.println("=== 取消订阅功能测试完成 ===");
    }
    
    @Test
    public void testSubscribeUnsubscribeMultipleChannels() throws InterruptedException {
        System.out.println("=== 测试多个频道的订阅/取消订阅 ===");
        final String[] channels = {"test:channel:1", "test:channel:2", "test:channel:3"};
        final String message = "Multi-channel message";
        final CountDownLatch[] latches = new CountDownLatch[channels.length];
        final AtomicReference<String>[] receivedMessages = new AtomicReference[channels.length];
        
        // 初始化数组
        for (int i = 0; i < channels.length; i++) {
            latches[i] = new CountDownLatch(1);
            receivedMessages[i] = new AtomicReference<>();
        }
        
        // 创建订阅监听器
        RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
            @Override
            public void message(String channel, String message) {
                for (int i = 0; i < channels.length; i++) {
                    if (channels[i].equals(channel)) {
                        System.out.println("频道" + (i+1) + "收到消息: " + message);
                        receivedMessages[i].set(message);
                        latches[i].countDown();
                        break;
                    }
                }
            }
        };
        
        // 订阅多个频道
        for (String channel : channels) {
            client1.subscribe(channel, listener);
        }
        Thread.sleep(200); // 等待所有订阅建立
        
        // 发布消息到每个频道
        for (int i = 0; i < channels.length; i++) {
            long subscribers = client2.publish(channels[i], message);
            System.out.println("发布消息到频道" + (i+1) + ": " + message + " (订阅者数量: " + subscribers + ")");
        }
        
        // 等待所有消息接收
        boolean allReceived = true;
        for (int i = 0; i < channels.length; i++) {
            boolean received = latches[i].await(5, TimeUnit.SECONDS);
            if (!received) {
                System.out.println("频道" + (i+1) + "未在5秒内收到消息");
                allReceived = false;
            }
        }
        assertTrue(allReceived, "所有频道都应该在5秒内收到消息");
        
        // 验证接收到的消息
        for (int i = 0; i < channels.length; i++) {
            assertEquals(message, receivedMessages[i].get(), "频道" + (i+1) + "接收到的消息应该正确");
        }
        
        // 取消订阅所有频道
        for (String channel : channels) {
            client1.unsubscribe(channel);
        }
        System.out.println("=== 多个频道的订阅/取消订阅测试完成 ===");
    }
    
    @Test
    public void testConcurrentPublishSubscribe() throws InterruptedException {
        System.out.println("=== 测试并发发布/订阅 ===");
        final String channel = "test:concurrent:pubsub";
        final int messageCount = 20;
        final CountDownLatch allMessagesLatch = new CountDownLatch(messageCount);
        final AtomicInteger receivedCount = new AtomicInteger(0);
        
        // 创建订阅监听器
        RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
            @Override
            public void message(String ch, String message) {
                if (channel.equals(ch)) {
                    System.out.println("并发收到消息: " + message + " (总计: " + receivedCount.incrementAndGet() + ")");
                    allMessagesLatch.countDown();
                }
            }
        };
        
        // 订阅频道
        client1.subscribe(channel, listener);
        Thread.sleep(100); // 等待订阅建立
        
        // 并发发布消息
        for (int i = 0; i < messageCount; i++) {
            final int messageId = i;
            new Thread(() -> {
                String message = "Concurrent Message " + messageId;
                long subscribers = client2.publish(channel, message);
                System.out.println("并发发布消息: " + message + " (订阅者数量: " + subscribers + ")");
            }).start();
        }
        
        // 等待所有消息接收完成
        boolean allReceived = allMessagesLatch.await(15, TimeUnit.SECONDS);
        assertTrue(allReceived, "应该在15秒内收到所有" + messageCount + "条消息");
        assertEquals(messageCount, receivedCount.get(), "应该收到所有" + messageCount + "条消息");
        
        // 取消订阅
        client1.unsubscribe(channel);
        System.out.println("=== 并发发布/订阅测试完成 ===");
    }
    
    @Test
    public void testSubscribeEvents() throws InterruptedException {
        System.out.println("=== 测试订阅事件 ===");
        final String channel = "test:subscribe:events";
        final CountDownLatch subscribeLatch = new CountDownLatch(1);
        final CountDownLatch unsubscribeLatch = new CountDownLatch(1);
        final AtomicInteger subscribeCount = new AtomicInteger(0);
        final AtomicInteger unsubscribeCount = new AtomicInteger(0);
        
        // 创建带有事件处理的订阅监听器
        RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
            @Override
            public void subscribed(String ch, long count) {
                if (channel.equals(ch)) {
                    System.out.println("订阅事件 - 频道: " + ch + ", 订阅者数量: " + count);
                    subscribeCount.set((int) count);
                    subscribeLatch.countDown();
                }
            }
            
            @Override
            public void unsubscribed(String ch, long count) {
                if (channel.equals(ch)) {
                    System.out.println("取消订阅事件 - 频道: " + ch + ", 订阅者数量: " + count);
                    unsubscribeCount.set((int) count);
                    unsubscribeLatch.countDown();
                }
            }
            
            @Override
            public void message(String ch, String message) {
                if (channel.equals(ch)) {
                    System.out.println("消息事件 - 频道: " + ch + ", 消息: " + message);
                }
            }
        };
        
        // 订阅频道
        System.out.println("订阅频道: " + channel);
        client1.subscribe(channel, listener);
        
        // 等待订阅事件
        boolean subscribed = subscribeLatch.await(5, TimeUnit.SECONDS);
        assertTrue(subscribed, "应该在5秒内收到订阅事件");
        assertEquals(1, subscribeCount.get(), "应该有1个订阅者");
        
        // 发布测试消息
        long subscribers = client2.publish(channel, "Test Subscribe Event");
        System.out.println("发布测试消息 (订阅者数量: " + subscribers + ")");
        
        // 等待一小段时间确保消息处理完成
        Thread.sleep(200);
        
        // 取消订阅
        System.out.println("取消订阅频道: " + channel);
        client1.unsubscribe(channel);
        
        // 等待取消订阅事件
        boolean unsubscribed = unsubscribeLatch.await(5, TimeUnit.SECONDS);
        assertTrue(unsubscribed, "应该在5秒内收到取消订阅事件");
        assertEquals(0, unsubscribeCount.get(), "应该有0个订阅者");
        
        System.out.println("=== 订阅事件测试完成 ===");
    }
    
    /**
     * 根据订阅者ID获取对应的客户端实例
     */
    private LettuceClient getSubscriberClient(int subscriberId) {
        switch (subscriberId % 3) {
            case 0: return client1;
            case 1: return client2;
            case 2: return client3;
            default: return client1;
        }
    }
}