package cn.geekslife.rpc.registry;

import cn.geekslife.rpc.common.URL;

public interface RegistryFactory {
    Registry getRegistry(URL url);
}