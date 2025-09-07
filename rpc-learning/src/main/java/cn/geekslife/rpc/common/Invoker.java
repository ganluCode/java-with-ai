package cn.geekslife.rpc.common;

public interface Invoker<T> {
    Class<T> getInterface();
    Result invoke(Invocation invocation) throws RpcException;
    URL getUrl();
}