package com.example.redis.lock;

import io.lettuce.core.pubsub.RedisPubSubAdapter;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Redis发布/订阅功能使用示例
 * 
 * 演示如何使用LettuceClient的发布/订阅功能
 */
public class RedisPubSubExample {

    public static void main(String[] args) {
        System.out.println("=== Redis发布/订阅功能使用示例 ===");
        
        // 演示基础发布/订阅功能
        basicPubSubExample();
        
        // 演示聊天室示例
        chatRoomExample();
        
        System.out.println("=== Redis发布/订阅功能使用示例完成 ===");
    }
    
    /**
     * 基础发布/订阅功能示例
     */
    private static void basicPubSubExample() {
        System.out.println("\n--- 基础发布/订阅功能示例 ---");
        
        final String channel = "example:basic";
        final CountDownLatch messageLatch = new CountDownLatch(1);
        
        // 创建发布者和订阅者客户端
        LettuceClient publisher = new LettuceClient();
        LettuceClient subscriber = new LettuceClient();
        
        try {
            // 创建订阅监听器
            RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
                @Override
                public void message(String ch, String message) {
                    if (channel.equals(ch)) {
                        System.out.println("收到消息: " + message);
                        messageLatch.countDown();
                    }
                }
                
                @Override
                public void subscribed(String ch, long count) {
                    if (channel.equals(ch)) {
                        System.out.println("成功订阅频道: " + ch + " (订阅者数量: " + count + ")");
                    }
                }
            };
            
            // 订阅频道
            System.out.println("订阅频道: " + channel);
            subscriber.subscribe(channel, listener);
            
            // 等待订阅建立
            Thread.sleep(100);
            
            // 发布消息
            String message = "Hello Redis Pub/Sub!";
            System.out.println("发布消息: " + message);
            long subscribers = publisher.publish(channel, message);
            System.out.println("消息发布完成，订阅者数量: " + subscribers);
            
            // 等待消息接收
            messageLatch.await();
            
            // 取消订阅
            System.out.println("取消订阅频道: " + channel);
            subscriber.unsubscribe(channel);
            
        } catch (Exception e) {
            System.err.println("基础发布/订阅示例异常: " + e.getMessage());
        } finally {
            // 关闭客户端
            publisher.close();
            subscriber.close();
        }
    }
    
    /**
     * 聊天室示例
     */
    private static void chatRoomExample() {
        System.out.println("\n--- 聊天室示例 ---");
        
        final String chatChannel = "chat:room:general";
        final ExecutorService executor = Executors.newCachedThreadPool();
        final CountDownLatch startLatch = new CountDownLatch(1);
        
        try {
            // 创建聊天室参与者
            ChatParticipant alice = new ChatParticipant("Alice", chatChannel);
            ChatParticipant bob = new ChatParticipant("Bob", chatChannel);
            ChatParticipant charlie = new ChatParticipant("Charlie", chatChannel);
            
            // 启动聊天参与者
            executor.submit(alice);
            executor.submit(bob);
            executor.submit(charlie);
            
            // 等待所有参与者准备就绪
            Thread.sleep(500);
            startLatch.countDown();
            
            // 模拟聊天过程
            Thread.sleep(1000);
            alice.sendMessage("大家好！");
            
            Thread.sleep(1000);
            bob.sendMessage("你好Alice！");
            
            Thread.sleep(1000);
            charlie.sendMessage("欢迎加入聊天室！");
            
            Thread.sleep(1000);
            alice.sendMessage("很高兴认识你们！");
            
            Thread.sleep(2000);
            
            // 结束聊天
            alice.stop();
            bob.stop();
            charlie.stop();
            
            executor.shutdown();
            
        } catch (Exception e) {
            System.err.println("聊天室示例异常: " + e.getMessage());
        }
    }
    
    /**
     * 聊天参与者类
     */
    static class ChatParticipant implements Runnable {
        private final String name;
        private final String channel;
        private final LettuceClient client;
        private volatile boolean running = true;
        private RedisPubSubAdapter<String, String> listener;
        
        public ChatParticipant(String name, String channel) {
            this.name = name;
            this.channel = channel;
            this.client = new LettuceClient();
        }
        
        @Override
        public void run() {
            try {
                // 创建消息监听器
                listener = new RedisPubSubAdapter<String, String>() {
                    @Override
                    public void message(String ch, String message) {
                        if (channel.equals(ch)) {
                            System.out.println("[" + name + "] 收到消息: " + message);
                        }
                    }
                    
                    @Override
                    public void subscribed(String ch, long count) {
                        if (channel.equals(ch)) {
                            System.out.println("[" + name + "] 加入聊天室");
                        }
                    }
                };
                
                // 订阅聊天频道
                client.subscribe(channel, listener);
                
                // 持续监听消息
                while (running) {
                    Thread.sleep(100);
                }
                
            } catch (Exception e) {
                if (running) {
                    System.err.println("[" + name + "] 聊天参与者异常: " + e.getMessage());
                }
            }
        }
        
        /**
         * 发送消息
         */
        public void sendMessage(String message) {
            try {
                String fullMessage = name + ": " + message;
                long subscribers = client.publish(channel, fullMessage);
                System.out.println("[" + name + "] 发送消息: " + message + " (订阅者数量: " + subscribers + ")");
            } catch (Exception e) {
                System.err.println("[" + name + "] 发送消息异常: " + e.getMessage());
            }
        }
        
        /**
         * 停止参与者
         */
        public void stop() {
            running = false;
            try {
                if (client != null) {
                    client.unsubscribe(channel);
                    client.close();
                }
            } catch (Exception e) {
                System.err.println("[" + name + "] 停止参与者异常: " + e.getMessage());
            }
        }
    }
    
    /**
     * 交互式聊天示例
     */
    private static void interactiveChatExample() {
        System.out.println("\n--- 交互式聊天示例 ---");
        System.out.println("输入消息进行聊天，输入'quit'退出");
        
        final String chatChannel = "chat:interactive";
        final LettuceClient publisher = new LettuceClient();
        final LettuceClient subscriber = new LettuceClient();
        final Scanner scanner = new Scanner(System.in);
        
        try {
            // 创建消息监听器
            RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
                @Override
                public void message(String channel, String message) {
                    System.out.println("收到: " + message);
                }
            };
            
            // 订阅频道
            subscriber.subscribe(chatChannel, listener);
            System.out.println("已加入聊天频道: " + chatChannel);
            
            // 交互式发送消息
            String input;
            while (!(input = scanner.nextLine()).equals("quit")) {
                if (!input.trim().isEmpty()) {
                    publisher.publish(chatChannel, "You: " + input);
                }
            }
            
        } catch (Exception e) {
            System.err.println("交互式聊天示例异常: " + e.getMessage());
        } finally {
            // 清理资源
            try {
                subscriber.unsubscribe(chatChannel);
                publisher.close();
                subscriber.close();
                scanner.close();
            } catch (Exception e) {
                System.err.println("资源清理异常: " + e.getMessage());
            }
        }
    }
}