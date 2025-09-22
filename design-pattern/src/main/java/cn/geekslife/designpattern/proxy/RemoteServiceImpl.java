package cn.geekslife.designpattern.proxy;

/**
 * 远程服务实现类
 */
public class RemoteServiceImpl implements RemoteService {
    private String remoteData;
    
    public RemoteServiceImpl() {
        // 模拟远程服务初始化
        System.out.println("初始化远程服务...");
        try {
            Thread.sleep(2000); // 模拟网络延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.remoteData = "远程数据";
        System.out.println("远程服务初始化完成");
    }
    
    @Override
    public void remoteRequest() {
        System.out.println("远程服务处理请求");
    }
    
    @Override
    public String getRemoteData() {
        return remoteData;
    }
}