package cn.geekslife.rpc.registry;

import cn.geekslife.rpc.common.URL;

import java.util.List;

public interface Registry {
    void register(URL url);
    void unregister(URL url);
    List<URL> discover(URL url);
}