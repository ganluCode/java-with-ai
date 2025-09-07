package cn.geekslife.workerthread;

/**
 * 工作线程类 - Worker Thread模式中的工作者
 */
public class WorkerThread extends Thread {
    private final Channel channel;
    private volatile boolean running = true;
    
    /**
     * 构造函数
     * @param name 线程名称
     * @param channel 通道
     */
    public WorkerThread(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }
    
    /**
     * 线程主逻辑
     */
    @Override
    public void run() {
        System.out.println(getName() + "：工作线程启动");
        
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    // 从通道获取请求
                    Request request = channel.take();
                    
                    // 执行请求
                    request.execute();
                } catch (InterruptedException e) {
                    System.out.println(getName() + "：工作线程被中断");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            System.out.println(getName() + "：工作线程结束");
        }
    }
    
    /**
     * 停止工作线程
     */
    public void stopWorker() {
        running = false;
        System.out.println(getName() + "：收到停止信号");
    }
}