package com.example.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符享元工厂类
 * 管理字符格式的享元对象
 */
public class CharacterFlyweightFactory {
    // 享元对象池
    private static final Map<String, CharacterFlyweight> flyweights = new HashMap<>();
    
    /**
     * 获取字符享元对象
     * @param font 字体
     * @param size 大小
     * @param color 颜色
     * @return 字符享元对象
     */
    public static CharacterFlyweight getCharacterFlyweight(String font, int size, String color) {
        // 创建唯一键值
        String key = font + ":" + size + ":" + color;
        
        CharacterFlyweight flyweight = flyweights.get(key);
        
        if (flyweight == null) {
            flyweight = new CharacterFlyweight(font, size, color);
            flyweights.put(key, flyweight);
            System.out.println("创建新的字符格式: " + key);
        } else {
            System.out.println("复用字符格式: " + key);
        }
        
        return flyweight;
    }
    
    /**
     * 获取享元对象池大小
     * @return 对象池大小
     */
    public static int getFlyweightCount() {
        return flyweights.size();
    }
    
    /**
     * 清空享元对象池
     */
    public static void clear() {
        flyweights.clear();
    }
}