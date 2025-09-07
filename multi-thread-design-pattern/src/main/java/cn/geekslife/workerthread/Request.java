package cn.geekslife.workerthread;

/**
 * 请求类 - Worker Thread模式中的任务载体
 */
public class Request {
    private final String name;
    private final long timestamp;
    private final int requestId;
    
    /**
     * 构造函数
     * @param name 请求名称
     * @param requestId 请求ID
     */
    public Request(String name, int requestId) {
        this.name = name;
        this.requestId = requestId;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 执行请求 - 模拟业务逻辑处理
     */
    public void execute() {
        System.out.println(Thread.currentThread().getName() + "：开始执行请求 [" + requestId + "] - " + name);
        
        try {
            // 模拟处理耗时
            long processingTime = (long) (Math.random() * 1000) + 100;
            Thread.sleep(processingTime);
            
            System.out.println(Thread.currentThread().getName() + "：请求 [" + requestId + "] 执行完成，耗时 " + processingTime + "ms");
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "：请求 [" + requestId + "] 执行被中断");
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 获取请求名称
     * @return 请求名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取时间戳
     * @return 时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * 获取请求ID
     * @return 请求ID
     */
    public int getRequestId() {
        return requestId;
    }
    
    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "Request{id=" + requestId + ", name='" + name + "', timestamp=" + timestamp + "}";
    }
}