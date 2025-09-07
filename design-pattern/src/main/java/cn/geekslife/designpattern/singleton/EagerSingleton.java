package cn.geekslife.designpattern.singleton;

/**
 * 饿汉式单例模式
 * 线程安全，但在类加载时就创建实例，可能造成资源浪费
 */
public class EagerSingleton {
    // 在类加载时就创建实例
    private static final EagerSingleton INSTANCE = new EagerSingleton();
    
    // 私有构造函数，防止外部实例化
    private EagerSingleton() {
        // 防止反射攻击
        if (INSTANCE != null) {
            throw new RuntimeException("不能通过反射创建实例！");
        }
    }
    
    // 提供全局访问点
    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
    
    public void doSomething() {
        System.out.println("饿汉式单例执行业务逻辑");
    }
}