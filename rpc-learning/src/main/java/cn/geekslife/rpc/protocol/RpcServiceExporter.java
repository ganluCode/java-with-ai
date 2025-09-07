package cn.geekslife.rpc.protocol;

import cn.geekslife.rpc.annotation.RpcService;
import cn.geekslife.rpc.common.URL;
import cn.geekslife.rpc.extension.ExtensionLoader;
import cn.geekslife.rpc.registry.Registry;
import cn.geekslife.rpc.registry.RegistryFactory;

import java.util.HashMap;
import java.util.Map;

public class RpcServiceExporter {
    
    public void exportService(Object serviceBean, RpcService rpcService) {
        // 获取服务接口类
        Class<?> interfaceClass = rpcService.interfaceClass();
        if (interfaceClass == void.class) {
            interfaceClass = serviceBean.getClass().getInterfaces()[0];
        }
        
        // 构造服务URL
        Map<String, String> parameters = new HashMap<>();
        parameters.put("interface", interfaceClass.getName());
        parameters.put("version", rpcService.version());
        parameters.put("group", rpcService.group());
        
        URL url = new URL("rpc", "localhost", 20880, interfaceClass.getName(), parameters);
        
        // 导出服务到注册中心
        exportToRegistry(url);
    }
    
    private void exportToRegistry(URL url) {
        // 构造注册中心URL
        URL registryUrl = new URL("nacos", "localhost", 8848, "", null);
        
        // 获取注册中心工厂
        RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension("nacos");
        
        // 获取注册中心
        Registry registry = registryFactory.getRegistry(registryUrl);
        
        // 注册服务
        registry.register(url);
    }
}