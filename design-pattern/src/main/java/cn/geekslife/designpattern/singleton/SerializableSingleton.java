package cn.geekslife.designpattern.singleton;

import java.io.Serializable;

/**
 * 可序列化的单例模式
 * 演示如何防止序列化破坏单例
 */
public class SerializableSingleton implements Serializable {
    private static final SerializableSingleton INSTANCE = new SerializableSingleton();
    
    private SerializableSingleton() {
        // 防止反射攻击
        if (INSTANCE != null) {
            throw new RuntimeException("不能通过反射创建实例！");
        }
    }
    
    public static SerializableSingleton getInstance() {
        return INSTANCE;
    }
    
    public void doSomething() {
        System.out.println("可序列化单例执行业务逻辑");
    }
    
    // 防止序列化破坏单例
    private Object readResolve() {
        return INSTANCE;
    }
}