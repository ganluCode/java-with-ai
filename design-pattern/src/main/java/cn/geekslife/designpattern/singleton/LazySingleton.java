package cn.geekslife.designpattern.singleton;

/**
 * 懒汉式单例模式（线程不安全）
 * 延迟加载，但线程不安全
 */
public class LazySingleton {
    private static LazySingleton instance;
    
    private LazySingleton() {
        // 防止反射攻击
        if (instance != null) {
            throw new RuntimeException("不能通过反射创建实例！");
        }
    }
    
    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
    
    public void doSomething() {
        System.out.println("懒汉式单例执行业务逻辑");
    }
}