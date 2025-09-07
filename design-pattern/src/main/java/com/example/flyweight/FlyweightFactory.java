package com.example.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元工厂类
 * 负责创建和管理享元对象，确保合理地共享Flyweight
 */
public class FlyweightFactory {
    // 享元对象池
    private Map<String, Flyweight> flyweights = new HashMap<>();
    
    /**
     * 获取享元对象
     * @param key 享元对象的键
     * @return 享元对象
     */
    public Flyweight getFlyweight(String key) {
        Flyweight flyweight = flyweights.get(key);
        
        if (flyweight == null) {
            flyweight = new ConcreteFlyweight(key);
            flyweights.put(key, flyweight);
            System.out.println("创建新的享元对象: " + key);
        } else {
            System.out.println("从缓存中获取享元对象: " + key);
        }
        
        return flyweight;
    }
    
    /**
     * 获取享元对象池大小
     * @return 对象池大小
     */
    public int getFlyweightCount() {
        return flyweights.size();
    }
}