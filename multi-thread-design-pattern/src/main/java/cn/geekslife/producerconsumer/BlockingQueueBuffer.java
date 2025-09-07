package cn.geekslife.producerconsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 使用BlockingQueue实现的生产者-消费者模式
 * 演示如何使用Java内置的阻塞队列实现Producer-Consumer模式
 */
public class BlockingQueueBuffer {
    // 阻塞队列
    private final BlockingQueue<Item> queue;
    
    /**
     * 构造函数
     * @param capacity 缓冲区容量
     */
    public BlockingQueueBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("缓冲区容量必须大于0");
        }
        this.queue = new ArrayBlockingQueue<>(capacity);
    }
    
    /**
     * 向缓冲区添加数据项 - 生产者操作
     * BlockingQueue自动处理阻塞逻辑
     * @param item 数据项
     * @throws InterruptedException 如果线程被中断
     */
    public void put(Item item) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "：生产数据项 " + item);
        // put方法会自动阻塞直到队列有空间
        queue.put(item);
        System.out.println(Thread.currentThread().getName() + "：数据项已放入队列");
    }
    
    /**
     * 从缓冲区取出数据项 - 消费者操作
     * BlockingQueue自动处理阻塞逻辑
     * @return 数据项
     * @throws InterruptedException 如果线程被中断
     */
    public Item take() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "：等待消费数据项...");
        // take方法会自动阻塞直到队列有数据
        Item item = queue.take();
        System.out.println(Thread.currentThread().getName() + "：消费数据项 " + item);
        return item;
    }
    
    /**
     * 尝试向缓冲区添加数据项，不阻塞
     * @param item 数据项
     * @return 是否成功添加
     */
    public boolean tryPut(Item item) {
        boolean result = queue.offer(item);
        if (result) {
            System.out.println(Thread.currentThread().getName() + "：成功添加数据项 " + item);
        } else {
            System.out.println(Thread.currentThread().getName() + "：队列已满，无法添加数据项 " + item);
        }
        return result;
    }
    
    /**
     * 尝试从缓冲区取出数据项，不阻塞
     * @return 数据项，如果队列为空返回null
     */
    public Item tryTake() {
        Item item = queue.poll();
        if (item != null) {
            System.out.println(Thread.currentThread().getName() + "：尝试消费数据项 " + item);
        } else {
            System.out.println(Thread.currentThread().getName() + "：队列为空，无法消费数据项");
        }
        return item;
    }
    
    /**
     * 获取缓冲区当前大小
     * @return 缓冲区大小
     */
    public int size() {
        return queue.size();
    }
    
    /**
     * 检查缓冲区是否为空
     * @return 是否为空
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    /**
     * 检查缓冲区是否已满
     * @return 是否已满
     */
    public boolean isFull() {
        return queue.remainingCapacity() == 0;
    }
    
    /**
     * 获取缓冲区容量
     * @return 缓冲区容量
     */
    public int getCapacity() {
        return queue.size() + queue.remainingCapacity();
    }
}