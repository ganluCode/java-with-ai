package cn.geekslife.activeobject;

/**
 * 代理对象类 - Active Object模式中的客户端接口
 */
public class Proxy {
    private final Scheduler scheduler;
    private final Servant servant;
    
    /**
     * 构造函数
     * @param scheduler 调度器
     * @param servant 服务对象
     */
    public Proxy(Scheduler scheduler, Servant servant) {
        this.scheduler = scheduler;
        this.servant = servant;
    }
    
    /**
     * 执行工作 - 异步方法调用
     * @param name 工作名称
     * @return Future结果对象
     */
    public FutureResult doWork(String name) {
        System.out.println("Proxy：接收到工作请求 " + name);
        
        // 创建Future对象
        FutureResult future = new FutureResult();
        
        // 创建任务ID
        String taskId = "TASK-" + System.currentTimeMillis() + "-" + name.hashCode();
        
        // 创建方法请求
        MethodRequest request = new DoWorkRequest(servant, name, future, taskId);
        
        try {
            // 将请求加入调度队列
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            System.err.println("Proxy：请求入队被中断 " + name);
            Thread.currentThread().interrupt();
            // 在实际应用中，可能需要设置异常结果
            future.setResult(new Result("ERROR: 请求入队被中断", taskId));
        }
        
        System.out.println("Proxy：返回Future对象给客户端 " + name);
        return future;
    }
    
    /**
     * 获取调度器队列大小
     * @return 队列大小
     */
    public int getQueueSize() {
        return scheduler.getQueueSize();
    }
}