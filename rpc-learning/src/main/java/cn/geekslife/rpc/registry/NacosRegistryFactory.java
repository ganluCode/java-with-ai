package cn.geekslife.rpc.registry;

import cn.geekslife.rpc.common.URL;

public class NacosRegistryFactory implements RegistryFactory {
    
    @Override
    public Registry getRegistry(URL url) {
        return new NacosRegistry(url);
    }
}