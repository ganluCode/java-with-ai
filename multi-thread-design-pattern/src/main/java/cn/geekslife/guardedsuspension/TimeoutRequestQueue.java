package cn.geekslife.guardedsuspension;

/**
 * 超时请求队列类 - 演示带超时机制的Guarded Suspension模式
 */
public class TimeoutRequestQueue {
    // 请求队列
    private final Request[] queue;
    // 队列头部索引
    private int head = 0;
    // 队列尾部索引
    private int tail = 0;
    // 队列中元素数量
    private int count = 0;
    
    /**
     * 构造函数
     * @param capacity 队列容量
     */
    public TimeoutRequestQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("队列容量必须大于0");
        }
        this.queue = new Request[capacity];
    }
    
    /**
     * 获取请求 - 带超时机制的Guarded Suspension模式
     * @param timeout 超时时间（毫秒）
     * @return 请求对象，如果超时返回null
     * @throws InterruptedException 如果线程被中断
     */
    public synchronized Request getRequest(long timeout) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long remainingTime = timeout;
        
        // 使用while循环检查条件，防止虚假唤醒
        while (count == 0 && remainingTime > 0) {
            System.out.println(Thread.currentThread().getName() + "：请求队列为空，等待新请求... 剩余超时时间: " + remainingTime + "ms");
            // 等待指定时间或直到被唤醒
            wait(remainingTime);
            
            // 计算剩余超时时间
            long elapsed = System.currentTimeMillis() - startTime;
            remainingTime = timeout - elapsed;
        }
        
        // 如果超时且队列仍为空，返回null
        if (count == 0) {
            System.out.println(Thread.currentThread().getName() + "：获取请求超时");
            return null;
        }
        
        // 从队列中取出请求
        Request request = queue[head];
        queue[head] = null; // 避免内存泄漏
        head = (head + 1) % queue.length;
        count--;
        System.out.println(Thread.currentThread().getName() + "：获取请求 " + request);
        // 通知等待的生产者线程
        notifyAll();
        return request;
    }
    
    /**
     * 获取请求 - 不超时版本
     * @return 请求对象
     * @throws InterruptedException 如果线程被中断
     */
    public synchronized Request getRequest() throws InterruptedException {
        return getRequest(0); // 0表示无限等待
    }
    
    /**
     * 添加请求 - 带超时机制的Guarded Suspension模式
     * @param request 请求对象
     * @param timeout 超时时间（毫秒）
     * @return 是否成功添加
     * @throws InterruptedException 如果线程被中断
     */
    public synchronized boolean putRequest(Request request, long timeout) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long remainingTime = timeout;
        
        // 如果队列有容量限制，检查是否已满
        while (count == queue.length && remainingTime > 0) {
            System.out.println(Thread.currentThread().getName() + "：请求队列已满，等待空间... 剩余超时时间: " + remainingTime + "ms");
            // 等待指定时间或直到被唤醒
            wait(remainingTime);
            
            // 计算剩余超时时间
            long elapsed = System.currentTimeMillis() - startTime;
            remainingTime = timeout - elapsed;
        }
        
        // 如果超时且队列仍满，返回false
        if (count == queue.length) {
            System.out.println(Thread.currentThread().getName() + "：添加请求超时，队列已满");
            return false;
        }
        
        // 添加请求到队列
        queue[tail] = request;
        tail = (tail + 1) % queue.length;
        count++;
        System.out.println(Thread.currentThread().getName() + "：添加请求 " + request);
        // 通知等待的消费者线程
        notifyAll();
        return true;
    }
    
    /**
     * 添加请求 - 不超时版本
     * @param request 请求对象
     * @throws InterruptedException 如果线程被中断
     */
    public synchronized void putRequest(Request request) throws InterruptedException {
        putRequest(request, 0); // 0表示无限等待
    }
    
    /**
     * 获取队列大小
     * @return 队列大小
     */
    public synchronized int size() {
        return count;
    }
    
    /**
     * 检查队列是否为空
     * @return 是否为空
     */
    public synchronized boolean isEmpty() {
        return count == 0;
    }
    
    /**
     * 检查队列是否已满
     * @return 是否已满
     */
    public synchronized boolean isFull() {
        return count == queue.length;
    }
}