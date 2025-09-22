package cn.geekslife.rpc.cluster;

import cn.geekslife.rpc.common.Invoker;
import cn.geekslife.rpc.common.RpcException;

public class FailoverCluster implements Cluster {
    
    @Override
    public <T> Invoker<T> join(Directory<T> directory) {
        return new FailoverClusterInvoker<>(directory);
    }
    
    public static class FailoverClusterInvoker<T> extends AbstractClusterInvoker<T> {
        
        public FailoverClusterInvoker(Directory<T> directory) {
            super(directory);
        }
        
        @Override
        protected Object doInvoke(cn.geekslife.rpc.common.Invocation invocation, java.util.List<Invoker<T>> invokers) throws Throwable {
            java.util.List<Invoker<T>> copyInvokers = invokers;
            if (copyInvokers == null || copyInvokers.isEmpty()) {
                throw new RpcException("No provider available");
            }
            
            // 获取重试次数
            int retries = getUrl().getParameter("retries", 2);
            if (retries <= 0) {
                retries = 0;
            }
            
            RpcException lastException = null;
            for (int i = 0; i <= retries; i++) {
                try {
                    // 重新获取Invoker列表
                    copyInvokers = list(invocation);
                    if (copyInvokers == null || copyInvokers.isEmpty()) {
                        throw new RpcException("No provider available");
                    }
                    
                    // 负载均衡选择Invoker
                    cn.geekslife.rpc.cluster.LoadBalance loadbalance = cn.geekslife.rpc.extension.ExtensionLoader
                            .getExtensionLoader(cn.geekslife.rpc.cluster.LoadBalance.class)
                            .getExtension(getUrl().getParameter("loadbalance", "random"));
                    Invoker<T> invoker = select(loadbalance, invocation, copyInvokers, null);
                    
                    // 调用
                    return invoker.invoke(invocation);
                } catch (RpcException e) {
                    if (i == retries) {
                        throw e;
                    }
                    lastException = e;
                } catch (Throwable e) {
                    if (i == retries) {
                        throw new RpcException("Failed to invoke", e);
                    }
                    lastException = new RpcException("Failed to invoke", e);
                }
            }
            
            throw lastException;
        }
    }
}