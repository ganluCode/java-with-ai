package cn.geekslife.rpc.cluster;

import cn.geekslife.rpc.common.Invoker;

public interface Cluster {
    <T> Invoker<T> join(Directory<T> directory);
}