package cn.geekslife.designpattern.singleton;

/**
 * 静态内部类单例模式
 * 线程安全、延迟加载、性能好，推荐使用
 */
public class StaticInnerClassSingleton {
    
    private StaticInnerClassSingleton() {
        // 防止反射攻击
        if (StaticInnerClassSingletonHolder.INSTANCE != null) {
            throw new RuntimeException("不能通过反射创建实例！");
        }
    }
    
    // 静态内部类
    private static class StaticInnerClassSingletonHolder {
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }
    
    public static StaticInnerClassSingleton getInstance() {
        return StaticInnerClassSingletonHolder.INSTANCE;
    }
    
    public void doSomething() {
        System.out.println("静态内部类单例执行业务逻辑");
    }
}