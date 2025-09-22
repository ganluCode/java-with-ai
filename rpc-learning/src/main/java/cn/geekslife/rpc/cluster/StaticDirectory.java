package cn.geekslife.rpc.cluster;

import cn.geekslife.rpc.common.Invoker;
import cn.geekslife.rpc.common.Invocation;
import cn.geekslife.rpc.common.URL;

import java.util.List;

public class StaticDirectory<T> implements Directory<T> {
    
    private final List<URL> urls;
    private final Class<T> interfaceClass;
    
    public StaticDirectory(List<URL> urls, Class<T> interfaceClass) {
        this.urls = urls;
        this.interfaceClass = interfaceClass;
    }
    
    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }
    
    @Override
    public URL getUrl() {
        return null;
    }
    
    @Override
    public List<Invoker<T>> list(Invocation invocation) {
        // 根据URL列表创建Invoker列表
        // 这里简化处理，实际应该根据URL创建对应的Invoker
        return null;
    }
}