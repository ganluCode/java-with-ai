package com.example.redis.lock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import io.lettuce.core.pubsub.RedisPubSubAdapter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis发布/订阅功能单元测试
 * 
 * 测试LettuceClient的发布/订阅功能
 */
@Disabled("需要Redis服务器运行在localhost:6379")
public class RedisPubSubTest {

    @Test
    public void testBasicPublishSubscribe() throws InterruptedException {
        System.out.println("=== 开始执行基础发布/订阅测试 ===");
        final String testChannel = "test:pubsub:basic";
        final String testMessage = "Hello Redis Pub/Sub!";
        final CountDownLatch messageReceivedLatch = new CountDownLatch(1);
        final AtomicReference<String> receivedMessage = new AtomicReference<>();
        final AtomicBoolean subscriptionSuccess = new AtomicBoolean(false);
        
        // 创建两个Redis客户端实例
        // 一个用于发布消息，一个用于订阅消息
        LettuceClient publisherClient = new LettuceClient();
        LettuceClient subscriberClient = new LettuceClient();
        
        try {
            // 创建消息监听器
            RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
                @Override
                public void message(String channel, String message) {
                    System.out.println("收到消息 - 频道: " + channel + ", 消息: " + message);
                    if (testChannel.equals(channel)) {
                        receivedMessage.set(message);
                        messageReceivedLatch.countDown();
                    }
                }
                
                @Override
                public void subscribed(String channel, long count) {
                    System.out.println("订阅成功 - 频道: " + channel + ", 订阅者数量: " + count);
                    if (testChannel.equals(channel)) {
                        subscriptionSuccess.set(true);
                    }
                }
            };
            
            // 订阅频道
            System.out.println("订阅频道: " + testChannel);
            subscriberClient.subscribe(testChannel, listener);
            
            // 等待订阅成功
            Thread.sleep(3000);
            assertTrue(subscriptionSuccess.get(), "应该成功订阅频道");
            
            // 发布消息
            System.out.println("发布消息到频道: " + testChannel);
            long subscribers = publisherClient.publish(testChannel, testMessage);
            System.out.println("消息发布完成，订阅者数量: " + subscribers);
            
            // 等待消息接收
            boolean messageReceived = messageReceivedLatch.await(5, TimeUnit.SECONDS);
            assertTrue(messageReceived, "应该在5秒内收到消息");
            
            // 验证接收到的消息
            assertEquals(testMessage, receivedMessage.get(), "接收到的消息应该与发布的消息一致");
            
            System.out.println("=== 基础发布/订阅测试完成 ===");
            
        } finally {
            // 清理资源
            try {
                subscriberClient.unsubscribe(testChannel);
                publisherClient.close();
                subscriberClient.close();
            } catch (Exception e) {
                System.err.println("资源清理异常: " + e.getMessage());
            }
        }
    }

    @Test
    public void testMultipleSubscribers() throws InterruptedException {
        System.out.println("=== 开始执行多订阅者测试 ===");
        final String testChannel = "test:pubsub:multiple";
        final String testMessage = "Broadcast Message";
        final int subscriberCount = 3;
        final CountDownLatch[] messageReceivedLatches = new CountDownLatch[subscriberCount];
        final AtomicReference<String>[] receivedMessages = new AtomicReference[subscriberCount];
        final AtomicBoolean[] subscriptionSuccesses = new AtomicBoolean[subscriberCount];
        
        // 初始化数组
        for (int i = 0; i < subscriberCount; i++) {
            messageReceivedLatches[i] = new CountDownLatch(1);
            receivedMessages[i] = new AtomicReference<>();
            subscriptionSuccesses[i] = new AtomicBoolean(false);
        }
        
        // 创建发布者客户端
        LettuceClient publisherClient = new LettuceClient();
        
        // 创建多个订阅者客户端
        LettuceClient[] subscriberClients = new LettuceClient[subscriberCount];
        RedisPubSubAdapter<String, String>[] listeners = new RedisPubSubAdapter[subscriberCount];
        
        try {
            // 创建多个订阅者
            for (int i = 0; i < subscriberCount; i++) {
                final int subscriberId = i;
                subscriberClients[i] = new LettuceClient();
                
                listeners[i] = new RedisPubSubAdapter<String, String>() {
                    @Override
                    public void message(String channel, String message) {
                        System.out.println("订阅者" + subscriberId + "收到消息 - 频道: " + channel + ", 消息: " + message);
                        if (testChannel.equals(channel)) {
                            receivedMessages[subscriberId].set(message);
                            messageReceivedLatches[subscriberId].countDown();
                        }
                    }
                    
                    @Override
                    public void subscribed(String channel, long count) {
                        System.out.println("订阅者" + subscriberId + "订阅成功 - 频道: " + channel + ", 订阅者数量: " + count);
                        if (testChannel.equals(channel)) {
                            subscriptionSuccesses[subscriberId].set(true);
                        }
                    }
                };
                
                // 订阅频道
                System.out.println("订阅者" + subscriberId + "订阅频道: " + testChannel);
                subscriberClients[i].subscribe(testChannel, listeners[i]);
            }
            
            // 等待所有订阅成功
            Thread.sleep(200);
            for (int i = 0; i < subscriberCount; i++) {
                assertTrue(subscriptionSuccesses[i].get(), "订阅者" + i + "应该成功订阅频道");
            }
            
            // 发布消息
            System.out.println("发布广播消息到频道: " + testChannel);
            long subscribers = publisherClient.publish(testChannel, testMessage);
            System.out.println("消息发布完成，订阅者数量: " + subscribers);
            
            // 等待所有订阅者收到消息
            boolean allMessagesReceived = true;
            for (int i = 0; i < subscriberCount; i++) {
                boolean messageReceived = messageReceivedLatches[i].await(5, TimeUnit.SECONDS);
                if (!messageReceived) {
                    System.out.println("订阅者" + i + "未在5秒内收到消息");
                    allMessagesReceived = false;
                }
            }
            
            assertTrue(allMessagesReceived, "所有订阅者都应该在5秒内收到消息");
            
            // 验证所有订阅者收到的消息
            for (int i = 0; i < subscriberCount; i++) {
                assertEquals(testMessage, receivedMessages[i].get(), "订阅者" + i + "接收到的消息应该与发布的消息一致");
            }
            
            // 验证订阅者数量
            assertEquals(subscriberCount, subscribers, "应该有" + subscriberCount + "个订阅者");
            
            System.out.println("=== 多订阅者测试完成 ===");
            
        } finally {
            // 清理资源
            try {
                for (int i = 0; i < subscriberCount; i++) {
                    if (subscriberClients[i] != null) {
                        subscriberClients[i].unsubscribe(testChannel);
                        subscriberClients[i].close();
                    }
                }
                if (publisherClient != null) {
                    publisherClient.close();
                }
            } catch (Exception e) {
                System.err.println("资源清理异常: " + e.getMessage());
            }
        }
    }

    @Test
    public void testUnsubscribeFunctionality() throws InterruptedException {
        System.out.println("=== 开始执行取消订阅功能测试 ===");
        final String testChannel = "test:pubsub:unsubscribe";
        final String testMessage1 = "Message Before Unsubscribe";
        final String testMessage2 = "Message After Unsubscribe";
        final CountDownLatch messageReceivedLatch1 = new CountDownLatch(1);
        final CountDownLatch messageReceivedLatch2 = new CountDownLatch(1);
        final AtomicReference<String> receivedMessage1 = new AtomicReference<>();
        final AtomicReference<String> receivedMessage2 = new AtomicReference<>();
        
        LettuceClient publisherClient = new LettuceClient();
        LettuceClient subscriberClient = new LettuceClient();
        
        try {
            // 创建消息监听器
            RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
                @Override
                public void message(String channel, String message) {
                    System.out.println("收到消息 - 频道: " + channel + ", 消息: " + message);
                    if (testChannel.equals(channel)) {
                        if (testMessage1.equals(message) && messageReceivedLatch1.getCount() > 0) {
                            receivedMessage1.set(message);
                            messageReceivedLatch1.countDown();
                        } else if (testMessage2.equals(message) && messageReceivedLatch2.getCount() > 0) {
                            receivedMessage2.set(message);
                            messageReceivedLatch2.countDown();
                        }
                    }
                }
            };
            
            // 订阅频道
            System.out.println("订阅频道: " + testChannel);
            subscriberClient.subscribe(testChannel, listener);
            
            // 等待订阅建立
            Thread.sleep(100);
            
            // 发布第一条消息
            System.out.println("发布第一条消息: " + testMessage1);
            publisherClient.publish(testChannel, testMessage1);
            
            // 等待第一条消息接收
            boolean firstMessageReceived = messageReceivedLatch1.await(5, TimeUnit.SECONDS);
            assertTrue(firstMessageReceived, "应该收到第一条消息");
            assertEquals(testMessage1, receivedMessage1.get(), "第一条消息内容应该正确");
            
            // 取消订阅
            System.out.println("取消订阅频道: " + testChannel);
            subscriberClient.unsubscribe(testChannel);
            
            // 等待取消订阅完成
            Thread.sleep(100);
            
            // 发布第二条消息（应该收不到）
            System.out.println("发布第二条消息: " + testMessage2);
            publisherClient.publish(testChannel, testMessage2);
            
            // 等待一段时间，确保不会收到消息
            Thread.sleep(500);
            
            // 验证没有收到第二条消息
            assertEquals(1, messageReceivedLatch2.getCount(), "取消订阅后不应该收到消息");
            assertNull(receivedMessage2.get(), "取消订阅后不应该收到消息");
            
            System.out.println("=== 取消订阅功能测试完成 ===");
            
        } finally {
            // 清理资源
            try {
                publisherClient.close();
                subscriberClient.close();
            } catch (Exception e) {
                System.err.println("资源清理异常: " + e.getMessage());
            }
        }
    }

    @Test
    public void testConcurrentPublishSubscribe() throws InterruptedException {
        System.out.println("=== 开始执行并发发布/订阅测试 ===");
        final String testChannel = "test:pubsub:concurrent";
        final int messageCount = 10;
        final CountDownLatch allMessagesReceivedLatch = new CountDownLatch(messageCount);
        final int[] receivedMessageCount = new int[1]; // 使用数组来避免final限制
        
        LettuceClient publisherClient = new LettuceClient();
        LettuceClient subscriberClient = new LettuceClient();
        
        try {
            // 创建消息监听器
            RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
                @Override
                public void message(String channel, String message) {
                    if (testChannel.equals(channel)) {
                        System.out.println("收到并发消息: " + message);
                        synchronized (receivedMessageCount) {
                            receivedMessageCount[0]++;
                        }
                        allMessagesReceivedLatch.countDown();
                    }
                }
            };
            
            // 订阅频道
            System.out.println("订阅并发测试频道: " + testChannel);
            subscriberClient.subscribe(testChannel, listener);
            
            // 等待订阅建立
            Thread.sleep(100);
            
            // 并发发布多条消息
            System.out.println("并发发布" + messageCount + "条消息...");
            for (int i = 0; i < messageCount; i++) {
                final String message = "Concurrent Message " + i;
                new Thread(() -> {
                    long subscribers = publisherClient.publish(testChannel, message);
                    System.out.println("发布消息: " + message + " (订阅者数量: " + subscribers + ")");
                }).start();
            }
            
            // 等待所有消息接收完成
            boolean allMessagesReceived = allMessagesReceivedLatch.await(10, TimeUnit.SECONDS);
            assertTrue(allMessagesReceived, "应该在10秒内收到所有" + messageCount + "条消息");
            
            // 验证接收到的消息数量
            assertEquals(messageCount, receivedMessageCount[0], "应该收到所有" + messageCount + "条消息");
            
            System.out.println("=== 并发发布/订阅测试完成 ===");
            
        } finally {
            // 清理资源
            try {
                subscriberClient.unsubscribe(testChannel);
                publisherClient.close();
                subscriberClient.close();
            } catch (Exception e) {
                System.err.println("资源清理异常: " + e.getMessage());
            }
        }
    }
}