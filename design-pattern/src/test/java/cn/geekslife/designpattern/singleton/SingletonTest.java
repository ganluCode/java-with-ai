package cn.geekslife.designpattern.singleton;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 单例模式测试类
 */
public class SingletonTest {
    
    /**
     * 测试饿汉式单例
     */
    @Test
    public void testEagerSingleton() {
        EagerSingleton instance1 = EagerSingleton.getInstance();
        EagerSingleton instance2 = EagerSingleton.getInstance();
        
        // 验证是否为同一个实例
        assertSame(instance1, instance2, "饿汉式单例应该返回同一个实例");
        
        // 验证功能是否正常
        instance1.doSomething();
    }
    
    /**
     * 测试懒汉式单例
     */
    @Test
    public void testLazySingleton() {
        LazySingleton instance1 = LazySingleton.getInstance();
        LazySingleton instance2 = LazySingleton.getInstance();
        
        // 验证是否为同一个实例
        assertSame(instance1, instance2, "懒汉式单例应该返回同一个实例");
        
        // 验证功能是否正常
        instance1.doSomething();
    }
    
    /**
     * 测试线程安全的懒汉式单例
     */
    @Test
    public void testThreadSafeLazySingleton() {
        ThreadSafeLazySingleton instance1 = ThreadSafeLazySingleton.getInstance();
        ThreadSafeLazySingleton instance2 = ThreadSafeLazySingleton.getInstance();
        
        // 验证是否为同一个实例
        assertSame(instance1, instance2, "线程安全懒汉式单例应该返回同一个实例");
        
        // 验证功能是否正常
        instance1.doSomething();
    }
    
    /**
     * 测试双重检查锁定单例
     */
    @Test
    public void testDoubleCheckedLockingSingleton() {
        DoubleCheckedLockingSingleton instance1 = DoubleCheckedLockingSingleton.getInstance();
        DoubleCheckedLockingSingleton instance2 = DoubleCheckedLockingSingleton.getInstance();
        
        // 验证是否为同一个实例
        assertSame(instance1, instance2, "双重检查锁定单例应该返回同一个实例");
        
        // 验证功能是否正常
        instance1.doSomething();
    }
    
    /**
     * 测试静态内部类单例
     */
    @Test
    public void testStaticInnerClassSingleton() {
        StaticInnerClassSingleton instance1 = StaticInnerClassSingleton.getInstance();
        StaticInnerClassSingleton instance2 = StaticInnerClassSingleton.getInstance();
        
        // 验证是否为同一个实例
        assertSame(instance1, instance2, "静态内部类单例应该返回同一个实例");
        
        // 验证功能是否正常
        instance1.doSomething();
    }
    
    /**
     * 测试枚举单例
     */
    @Test
    public void testEnumSingleton() {
        EnumSingleton instance1 = EnumSingleton.INSTANCE;
        EnumSingleton instance2 = EnumSingleton.INSTANCE;
        
        // 验证是否为同一个实例
        assertSame(instance1, instance2, "枚举单例应该返回同一个实例");
        
        // 验证功能是否正常
        instance1.doSomething();
        instance1.doAnotherThing();
    }
    
    /**
     * 测试多线程环境下的双重检查锁定单例
     * @throws InterruptedException
     */
    @Test
    public void testDoubleCheckedLockingSingletonInMultiThread() throws InterruptedException {
        int threadCount = 100;
        DoubleCheckedLockingSingleton[] instances = new DoubleCheckedLockingSingleton[threadCount];
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 创建多个线程同时获取单例实例
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                instances[index] = DoubleCheckedLockingSingleton.getInstance();
            });
        }
        
        // 关闭线程池并等待所有任务完成
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "线程池应该在5秒内完成所有任务");
        
        // 验证所有线程获取的都是同一个实例
        DoubleCheckedLockingSingleton firstInstance = instances[0];
        for (int i = 1; i < threadCount; i++) {
            assertSame(firstInstance, instances[i], "所有线程应该获取到同一个实例");
        }
    }
    
    /**
     * 测试多线程环境下的静态内部类单例
     * @throws InterruptedException
     */
    @Test
    public void testStaticInnerClassSingletonInMultiThread() throws InterruptedException {
        int threadCount = 100;
        StaticInnerClassSingleton[] instances = new StaticInnerClassSingleton[threadCount];
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 创建多个线程同时获取单例实例
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                instances[index] = StaticInnerClassSingleton.getInstance();
            });
        }
        
        // 关闭线程池并等待所有任务完成
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "线程池应该在5秒内完成所有任务");
        
        // 验证所有线程获取的都是同一个实例
        StaticInnerClassSingleton firstInstance = instances[0];
        for (int i = 1; i < threadCount; i++) {
            assertSame(firstInstance, instances[i], "所有线程应该获取到同一个实例");
        }
    }
    
    /**
     * 测试反射攻击防护
     */
    @Test
    public void testReflectionAttackProtection() {
        // 获取已存在的实例
        EagerSingleton instance1 = EagerSingleton.getInstance();
        
        // 尝试通过反射创建新实例
        assertThrows(Exception.class, () -> {
            Constructor<EagerSingleton> constructor = EagerSingleton.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            EagerSingleton instance2 = constructor.newInstance();
        }, "应该抛出异常防止反射攻击");
    }
    
    /**
     * 测试序列化破坏防护
     */
    @Test
    public void testSerializationProtection() throws Exception {
        // 获取单例实例
        SerializableSingleton instance1 = SerializableSingleton.getInstance();
        
        // 序列化
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(instance1);
        oos.close();
        
        // 反序列化
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        SerializableSingleton instance2 = (SerializableSingleton) ois.readObject();
        ois.close();
        
        // 验证序列化前后是同一个实例
        assertSame(instance1, instance2, "序列化不应该破坏单例");
    }
    
    /**
     * 性能测试：比较不同单例实现的性能
     */
    @Test
    public void testPerformance() {
        int iterations = 1000000;
        
        // 测试饿汉式单例性能
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            EagerSingleton.getInstance();
        }
        long eagerTime = System.nanoTime() - startTime;
        
        // 测试双重检查锁定单例性能
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            DoubleCheckedLockingSingleton.getInstance();
        }
        long doubleCheckedTime = System.nanoTime() - startTime;
        
        // 测试静态内部类单例性能
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            StaticInnerClassSingleton.getInstance();
        }
        long staticInnerTime = System.nanoTime() - startTime;
        
        System.out.println("饿汉式单例耗时: " + eagerTime + " ns");
        System.out.println("双重检查锁定单例耗时: " + doubleCheckedTime + " ns");
        System.out.println("静态内部类单例耗时: " + staticInnerTime + " ns");
        
        // 静态内部类和饿汉式性能应该相近，双重检查锁定稍慢
        assertTrue(staticInnerTime < doubleCheckedTime * 2, "静态内部类性能应该优于双重检查锁定");
    }
}