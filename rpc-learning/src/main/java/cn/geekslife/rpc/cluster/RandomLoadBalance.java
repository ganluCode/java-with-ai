package cn.geekslife.rpc.cluster;

import cn.geekslife.rpc.common.Invoker;
import cn.geekslife.rpc.common.Invocation;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalance implements LoadBalance {
    
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, Invocation invocation) {
        if (invokers == null || invokers.isEmpty()) {
            return null;
        }
        
        if (invokers.size() == 1) {
            return invokers.get(0);
        }
        
        // 平均随机
        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
    }
}