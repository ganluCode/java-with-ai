package cn.geekslife.designpattern.singleton;

/**
 * 线程安全的懒汉式单例模式
 * 通过同步方法实现线程安全，但性能较差
 */
public class ThreadSafeLazySingleton {
    private static ThreadSafeLazySingleton instance;
    
    private ThreadSafeLazySingleton() {
        // 防止反射攻击
        if (instance != null) {
            throw new RuntimeException("不能通过反射创建实例！");
        }
    }
    
    public static synchronized ThreadSafeLazySingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeLazySingleton();
        }
        return instance;
    }
    
    public void doSomething() {
        System.out.println("线程安全懒汉式单例执行业务逻辑");
    }
}