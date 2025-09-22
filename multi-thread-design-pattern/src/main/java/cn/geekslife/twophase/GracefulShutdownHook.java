package cn.geekslife.twophase;

/**
 * 优雅关闭钩子类 - 演示Two-Phase Termination模式在JVM关闭时的应用
 */
public class GracefulShutdownHook extends Thread {
    private final Service service;
    
    /**
     * 构造函数
     * @param service 服务
     */
    public GracefulShutdownHook(Service service) {
        this.service = service;
        setName("GracefulShutdownHook");
    }
    
    /**
     * 关闭钩子逻辑
     */
    @Override
    public void run() {
        System.out.println("GracefulShutdownHook：收到JVM关闭信号");
        service.stop();
        System.out.println("GracefulShutdownHook：优雅关闭完成");
    }
}