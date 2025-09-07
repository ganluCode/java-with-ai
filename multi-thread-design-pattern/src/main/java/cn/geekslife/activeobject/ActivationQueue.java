package cn.geekslife.activeobject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 激活队列类 - Active Object模式中的请求队列
 */
public class ActivationQueue {
    // 请求队列
    private final BlockingQueue<MethodRequest> queue = new LinkedBlockingQueue<>();
    
    /**
     * 将方法请求加入队列
     * @param request 方法请求
     * @throws InterruptedException 如果线程被中断
     */
    public void enqueue(MethodRequest request) throws InterruptedException {
        System.out.println("ActivationQueue：请求入队 " + request.getClass().getSimpleName());
        queue.put(request);
    }
    
    /**
     * 从队列中取出方法请求
     * @return 方法请求
     * @throws InterruptedException 如果线程被中断
     */
    public MethodRequest dequeue() throws InterruptedException {
        System.out.println("ActivationQueue：等待请求出队...");
        MethodRequest request = queue.take();
        System.out.println("ActivationQueue：请求出队 " + request.getClass().getSimpleName());
        return request;
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
    
    /**
     * 清空队列
     */
    public void clear() {
        int clearedCount = queue.drainTo(new java.util.ArrayList<>());
        System.out.println("ActivationQueue：清空队列，清除 " + clearedCount + " 个请求");
    }
}