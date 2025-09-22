package cn.geekslife.designpattern.proxy;

/**
 * 客户端类
 * 使用代理模式的客户端
 */
public class Client {
    public static void main(String[] args) {
        System.out.println("=== 虚拟代理测试 ===");
        Subject virtualProxy = new VirtualProxy();
        virtualProxy.request();
        System.out.println("数据: " + virtualProxy.getData());
        
        System.out.println("\n=== 保护代理测试 ===");
        Subject protectionProxy = new ProtectionProxy("admin");
        protectionProxy.request();
        System.out.println("数据: " + protectionProxy.getData());
        
        Subject protectionProxy2 = new ProtectionProxy("guest");
        protectionProxy2.request();
        System.out.println("数据: " + protectionProxy2.getData());
        
        System.out.println("\n=== 智能引用代理测试 ===");
        SmartReferenceProxy smartProxy = new SmartReferenceProxy();
        smartProxy.request();
        System.out.println("数据: " + smartProxy.getData());
        smartProxy.request();
        System.out.println("引用次数: " + smartProxy.getReferenceCount());
        
        System.out.println("\n=== 缓存代理测试 ===");
        CacheProxy cacheProxy = new CacheProxy();
        System.out.println("第一次获取数据: " + cacheProxy.getData());
        System.out.println("第二次获取数据: " + cacheProxy.getData());
        System.out.println("缓存大小: " + cacheProxy.getCacheSize());
        
        System.out.println("\n=== 日志代理测试 ===");
        Subject loggingProxy = new LoggingProxy();
        loggingProxy.request();
        System.out.println("数据: " + loggingProxy.getData());
        
        System.out.println("\n=== 远程代理测试 ===");
        RemoteService remoteProxy = new RemoteProxy();
        remoteProxy.remoteRequest();
        System.out.println("远程数据: " + remoteProxy.getRemoteData());
    }
}