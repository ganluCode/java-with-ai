package cn.geekslife.designpattern.singleton;

/**
 * 双重检查锁定单例模式
 * 线程安全、延迟加载、性能较好，推荐使用
 */
public class DoubleCheckedLockingSingleton {
    // volatile关键字确保多线程环境下的可见性
    private static volatile DoubleCheckedLockingSingleton instance;
    
    private DoubleCheckedLockingSingleton() {
        // 防止反射攻击
        if (instance != null) {
            throw new RuntimeException("不能通过反射创建实例！");
        }
    }
    
    public static DoubleCheckedLockingSingleton getInstance() {
        // 第一次检查，避免不必要的同步
        if (instance == null) {
            synchronized (DoubleCheckedLockingSingleton.class) {
                // 第二次检查，确保只创建一个实例
                if (instance == null) {
                    instance = new DoubleCheckedLockingSingleton();
                }
            }
        }
        return instance;
    }
    
    public void doSomething() {
        System.out.println("双重检查锁定单例执行业务逻辑");
    }
}