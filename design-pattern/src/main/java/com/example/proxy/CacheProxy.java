package com.example.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存代理类
 * 缓存 expensive operations 的结果
 */
public class CacheProxy implements Subject {
    private RealSubject realSubject;
    private Map<String, String> cache = new HashMap<>();
    
    @Override
    public void request() {
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        realSubject.request();
    }
    
    @Override
    public String getData() {
        String key = "data";
        if (cache.containsKey(key)) {
            System.out.println("从缓存中获取数据");
            return cache.get(key);
        }
        
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        
        String data = realSubject.getData();
        cache.put(key, data);
        System.out.println("将数据存入缓存");
        return data;
    }
    
    public void clearCache() {
        cache.clear();
        System.out.println("缓存已清空");
    }
    
    public int getCacheSize() {
        return cache.size();
    }
}