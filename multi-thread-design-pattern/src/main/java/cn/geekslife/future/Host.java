package cn.geekslife.future;

import java.util.concurrent.Callable;

/**
 * 使用Java内置Future的主机类
 * 演示Future模式的标准实现
 */
public class Host {
    
    /**
     * 请求数据处理 - 返回Future对象
     * @param data 原始数据
     * @return FutureData对象
     */
    public FutureData request(final String data) {
        System.out.println("Host：接收到请求 - " + data);
        
        // 创建FutureData作为占位符
        final FutureData future = new FutureData();
        
        // 启动新线程处理耗时操作
        new Thread(() -> {
            // 创建真实数据
            RealData realData = new RealData(data);
            // 设置真实数据到Future
            future.setRealData(realData);
        }, "DataProcessor").start();
        
        return future;
    }
    
    /**
     * 使用Callable的请求处理
     * @param data 原始数据
     * @return Callable对象
     */
    public Callable<String> requestCallable(final String data) {
        return () -> {
            System.out.println("Host：Callable接收到请求 - " + data);
            RealData realData = new RealData(data);
            return realData.getContent();
        };
    }
}