package cn.geekslife.guardedsuspension;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 使用ReentrantLock和Condition实现的请求队列类
 * 演示如何使用Lock接口实现Guarded Suspension模式
 */
public class RequestQueueWithLock {
    // 请求队列
    private final Queue<Request> queue = new LinkedList<>();
    // 队列最大容量
    private final int limit;
    // 可重入锁
    private final ReentrantLock lock = new ReentrantLock();
    // 队列非空条件
    private final Condition notEmpty = lock.newCondition();
    // 队列非满条件
    private final Condition notFull = lock.newCondition();
    
    /**
     * 默认构造函数，队列容量无限制
     */
    public RequestQueueWithLock() {
        this.limit = Integer.MAX_VALUE;
    }
    
    /**
     * 构造函数
     * @param limit 队列最大容量
     */
    public RequestQueueWithLock(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("队列容量必须大于0");
        }
        this.limit = limit;
    }
    
    /**
     * 获取请求 - 使用ReentrantLock和Condition实现Guarded Suspension模式
     * @return 请求对象
     * @throws InterruptedException 如果线程被中断
     */
    public Request getRequest() throws InterruptedException {
        lock.lock();
        try {
            // 使用while循环检查条件，防止虚假唤醒
            while (queue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + "：请求队列为空，等待新请求...");
                // 等待队列非空条件
                notEmpty.await();
            }
            
            // 从队列中取出请求
            Request request = queue.poll();
            System.out.println(Thread.currentThread().getName() + "：获取请求 " + request);
            // 通知等待的生产者线程
            notFull.signal();
            return request;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 添加请求 - 使用ReentrantLock和Condition实现Guarded Suspension模式
     * @param request 请求对象
     * @throws InterruptedException 如果线程被中断
     */
    public void putRequest(Request request) throws InterruptedException {
        lock.lock();
        try {
            // 如果队列有容量限制，检查是否已满
            while (queue.size() >= limit) {
                System.out.println(Thread.currentThread().getName() + "：请求队列已满，等待空间...");
                // 等待队列非满条件
                notFull.await();
            }
            
            // 添加请求到队列
            queue.offer(request);
            System.out.println(Thread.currentThread().getName() + "：添加请求 " + request);
            // 通知等待的消费者线程
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 获取队列大小
     * @return 队列大小
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
     * 检查队列是否为空
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
}