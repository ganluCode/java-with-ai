package cn.geekslife.rpc.extension;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ExtensionLoader<T> {
    
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = 
            new ConcurrentHashMap<>();
    
    private final Class<T> type;
    private final ConcurrentMap<String, T> cachedInstances = new ConcurrentHashMap<>();
    
    private ExtensionLoader(Class<T> type) {
        this.type = type;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not an interface!");
        }
        if (!type.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("Extension type (" + type + 
                    ") is not an extension, because it is NOT annotated with @" + SPI.class.getSimpleName() + "!");
        }
        
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }
    
    public T getExtension(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Extension name == null");
        }
        
        T instance = cachedInstances.get(name);
        if (instance == null) {
            instance = createExtension(name);
            cachedInstances.putIfAbsent(name, instance);
            instance = cachedInstances.get(name);
        }
        return instance;
    }
    
    @SuppressWarnings("unchecked")
    private T createExtension(String name) {
        try {
            // 简化实现，实际应该从配置文件中读取具体的实现类
            if ("nacos".equals(name)) {
                if (type == cn.geekslife.rpc.registry.RegistryFactory.class) {
                    return (T) new cn.geekslife.rpc.registry.NacosRegistryFactory();
                }
            } else if ("failover".equals(name)) {
                if (type == cn.geekslife.rpc.cluster.Cluster.class) {
                    return (T) new cn.geekslife.rpc.cluster.FailoverCluster();
                }
            } else if ("random".equals(name)) {
                if (type == cn.geekslife.rpc.cluster.LoadBalance.class) {
                    return (T) new cn.geekslife.rpc.cluster.RandomLoadBalance();
                }
            }
            
            // 默认实现
            if (type == cn.geekslife.rpc.registry.RegistryFactory.class) {
                return (T) new cn.geekslife.rpc.registry.NacosRegistryFactory();
            } else if (type == cn.geekslife.rpc.cluster.Cluster.class) {
                return (T) new cn.geekslife.rpc.cluster.FailoverCluster();
            } else if (type == cn.geekslife.rpc.cluster.LoadBalance.class) {
                return (T) new cn.geekslife.rpc.cluster.RandomLoadBalance();
            }
            
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create extension instance", e);
        }
    }
}