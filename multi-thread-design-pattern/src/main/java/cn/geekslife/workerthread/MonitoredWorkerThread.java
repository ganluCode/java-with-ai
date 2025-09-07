package cn.geekslife.workerthread;

/**
 * 带监控功能的工作线程类
 * 演示Worker Thread模式的高级特性
 */
public class MonitoredWorkerThread extends Thread {
    private final MonitoredWorkerManager manager;
    private volatile boolean running = true;
    
    /**
     * 构造函数
     * @param name 线程名称
     * @param manager 管理器
     */
    public MonitoredWorkerThread(String name, MonitoredWorkerManager manager) {
        super(name);
        this.manager = manager;
    }
    
    /**
     * 线程主逻辑
     */
    @Override
    public void run() {
        System.out.println(getName() + "：监控工作线程启动");
        
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    // 从管理器获取请求
                    Request request = manager.take();
                    
                    // 执行请求
                    request.execute();
                    
                    // 增加完成任务计数
                    manager.incrementCompletedTasks();
                } catch (InterruptedException e) {
                    System.out.println(getName() + "：监控工作线程被中断");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println(getName() + "：执行请求时发生错误: " + e.getMessage());
                    // 增加失败任务计数
                    manager.incrementFailedTasks();
                }
            }
        } finally {
            System.out.println(getName() + "：监控工作线程结束");
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