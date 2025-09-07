package cn.geekslife.producerconsumer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 有界缓冲区类 - 使用wait/notify机制实现Producer-Consumer模式
 */
public class BoundedBuffer {
    // 缓冲区队列
    private final Queue<Item> queue = new LinkedList<>();
    // 缓冲区容量
    private final int capacity;
    
    /**
     * 构造函数
     * @param capacity 缓冲区容量
     */
    public BoundedBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("缓冲区容量必须大于0");
        }
        this.capacity = capacity;
    }
    
    /**
     * 向缓冲区添加数据项 - 生产者操作
     * @param item 数据项
     * @throws InterruptedException 如果线程被中断
     */
    public synchronized void put(Item item) throws InterruptedException {
        // 如果缓冲区已满，等待直到有空间
        while (queue.size() >= capacity) {
            System.out.println(Thread.currentThread().getName() + "：缓冲区已满(" + queue.size() + "/" + capacity + ")，生产者等待...");
            wait();
        }
        
        // 添加数据项到缓冲区
        queue.offer(item);
        System.out.println(Thread.currentThread().getName() + "：生产数据项 " + item);
        
        // 通知等待的消费者线程
        notifyAll();
    }
    
    /**
     * 从缓冲区取出数据项 - 消费者操作
     * @return 数据项
     * @throws InterruptedException 如果线程被中断
     */
    public synchronized Item take() throws InterruptedException {
        // 如果缓冲区为空，等待直到有数据
        while (queue.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + "：缓冲区为空，消费者等待...");
            wait();
        }
        
        // 从缓冲区取出数据项
        Item item = queue.poll();
        System.out.println(Thread.currentThread().getName() + "：消费数据项 " + item);
        
        // 通知等待的生产者线程
        notifyAll();
        
        return item;
    }
    
    /**
     * 获取缓冲区当前大小
     * @return 缓冲区大小
     */
    public synchronized int size() {
        return queue.size();
    }
    
    /**
     * 检查缓冲区是否为空
     * @return 是否为空
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
    
    /**
     * 检查缓冲区是否已满
     * @return 是否已满
     */
    public synchronized boolean isFull() {
        return queue.size() >= capacity;
    }
    
    /**
     * 获取缓冲区容量
     * @return 缓冲区容量
     */
    public int getCapacity() {
        return capacity;
    }
}