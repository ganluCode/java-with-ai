package cn.geekslife.rpc.proxy;

import cn.geekslife.rpc.common.Invocation;
import cn.geekslife.rpc.common.Invoker;
import cn.geekslife.rpc.common.Result;
import cn.geekslife.rpc.common.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvokerInvocationHandler implements InvocationHandler {
    
    private final Invoker<?> invoker;
    
    public InvokerInvocationHandler(Invoker<?> invoker) {
        this.invoker = invoker;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 处理Object类的方法
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(invoker, args);
        }
        
        // 构造调用信息
        RpcInvocation rpcInvocation = new RpcInvocation(
            method.getName(),
            method.getParameterTypes(),
            args
        );
        
        // 执行调用
        Result result = invoker.invoke(rpcInvocation);
        
        // 返回结果
        return result.recreate();
    }
}