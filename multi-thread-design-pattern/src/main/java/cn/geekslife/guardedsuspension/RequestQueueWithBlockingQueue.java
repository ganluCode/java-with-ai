package cn.geekslife.guardedsuspension;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 使用BlockingQueue实现的请求队列类
 * 演示如何使用Java内置的阻塞队列实现Guarded Suspension模式
 */
public class RequestQueueWithBlockingQueue {
    // 阻塞队列
    private final BlockingQueue<Request> queue;
    
    /**
     * 默认构造函数，使用无界队列
     */
    public RequestQueueWithBlockingQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }
    
    /**
     * 构造函数
     * @param capacity 队列容量
     */
    public RequestQueueWithBlockingQueue(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
    }
    
    /**
     * 获取请求 - BlockingQueue自动实现Guarded Suspension模式
     * @return 请求对象
     * @throws InterruptedException 如果线程被中断
     */
    public Request getRequest() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "：等待获取请求...");
        // take()方法会自动阻塞直到队列非空
        Request request = queue.take();
        System.out.println(Thread.currentThread().getName() + "：获取请求 " + request);
        return request;
    }
    
    /**
     * 添加请求 - BlockingQueue自动实现Guarded Suspension模式
     * @param request 请求对象
     * @throws InterruptedException 如果线程被中断
     */
    public void putRequest(Request request) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "：添加请求 " + request);
        // put()方法会自动阻塞直到队列有空间
        queue.put(request);
    }
    
    /**
     * 尝试添加请求，不阻塞
     * @param request 请求对象
     * @return 是否成功添加
     */
    public boolean tryPutRequest(Request request) {
        boolean result = queue.offer(request);
        if (result) {
            System.out.println(Thread.currentThread().getName() + "：成功添加请求 " + request);
        } else {
            System.out.println(Thread.currentThread().getName() + "：队列已满，无法添加请求 " + request);
        }
        return result;
    }
    
    /**
     * 获取队列大小
     * @return 队列大小
     */
    public int size() {
        return queue.size();
    }
    
    /**
     * 检查队列是否为空
     * @return 是否为空
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}