package cn.geekslife.workerthread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 通道类 - Worker Thread模式中的任务队列和工作线程管理器
 */
public class Channel {
    // 任务队列
    private final BlockingQueue<Request> queue;
    // 工作线程数组
    private final WorkerThread[] workerThreads;
    // 是否正在运行
    private volatile boolean running = false;
    
    /**
     * 构造函数
     * @param threadsCount 工作线程数量
     */
    public Channel(int threadsCount) {
        if (threadsCount <= 0) {
            throw new IllegalArgumentException("工作线程数量必须大于0");
        }
        
        this.queue = new LinkedBlockingQueue<>();
        this.workerThreads = new WorkerThread[threadsCount];
        
        // 创建工作线程
        for (int i = 0; i < threadsCount; i++) {
            workerThreads[i] = new WorkerThread("WorkerThread-" + i, this);
        }
        
        System.out.println("Channel：创建通道，工作线程数量: " + threadsCount);
    }
    
    /**
     * 启动通道
     */
    public void start() {
        if (running) {
            System.out.println("Channel：通道已在运行中");
            return;
        }
        
        running = true;
        System.out.println("Channel：启动通道");
        
        // 启动所有工作线程
        for (WorkerThread workerThread : workerThreads) {
            workerThread.start();
        }
    }
    
    /**
     * 停止通道
     */
    public void stop() {
        if (!running) {
            System.out.println("Channel：通道未在运行中");
            return;
        }
        
        System.out.println("Channel：停止通道");
        running = false;
        
        // 中断所有工作线程
        for (WorkerThread workerThread : workerThreads) {
            workerThread.interrupt();
        }
        
        // 等待所有工作线程结束
        for (WorkerThread workerThread : workerThreads) {
            try {
                workerThread.join(1000);
                if (workerThread.isAlive()) {
                    System.out.println("Channel：工作线程 " + workerThread.getName() + " 未正常结束");
                }
            } catch (InterruptedException e) {
                System.out.println("Channel：等待工作线程结束时被中断");
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("Channel：通道已停止");
    }
    
    /**
     * 向通道提交请求
     * @param request 请求
     * @throws InterruptedException 如果线程被中断
     */
    public void put(Request request) throws InterruptedException {
        if (!running) {
            throw new IllegalStateException("通道未运行，无法提交请求");
        }
        
        System.out.println("Channel：提交请求到队列 " + request);
        queue.put(request);
    }
    
    /**
     * 从通道获取请求
     * @return 请求
     * @throws InterruptedException 如果线程被中断
     */
    public Request take() throws InterruptedException {
        return queue.take();
    }
    
    /**
     * 获取队列大小
     * @return 队列大小
     */
    public int getQueueSize() {
        return queue.size();
    }
    
    /**
     * 检查通道是否正在运行
     * @return 是否正在运行
     */
    public boolean isRunning() {
        return running;
    }
}