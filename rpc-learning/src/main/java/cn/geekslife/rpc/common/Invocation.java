package cn.geekslife.rpc.common;

public interface Invocation {
    String getMethodName();
    Class<?>[] getParameterTypes();
    Object[] getArguments();
}