package cn.geekslife.twophase;

/**
 * 服务类 - 演示Two-Phase Termination模式在服务管理中的应用
 */
public class Service {
    private final Worker worker;
    private final TaskProcessor taskProcessor;
    private volatile boolean running = false;
    
    /**
     * 构造函数
     */
    public Service() {
        this.worker = new Worker();
        this.taskProcessor = new TaskProcessor();
        worker.setName("WorkerThread");
        taskProcessor.setName("TaskProcessorThread");
    }
    
    /**
     * 启动服务
     */
    public void start() {
        if (running) {
            System.out.println("Service：服务已在运行中");
            return;
        }
        
        System.out.println("Service：启动服务");
        running = true;
        
        worker.start();
        taskProcessor.start();
        
        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new GracefulShutdownHook(this));
    }
    
    /**
     * 停止服务
     */
    public void stop() {
        if (!running) {
            System.out.println("Service：服务未在运行中");
            return;
        }
        
        System.out.println("Service：停止服务");
        running = false;
        
        // 优雅地终止工作线程
        worker.shutdown();
        taskProcessor.shutdown();
        
        // 等待线程终止
        try {
            worker.join(5000);
            taskProcessor.join(5000);
            
            if (worker.isAlive()) {
                System.out.println("Service：Worker线程未能及时终止");
            }
            
            if (taskProcessor.isAlive()) {
                System.out.println("Service：TaskProcessor线程未能及时终止");
            }
        } catch (InterruptedException e) {
            System.out.println("Service：等待线程终止时被中断");
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Service：服务已停止");
    }
    
    /**
     * 提交任务到服务
     * @param task 任务
     */
    public void submitTask(Runnable task) {
        if (running) {
            taskProcessor.submit(task);
        } else {
            System.out.println("Service：服务未运行，无法提交任务");
        }
    }
    
    /**
     * 检查服务是否正在运行
     * @return 是否正在运行
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * 获取工作线程
     * @return 工作线程
     */
    public Worker getWorker() {
        return worker;
    }
    
    /**
     * 获取任务处理器
     * @return 任务处理器
     */
    public TaskProcessor getTaskProcessor() {
        return taskProcessor;
    }
}