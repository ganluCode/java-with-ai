package cn.geekslife.rpc.proxy;

import cn.geekslife.rpc.common.Invoker;

import java.lang.reflect.Proxy;

public class ProxyFactory {
    
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> interfaceClass, Invoker<T> invoker) {
        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class[]{interfaceClass},
            new InvokerInvocationHandler(invoker)
        );
    }
}