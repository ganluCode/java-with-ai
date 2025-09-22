package cn.geekslife.guardedsuspension;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 请求队列类 - 使用Guarded Suspension模式实现
 * 演示经典的生产者-消费者模式
 */
public class RequestQueue {
    // 请求队列
    private final Queue<Request> queue = new LinkedList<>();
    // 队列最大容量
    private final int limit;
    
    /**
     * 默认构造函数，队列容量无限制
     */
    public RequestQueue() {
        this.limit = Integer.MAX_VALUE;
    }
    
    /**
     * 构造函数
     * @param limit 队列最大容量
     */
    public RequestQueue(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("队列容量必须大于0");
        }
        this.limit = limit;
    }
    
    /**
     * 获取请求 - Guarded Suspension模式的核心实现
     * 当队列为空时，线程会等待直到有请求可用
     * @return 请求对象
     * @throws InterruptedException 如果线程被中断
     */
    public synchronized Request getRequest() throws InterruptedException {
        // 使用while循环检查条件，防止虚假唤醒
        while (queue.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + "：请求队列为空，等待新请求...");
            // 释放锁并进入等待状态
            wait();
        }
        
        // 从队列中取出请求
        Request request = queue.poll();
        System.out.println(Thread.currentThread().getName() + "：获取请求 " + request);
        return request;
    }
    
    /**
     * 添加请求 - Guarded Suspension模式的核心实现
     * 当队列满时，生产者线程会等待直到有空间可用
     * @param request 请求对象
     * @throws InterruptedException 如果线程被中断
     */
    public synchronized void putRequest(Request request) throws InterruptedException {
        // 如果队列有容量限制，检查是否已满
        while (queue.size() >= limit) {
            System.out.println(Thread.currentThread().getName() + "：请求队列已满，等待空间...");
            // 释放锁并进入等待状态
            wait();
        }
        
        // 添加请求到队列
        queue.offer(request);
        System.out.println(Thread.currentThread().getName() + "：添加请求 " + request);
        // 通知等待的消费者线程
        notifyAll();
    }
    
    /**
     * 获取队列大小
     * @return 队列大小
     */
    public synchronized int size() {
        return queue.size();
    }
    
    /**
     * 检查队列是否为空
     * @return 是否为空
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}