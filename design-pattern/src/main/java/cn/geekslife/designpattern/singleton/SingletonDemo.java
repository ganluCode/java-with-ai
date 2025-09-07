package cn.geekslife.designpattern.singleton;

/**
 * 单例模式演示类
 * 展示各种单例模式的使用方法
 */
public class SingletonDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 单例模式演示 ===");
        
        // 1. 饿汉式单例
        System.out.println("\n1. 饿汉式单例:");
        EagerSingleton eager1 = EagerSingleton.getInstance();
        EagerSingleton eager2 = EagerSingleton.getInstance();
        System.out.println("eager1 == eager2: " + (eager1 == eager2));
        eager1.doSomething();
        
        // 2. 懒汉式单例
        System.out.println("\n2. 懒汉式单例:");
        LazySingleton lazy1 = LazySingleton.getInstance();
        LazySingleton lazy2 = LazySingleton.getInstance();
        System.out.println("lazy1 == lazy2: " + (lazy1 == lazy2));
        lazy1.doSomething();
        
        // 3. 线程安全懒汉式单例
        System.out.println("\n3. 线程安全懒汉式单例:");
        ThreadSafeLazySingleton threadSafe1 = ThreadSafeLazySingleton.getInstance();
        ThreadSafeLazySingleton threadSafe2 = ThreadSafeLazySingleton.getInstance();
        System.out.println("threadSafe1 == threadSafe2: " + (threadSafe1 == threadSafe2));
        threadSafe1.doSomething();
        
        // 4. 双重检查锁定单例
        System.out.println("\n4. 双重检查锁定单例:");
        DoubleCheckedLockingSingleton doubleChecked1 = DoubleCheckedLockingSingleton.getInstance();
        DoubleCheckedLockingSingleton doubleChecked2 = DoubleCheckedLockingSingleton.getInstance();
        System.out.println("doubleChecked1 == doubleChecked2: " + (doubleChecked1 == doubleChecked2));
        doubleChecked1.doSomething();
        
        // 5. 静态内部类单例
        System.out.println("\n5. 静态内部类单例:");
        StaticInnerClassSingleton staticInner1 = StaticInnerClassSingleton.getInstance();
        StaticInnerClassSingleton staticInner2 = StaticInnerClassSingleton.getInstance();
        System.out.println("staticInner1 == staticInner2: " + (staticInner1 == staticInner2));
        staticInner1.doSomething();
        
        // 6. 枚举单例
        System.out.println("\n6. 枚举单例:");
        EnumSingleton enum1 = EnumSingleton.INSTANCE;
        EnumSingleton enum2 = EnumSingleton.INSTANCE;
        System.out.println("enum1 == enum2: " + (enum1 == enum2));
        enum1.doSomething();
        enum1.doAnotherThing();
        
        // 7. 可序列化单例
        System.out.println("\n7. 可序列化单例:");
        SerializableSingleton serializable1 = SerializableSingleton.getInstance();
        SerializableSingleton serializable2 = SerializableSingleton.getInstance();
        System.out.println("serializable1 == serializable2: " + (serializable1 == serializable2));
        serializable1.doSomething();
    }
}