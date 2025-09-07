package cn.geekslife.rpc.proxy;

import cn.geekslife.rpc.annotation.RpcReference;
import cn.geekslife.rpc.cluster.Cluster;
import cn.geekslife.rpc.cluster.Directory;
import cn.geekslife.rpc.cluster.StaticDirectory;
import cn.geekslife.rpc.common.Invoker;
import cn.geekslife.rpc.common.URL;
import cn.geekslife.rpc.extension.ExtensionLoader;
import cn.geekslife.rpc.registry.Registry;
import cn.geekslife.rpc.registry.RegistryFactory;

import java.util.List;

public class RpcServiceReferer {
    
    public <T> Object referService(Class<T> interfaceClass, RpcReference rpcReference) {
        // 构造注册中心URL
        URL registryUrl = new URL("nacos", "localhost", 8848, "", null);
        
        // 获取注册中心
        RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension("nacos");
        Registry registry = registryFactory.getRegistry(registryUrl);
        
        // 构造服务URL
        URL serviceUrl = new URL("rpc", "", 0, interfaceClass.getName(), null);
        serviceUrl.getParameters().put("interface", interfaceClass.getName());
        serviceUrl.getParameters().put("version", rpcReference.version());
        serviceUrl.getParameters().put("group", rpcReference.group());
        
        // 发现服务
        List<URL> urls = registry.discover(serviceUrl);
        
        // 创建Invoker列表
        // 这里简化处理，实际应该根据URL创建对应的Invoker
        
        // 创建Directory
        Directory<T> directory = new StaticDirectory<>(urls, interfaceClass);
        
        // 获取集群
        Cluster cluster = ExtensionLoader.getExtensionLoader(Cluster.class).getExtension("failover");
        
        // 加入集群
        Invoker<T> invoker = cluster.join(directory);
        
        // 创建代理
        return ProxyFactory.getProxy(interfaceClass, invoker);
    }
}