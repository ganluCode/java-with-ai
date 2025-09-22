package cn.geekslife.rpc.cluster;

import cn.geekslife.rpc.common.Invoker;
import cn.geekslife.rpc.common.Invocation;
import cn.geekslife.rpc.common.Result;
import cn.geekslife.rpc.common.URL;
import cn.geekslife.rpc.common.RpcException;

import java.util.List;

public abstract class AbstractClusterInvoker<T> implements Invoker<T> {
    
    protected final Directory<T> directory;
    
    public AbstractClusterInvoker(Directory<T> directory) {
        this.directory = directory;
    }
    
    @Override
    public Class<T> getInterface() {
        return directory.getInterface();
    }
    
    @Override
    public URL getUrl() {
        return directory.getUrl();
    }
    
    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        try {
            List<Invoker<T>> invokers = list(invocation);
            return (Result) doInvoke(invocation, invokers);
        } catch (Throwable e) {
            if (e instanceof RpcException) {
                throw (RpcException) e;
            } else {
                throw new RpcException(e);
            }
        }
    }
    
    protected abstract Object doInvoke(Invocation invocation, List<Invoker<T>> invokers) throws Throwable;
    
    protected List<Invoker<T>> list(Invocation invocation) throws RpcException {
        return directory.list(invocation);
    }
    
    protected <T> Invoker<T> select(LoadBalance loadbalance, Invocation invocation,
                                  List<Invoker<T>> invokers, List<Invoker<T>> selected) throws RpcException {
        if (invokers == null || invokers.isEmpty()) {
            return null;
        }
        
        if (invokers.size() == 1) {
            return invokers.get(0);
        }
        
        // 负载均衡选择
        return loadbalance.select(invokers, invocation);
    }
}