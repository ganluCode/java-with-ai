package cn.geekslife.threadspecific;

/**
 * 计数器管理器类 - Thread-Specific Storage模式在计数器中的应用
 */
public class CounterManager {
    // 线程本地存储的计数器
    private static final ThreadLocal<Integer> counter = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
    
    /**
     * 递增计数器
     * @return 递增后的值
     */
    public static int increment() {
        int currentValue = counter.get();
        int newValue = currentValue + 1;
        counter.set(newValue);
        System.out.println(Thread.currentThread().getName() + "：计数器递增 " + currentValue + " -> " + newValue);
        return newValue;
    }
    
    /**
     * 获取当前计数器值
     * @return 计数器值
     */
    public static int getCounter() {
        int value = counter.get();
        System.out.println(Thread.currentThread().getName() + "：获取计数器值 " + value);
        return value;
    }
    
    /**
     * 重置计数器
     */
    public static void reset() {
        int oldValue = counter.get();
        counter.set(0);
        System.out.println(Thread.currentThread().getName() + "：重置计数器 " + oldValue + " -> 0");
    }
    
    /**
     * 清理计数器资源
     */
    public static void clear() {
        Integer value = counter.get();
        counter.remove();
        System.out.println(Thread.currentThread().getName() + "：清理计数器资源，原值: " + value);
    }
}