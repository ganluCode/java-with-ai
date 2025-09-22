package cn.geekslife.producerconsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 使用ReentrantLock和Condition实现的有界缓冲区类
 * 演示如何使用Lock接口实现Producer-Consumer模式
 */
public class BoundedBufferWithLock {
    // 缓冲区队列
    private final Queue<Item> queue = new LinkedList<>();
    // 缓冲区容量
    private final int capacity;
    // 可重入锁
    private final ReentrantLock lock = new ReentrantLock();
    // 非空条件
    private final Condition notEmpty = lock.newCondition();
    // 非满条件
    private final Condition notFull = lock.newCondition();
    
    /**
     * 构造函数
     * @param capacity 缓冲区容量
     */
    public BoundedBufferWithLock(int capacity) {
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
    public void put(Item item) throws InterruptedException {
        lock.lock();
        try {
            // 如果缓冲区已满，等待直到有空间
            while (queue.size() >= capacity) {
                System.out.println(Thread.currentThread().getName() + "：缓冲区已满(" + queue.size() + "/" + capacity + ")，生产者等待...");
                notFull.await();
            }
            
            // 添加数据项到缓冲区
            queue.offer(item);
            System.out.println(Thread.currentThread().getName() + "：生产数据项 " + item);
            
            // 通知等待的消费者线程
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 从缓冲区取出数据项 - 消费者操作
     * @return 数据项
     * @throws InterruptedException 如果线程被中断
     */
    public Item take() throws InterruptedException {
        lock.lock();
        try {
            // 如果缓冲区为空，等待直到有数据
            while (queue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + "：缓冲区为空，消费者等待...");
                notEmpty.await();
            }
            
            // 从缓冲区取出数据项
            Item item = queue.poll();
            System.out.println(Thread.currentThread().getName() + "：消费数据项 " + item);
            
            // 通知等待的生产者线程
            notFull.signal();
            
            return item;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 获取缓冲区当前大小
     * @return 缓冲区大小
     */
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 检查缓冲区是否为空
     * @return 是否为空
     */
    public boolean isEmpty() {
        lock.lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 检查缓冲区是否已满
     * @return 是否已满
     */
    public boolean isFull() {
        lock.lock();
        try {
            return queue.size() >= capacity;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 获取缓冲区容量
     * @return 缓冲区容量
     */
    public int getCapacity() {
        return capacity;
    }
}