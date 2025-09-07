package cn.geekslife.designpattern.creational;

/**
 * SingletonPattern - 单例模式示例
 * 这是一个简单的双重检查锁定实现
 */
public class SingletonPattern {
    
    private static volatile SingletonPattern instance;
    
    private SingletonPattern() {
        // 私有构造函数防止外部实例化
        // 防止反射攻击
        if (instance != null) {
            throw new RuntimeException("不能通过反射创建实例！");
        }
    }
    
    public static SingletonPattern getInstance() {
        // 双重检查锁定
        if (instance == null) {
            synchronized (SingletonPattern.class) {
                if (instance == null) {
                    instance = new SingletonPattern();
                }
            }
        }
        return instance;
    }
    
    public void doSomething() {
        System.out.println("Singleton pattern is working!");
    }
    
    // 防止序列化破坏单例
    private Object readResolve() {
        return instance;
    }
}