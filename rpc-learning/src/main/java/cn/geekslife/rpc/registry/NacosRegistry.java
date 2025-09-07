package cn.geekslife.rpc.registry;

import cn.geekslife.rpc.common.URL;
import cn.geekslife.rpc.common.RpcException;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.ArrayList;
import java.util.List;

public class NacosRegistry implements Registry {
    
    private final NamingService namingService;
    private final URL registryUrl;
    
    public NacosRegistry(URL url) {
        this.registryUrl = url;
        // 初始化Nacos NamingService
        this.namingService = createNamingService(url);
    }
    
    private NamingService createNamingService(URL url) {
        try {
            return NamingFactory.createNamingService(url.getHost() + ":" + url.getPort());
        } catch (NacosException e) {
            throw new RpcException("Failed to create nacos naming service", e);
        }
    }
    
    @Override
    public void register(URL url) {
        try {
            // 构造Nacos服务实例
            Instance instance = new Instance();
            instance.setIp(url.getHost());
            instance.setPort(url.getPort());
            instance.setServiceName(url.getPath());
            instance.setMetadata(url.getParameters());
            
            // 注册服务
            namingService.registerInstance(url.getServiceInterface(), instance);
        } catch (Exception e) {
            throw new RpcException("Failed to register service to nacos", e);
        }
    }
    
    @Override
    public void unregister(URL url) {
        try {
            // 构造Nacos服务实例
            Instance instance = new Instance();
            instance.setIp(url.getHost());
            instance.setPort(url.getPort());
            instance.setServiceName(url.getPath());
            
            // 注销服务
            namingService.deregisterInstance(url.getServiceInterface(), instance);
        } catch (Exception e) {
            throw new RpcException("Failed to unregister service from nacos", e);
        }
    }
    
    @Override
    public List<URL> discover(URL url) {
        try {
            // 发现服务
            List<Instance> instances = namingService.getAllInstances(url.getServiceInterface());
            List<URL> urls = new ArrayList<>();
            for (Instance instance : instances) {
                URL serviceUrl = new URL(
                    registryUrl.getProtocol(),
                    instance.getIp(),
                    instance.getPort(),
                    url.getPath(),
                    instance.getMetadata()
                );
                urls.add(serviceUrl);
            }
            return urls;
        } catch (Exception e) {
            throw new RpcException("Failed to discover service from nacos", e);
        }
    }
}