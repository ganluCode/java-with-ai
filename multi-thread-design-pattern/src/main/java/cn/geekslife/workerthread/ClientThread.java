package cn.geekslife.workerthread;

/**
 * 客户端线程类 - Worker Thread模式中的任务提交者
 */
public class ClientThread extends Thread {
    private final String name;
    private final Channel channel;
    private final int requestCount;
    private volatile boolean running = true;
    
    /**
     * 构造函数
     * @param name 线程名称
     * @param channel 通道
     * @param requestCount 请求数量
     */
    public ClientThread(String name, Channel channel, int requestCount) {
        super(name);
        this.name = name;
        this.channel = channel;
        this.requestCount = requestCount;
    }
    
    /**
     * 线程主逻辑
     */
    @Override
    public void run() {
        System.out.println(getName() + "：客户端线程启动，计划提交 " + requestCount + " 个请求");
        
        try {
            for (int i = 0; i < requestCount && running; i++) {
                // 创建请求
                Request request = new Request(name + "-Request-" + i, i);
                
                // 提交请求到通道
                channel.put(request);
                
                // 随机间隔
                Thread.sleep((long) (Math.random() * 200));
            }
            
            System.out.println(getName() + "：客户端线程完成，共提交 " + requestCount + " 个请求");
        } catch (InterruptedException e) {
            System.out.println(getName() + "：客户端线程被中断");
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 停止客户端线程
     */
    public void stopClient() {
        running = false;
        System.out.println(getName() + "：收到停止信号");
    }
}