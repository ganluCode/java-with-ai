package cn.geekslife.activeobject;

/**
 * Active Object实现类 - Active Object模式的完整实现
 */
public class ActiveObjectImpl implements ActiveObject {
    private final Scheduler scheduler;
    private final Servant servant;
    private final Proxy proxy;
    private volatile boolean shutdownRequested = false;
    
    /**
     * 构造函数
     */
    public ActiveObjectImpl() {
        this.servant = new Servant();
        this.scheduler = new Scheduler("ActiveObjectScheduler");
        this.proxy = new Proxy(scheduler, servant);
        
        // 启动调度器
        scheduler.start();
        System.out.println("ActiveObjectImpl：Active Object初始化完成");
    }
    
    /**
     * 执行工作 - 异步方法调用
     * @param name 工作名称
     * @return Future结果对象
     */
    @Override
    public FutureResult doWork(String name) {
        if (shutdownRequested) {
            System.out.println("ActiveObjectImpl：已请求关闭，拒绝新请求 " + name);
            FutureResult future = new FutureResult();
            future.setResult(new Result("ERROR: Active Object已关闭", "SHUTDOWN"));
            return future;
        }
        
        return proxy.doWork(name);
    }
    
    /**
     * 关闭Active Object
     */
    @Override
    public void shutdown() {
        if (shutdownRequested) {
            System.out.println("ActiveObjectImpl：已请求关闭");
            return;
        }
        
        System.out.println("ActiveObjectImpl：开始关闭Active Object");
        shutdownRequested = true;
        
        // 停止调度器
        scheduler.stopScheduler();
        
        try {
            // 等待调度器线程结束
            scheduler.join(5000);
            if (scheduler.isAlive()) {
                System.out.println("ActiveObjectImpl：调度器线程未能及时结束");
            }
        } catch (InterruptedException e) {
            System.out.println("ActiveObjectImpl：等待调度器线程结束时被中断");
            Thread.currentThread().interrupt();
        }
        
        System.out.println("ActiveObjectImpl：Active Object关闭完成");
    }
    
    /**
     * 获取队列大小
     * @return 队列大小
     */
    @Override
    public int getQueueSize() {
        return proxy.getQueueSize();
    }
    
    /**
     * 检查是否已请求关闭
     * @return 是否已请求关闭
     */
    public boolean isShutdownRequested() {
        return shutdownRequested;
    }
    
    /**
     * 获取服务对象的工作计数
     * @return 工作计数
     */
    public int getWorkCount() {
        return servant.getWorkCount();
    }
}