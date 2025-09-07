package cn.geekslife.workerthread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 带监控功能的工作线程管理器
 * 演示Worker Thread模式的高级特性
 */
public class MonitoredWorkerManager {
    // 任务队列
    private final BlockingQueue<Request> queue;
    // 工作线程数组
    private final MonitoredWorkerThread[] workerThreads;
    // 是否正在运行
    private volatile boolean running = false;
    // 完成的任务计数
    private final AtomicInteger completedTasks = new AtomicInteger(0);
    // 失败的任务计数
    private final AtomicInteger failedTasks = new AtomicInteger(0);
    
    /**
     * 构造函数
     * @param threadsCount 工作线程数量
     */
    public MonitoredWorkerManager(int threadsCount) {
        if (threadsCount <= 0) {
            throw new IllegalArgumentException("工作线程数量必须大于0");
        }
        
        this.queue = new LinkedBlockingQueue<>();
        this.workerThreads = new MonitoredWorkerThread[threadsCount];
        
        // 创建工作线程
        for (int i = 0; i < threadsCount; i++) {
            workerThreads[i] = new MonitoredWorkerThread("MonitoredWorker-" + i, this);
        }
        
        System.out.println("MonitoredWorkerManager：创建监控工作线程管理器，工作线程数量: " + threadsCount);
    }
    
    /**
     * 启动管理器
     */
    public void start() {
        if (running) {
            System.out.println("MonitoredWorkerManager：管理器已在运行中");
            return;
        }
        
        running = true;
        System.out.println("MonitoredWorkerManager：启动管理器");
        
        // 启动所有工作线程
        for (MonitoredWorkerThread workerThread : workerThreads) {
            workerThread.start();
        }
    }
    
    /**
     * 停止管理器
     */
    public void stop() {
        if (!running) {
            System.out.println("MonitoredWorkerManager：管理器未在运行中");
            return;
        }
        
        System.out.println("MonitoredWorkerManager：停止管理器");
        running = false;
        
        // 中断所有工作线程
        for (MonitoredWorkerThread workerThread : workerThreads) {
            workerThread.interrupt();
        }
    }
    
    /**
     * 向队列提交请求
     * @param request 请求
     * @throws InterruptedException 如果线程被中断
     */
    public void put(Request request) throws InterruptedException {
        if (!running) {
            throw new IllegalStateException("管理器未运行，无法提交请求");
        }
        
        System.out.println("MonitoredWorkerManager：提交请求到队列 " + request);
        queue.put(request);
    }
    
    /**
     * 从队列获取请求
     * @return 请求
     * @throws InterruptedException 如果线程被中断
     */
    public Request take() throws InterruptedException {
        return queue.take();
    }
    
    /**
     * 增加完成任务计数
     */
    public void incrementCompletedTasks() {
        completedTasks.incrementAndGet();
    }
    
    /**
     * 增加失败任务计数
     */
    public void incrementFailedTasks() {
        failedTasks.incrementAndGet();
    }
    
    /**
     * 获取队列大小
     * @return 队列大小
     */
    public int getQueueSize() {
        return queue.size();
    }
    
    /**
     * 获取完成的任务数
     * @return 完成的任务数
     */
    public int getCompletedTasks() {
        return completedTasks.get();
    }
    
    /**
     * 获取失败的任务数
     * @return 失败的任务数
     */
    public int getFailedTasks() {
        return failedTasks.get();
    }
    
    /**
     * 获取总的任务数
     * @return 总的任务数
     */
    public int getTotalTasks() {
        return completedTasks.get() + failedTasks.get();
    }
    
    /**
     * 检查管理器是否正在运行
     * @return 是否正在运行
     */
    public boolean isRunning() {
        return running;
    }
}