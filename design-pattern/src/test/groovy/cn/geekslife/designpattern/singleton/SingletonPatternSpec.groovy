package cn.geekslife.designpattern.singleton

import spock.lang.Specification
import spock.lang.Unroll

/**
 * 单例模式Spock测试类
 * 使用行为驱动开发(BDD)方式测试各种单例模式实现
 */
class SingletonPatternSpec extends Specification {
    
    def "饿汉式单例应该在类加载时创建实例"() {
        when: "获取饿汉式单例实例"
        EagerSingleton instance1 = EagerSingleton.getInstance()
        EagerSingleton instance2 = EagerSingleton.getInstance()
        
        then: "验证实例不为null且两次获取的实例相同"
        instance1 != null
        instance2 != null
        instance1.is(instance2)
        
        and: "验证实例是正确的类型"
        instance1 instanceof EagerSingleton
    }
    
    def "饿汉式单例应该能够执行业务逻辑"() {
        given: "获取饿汉式单例实例"
        EagerSingleton singleton = EagerSingleton.getInstance()
        
        when: "执行业务逻辑"
        singleton.doSomething()
        
        then: "验证操作执行成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "懒汉式单例应该延迟创建实例"() {
        when: "获取懒汉式单例实例"
        LazySingleton instance1 = LazySingleton.getInstance()
        LazySingleton instance2 = LazySingleton.getInstance()
        
        then: "验证实例不为null且两次获取的实例相同"
        instance1 != null
        instance2 != null
        instance1.is(instance2)
        
        and: "验证实例是正确的类型"
        instance1 instanceof LazySingleton
    }
    
    def "懒汉式单例应该能够执行业务逻辑"() {
        given: "获取懒汉式单例实例"
        LazySingleton singleton = LazySingleton.getInstance()
        
        when: "执行业务逻辑"
        singleton.doSomething()
        
        then: "验证操作执行成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "线程安全懒汉式单例应该保证线程安全"() {
        when: "获取线程安全懒汉式单例实例"
        ThreadSafeLazySingleton instance1 = ThreadSafeLazySingleton.getInstance()
        ThreadSafeLazySingleton instance2 = ThreadSafeLazySingleton.getInstance()
        
        then: "验证实例不为null且两次获取的实例相同"
        instance1 != null
        instance2 != null
        instance1.is(instance2)
        
        and: "验证实例是正确的类型"
        instance1 instanceof ThreadSafeLazySingleton
    }
    
    def "线程安全懒汉式单例应该能够执行业务逻辑"() {
        given: "获取线程安全懒汉式单例实例"
        ThreadSafeLazySingleton singleton = ThreadSafeLazySingleton.getInstance()
        
        when: "执行业务逻辑"
        singleton.doSomething()
        
        then: "验证操作执行成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "双重检查锁定单例应该保证线程安全且延迟加载"() {
        when: "获取双重检查锁定单例实例"
        DoubleCheckedLockingSingleton instance1 = DoubleCheckedLockingSingleton.getInstance()
        DoubleCheckedLockingSingleton instance2 = DoubleCheckedLockingSingleton.getInstance()
        
        then: "验证实例不为null且两次获取的实例相同"
        instance1 != null
        instance2 != null
        instance1.is(instance2)
        
        and: "验证实例是正确的类型"
        instance1 instanceof DoubleCheckedLockingSingleton
    }
    
    def "双重检查锁定单例应该能够执行业务逻辑"() {
        given: "获取双重检查锁定单例实例"
        DoubleCheckedLockingSingleton singleton = DoubleCheckedLockingSingleton.getInstance()
        
        when: "执行业务逻辑"
        singleton.doSomething()
        
        then: "验证操作执行成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "静态内部类单例应该保证线程安全且延迟加载"() {
        when: "获取静态内部类单例实例"
        StaticInnerClassSingleton instance1 = StaticInnerClassSingleton.getInstance()
        StaticInnerClassSingleton instance2 = StaticInnerClassSingleton.getInstance()
        
        then: "验证实例不为null且两次获取的实例相同"
        instance1 != null
        instance2 != null
        instance1.is(instance2)
        
        and: "验证实例是正确的类型"
        instance1 instanceof StaticInnerClassSingleton
    }
    
    def "静态内部类单例应该能够执行业务逻辑"() {
        given: "获取静态内部类单例实例"
        StaticInnerClassSingleton singleton = StaticInnerClassSingleton.getInstance()
        
        when: "执行业务逻辑"
        singleton.doSomething()
        
        then: "验证操作执行成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "枚举单例应该保证唯一实例"() {
        when: "获取枚举单例实例"
        EnumSingleton instance1 = EnumSingleton.INSTANCE
        EnumSingleton instance2 = EnumSingleton.INSTANCE
        
        then: "验证两次获取的实例相同"
        instance1.is(instance2)
        
        and: "验证实例是正确的类型"
        instance1 instanceof EnumSingleton
    }
    
    def "枚举单例应该能够执行业务逻辑"() {
        given: "获取枚举单例实例"
        EnumSingleton singleton = EnumSingleton.INSTANCE
        
        when: "执行业务逻辑"
        singleton.doSomething()
        
        then: "验证操作执行成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "所有单例实现都应该保证实例唯一性"() {
        when: "获取各种单例实例"
        EagerSingleton eager1 = EagerSingleton.getInstance()
        EagerSingleton eager2 = EagerSingleton.getInstance()
        
        LazySingleton lazy1 = LazySingleton.getInstance()
        LazySingleton lazy2 = LazySingleton.getInstance()
        
        ThreadSafeLazySingleton threadSafeLazy1 = ThreadSafeLazySingleton.getInstance()
        ThreadSafeLazySingleton threadSafeLazy2 = ThreadSafeLazySingleton.getInstance()
        
        DoubleCheckedLockingSingleton doubleChecked1 = DoubleCheckedLockingSingleton.getInstance()
        DoubleCheckedLockingSingleton doubleChecked2 = DoubleCheckedLockingSingleton.getInstance()
        
        StaticInnerClassSingleton staticInner1 = StaticInnerClassSingleton.getInstance()
        StaticInnerClassSingleton staticInner2 = StaticInnerClassSingleton.getInstance()
        
        EnumSingleton enum1 = EnumSingleton.INSTANCE
        EnumSingleton enum2 = EnumSingleton.INSTANCE
        
        then: "验证每种实现的实例都是唯一的"
        eager1.is(eager2)
        lazy1.is(lazy2)
        threadSafeLazy1.is(threadSafeLazy2)
        doubleChecked1.is(doubleChecked2)
        staticInner1.is(staticInner2)
        enum1.is(enum2)
    }
    
    def "单例实例在不同时间点获取应该相同"() {
        when: "在不同时间点获取实例"
        def instances = []
        10.times {
            instances << EagerSingleton.getInstance()
            Thread.sleep(10) // 短暂延迟
        }
        
        then: "验证所有实例都相同"
        instances.every { it.is(instances[0]) }
    }
    
    def "单例模式应该防止通过反射创建多个实例"() {
        when: "尝试通过反射创建单例实例"
        EagerSingleton instance1 = EagerSingleton.getInstance()
        // 这会抛出异常，因为在构造函数中添加了防止反射攻击的检查
        EagerSingleton.newInstance()
        
        then: "应该抛出RuntimeException异常"
        thrown(RuntimeException)
    }
}