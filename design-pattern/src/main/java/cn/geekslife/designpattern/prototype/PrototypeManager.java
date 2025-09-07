package cn.geekslife.designpattern.prototype;

import java.util.HashMap;
import java.util.Map;

/**
 * 原型管理器
 * 用于管理原型对象的注册和获取
 */
public class PrototypeManager {
    private static Map<String, Prototype> prototypes = new HashMap<>();
    
    // 注册原型
    public static void registerPrototype(String key, Prototype prototype) {
        prototypes.put(key, prototype);
    }
    
    // 获取原型
    public static Prototype getPrototype(String key) {
        Prototype prototype = prototypes.get(key);
        return prototype != null ? prototype.clone() : null;
    }
    
    // 移除原型
    public static void removePrototype(String key) {
        prototypes.remove(key);
    }
    
    // 获取所有原型键
    public static String[] getKeys() {
        return prototypes.keySet().toArray(new String[0]);
    }
    
    // 清空所有原型
    public static void clear() {
        prototypes.clear();
    }
    
    // 获取原型数量
    public static int size() {
        return prototypes.size();
    }
}