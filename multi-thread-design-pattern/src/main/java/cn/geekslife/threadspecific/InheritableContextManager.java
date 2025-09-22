package cn.geekslife.threadspecific;

/**
 * 可继承的上下文管理器类 - Thread-Specific Storage模式的高级特性
 */
public class InheritableContextManager {
    // 可继承的线程本地存储
    private static final InheritableThreadLocal<Context> inheritableContextHolder = new InheritableThreadLocal<Context>() {
        @Override
        protected Context initialValue() {
            return new Context("default", "default-request");
        }
        
        @Override
        protected Context childValue(Context parentValue) {
            // 子线程继承父线程的上下文，但创建新的请求ID
            if (parentValue != null) {
                return new Context(parentValue.getUserId(), 
                    parentValue.getRequestId() + "-child-" + System.currentTimeMillis());
            }
            return new Context("child", "child-request");
        }
    };
    
    /**
     * 设置上下文
     * @param context 上下文
     */
    public static void setContext(Context context) {
        inheritableContextHolder.set(context);
        System.out.println(Thread.currentThread().getName() + "：设置可继承上下文 " + context);
    }
    
    /**
     * 获取上下文
     * @return 上下文
     */
    public static Context getContext() {
        Context context = inheritableContextHolder.get();
        System.out.println(Thread.currentThread().getName() + "：获取可继承上下文 " + context);
        return context;
    }
    
    /**
     * 清理上下文
     */
    public static void clearContext() {
        Context context = inheritableContextHolder.get();
        if (context != null) {
            System.out.println(Thread.currentThread().getName() + "：清理可继承上下文 " + context);
            inheritableContextHolder.remove();
        }
    }
    
    /**
     * 演示父子线程上下文继承
     */
    public static void demonstrateInheritance() {
        // 设置父线程上下文
        Context parentContext = new Context("parent-user", "parent-request-123");
        setContext(parentContext);
        
        System.out.println("父线程 " + Thread.currentThread().getName() + " 上下文: " + getContext());
        
        // 创建子线程
        Thread childThread = new Thread(() -> {
            // 子线程自动继承父线程的上下文
            Context childContext = getContext();
            System.out.println("子线程 " + Thread.currentThread().getName() + " 继承的上下文: " + childContext);
            
            // 修改子线程的上下文
            Context modifiedContext = new Context("child-user", "child-request-456");
            setContext(modifiedContext);
            System.out.println("子线程 " + Thread.currentThread().getName() + " 修改后的上下文: " + getContext());
        }, "ChildThread");
        
        childThread.start();
        
        try {
            childThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 父线程上下文不受影响
        System.out.println("父线程 " + Thread.currentThread().getName() + " 最终上下文: " + getContext());
    }
}