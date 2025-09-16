package cn.geekslife.designpattern.proxy;

/**
 * 远程代理类
 * 为远程服务提供本地代表
 */
public class RemoteProxy implements RemoteService {
    private RemoteServiceImpl remoteService;
    
    @Override
    public void remoteRequest() {
        // 模拟网络通信
        System.out.println("建立网络连接...");
        try {
            Thread.sleep(500); // 模拟网络延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        if (remoteService == null) {
            remoteService = new RemoteServiceImpl();
        }
        remoteService.remoteRequest();
        System.out.println("关闭网络连接");
    }
    
    @Override
    public String getRemoteData() {
        // 模拟网络通信
        System.out.println("建立网络连接获取数据...");
        try {
            Thread.sleep(300); // 模拟网络延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        if (remoteService == null) {
            remoteService = new RemoteServiceImpl();
        }
        String data = remoteService.getRemoteData();
        System.out.println("关闭网络连接");
        return data;
    }
}