package cn.geekslife.rpc.cluster;

import cn.geekslife.rpc.common.Invoker;
import cn.geekslife.rpc.common.Invocation;

import java.util.List;

public interface LoadBalance {
    <T> Invoker<T> select(List<Invoker<T>> invokers, Invocation invocation);
}