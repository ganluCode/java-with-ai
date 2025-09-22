package cn.geekslife.rpc.cluster;

import cn.geekslife.rpc.common.Invoker;
import cn.geekslife.rpc.common.Invocation;
import cn.geekslife.rpc.common.URL;

import java.util.List;

public interface Directory<T> {
    Class<T> getInterface();
    URL getUrl();
    List<Invoker<T>> list(Invocation invocation);
}