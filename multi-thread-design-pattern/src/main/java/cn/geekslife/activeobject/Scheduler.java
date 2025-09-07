package cn.geekslife.activeobject;

/**
 * 调度器类 - Active Object模式中的请求调度器
 */
public class Scheduler extends Thread {
    private final ActivationQueue queue;
    private volatile boolean running = true;
    
    /**
     * 构造函数
     * @param name 线程名称
     */
    public Scheduler(String name) {
        super(name);
        this.queue = new ActivationQueue();
    }
    
    /**
     * 线程主逻辑
     */
    @Override
    public void run() {
        System.out.println(getName() + "：调度器启动");
        
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    // 从队列中取出请求并执行
                    MethodRequest request = queue.dequeue();
                    request.execute();
                } catch (InterruptedException e) {
                    System.out.println(getName() + "：调度器被中断");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            System.out.println(getName() + "：调度器停止");
        }
    }
    
    /**
     * 将方法请求加入调度队列
     * @param request 方法请求
     * @throws InterruptedException 如果线程被中断
     */
    public void enqueue(MethodRequest request) throws InterruptedException {
        queue.enqueue(request);
    }
    
    /**
     * 停止调度器
     */
    public void stopScheduler() {
        System.out.println(getName() + "：收到停止信号");
        running = false;
        interrupt();
    }
    
    /**
     * 获取队列大小
     * @return 队列大小
     */
    public int getQueueSize() {
        return queue.size();
    }
    
    /**
     * 清空队列
     */
    public void clearQueue() {
        queue.clear();
    }
}