package cn.geekslife.designpattern.proxy;

/**
 * 远程服务接口
 */
public interface RemoteService {
    /**
     * 远程请求方法
     */
    void remoteRequest();
    
    /**
     * 获取远程数据
     * @return 数据
     */
    String getRemoteData();
}