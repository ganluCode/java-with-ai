package cn.geekslife.threadspecific;

/**
 * 上下文管理器类 - Thread-Specific Storage模式的核心实现
 */
public class ContextManager {
    // 线程本地存储的上下文
    private static final ThreadLocal<Context> contextHolder = new ThreadLocal<>();
    
    /**
     * 设置当前线程的上下文
     * @param context 上下文
     */
    public static void setContext(Context context) {
        contextHolder.set(context);
        System.out.println(Thread.currentThread().getName() + "：设置上下文 " + context);
    }
    
    /**
     * 获取当前线程的上下文
     * @return 上下文
     */
    public static Context getContext() {
        Context context = contextHolder.get();
        System.out.println(Thread.currentThread().getName() + "：获取上下文 " + context);
        return context;
    }
    
    /**
     * 清理当前线程的上下文
     */
    public static void clearContext() {
        Context context = contextHolder.get();
        if (context != null) {
            System.out.println(Thread.currentThread().getName() + "：清理上下文 " + context);
            contextHolder.remove();
        }
    }
    
    /**
     * 检查当前线程是否有上下文
     * @return 是否有上下文
     */
    public static boolean hasContext() {
        return contextHolder.get() != null;
    }
    
    /**
     * 获取当前线程的用户ID
     * @return 用户ID
     */
    public static String getCurrentUserId() {
        Context context = contextHolder.get();
        return context != null ? context.getUserId() : null;
    }
    
    /**
     * 获取当前线程的请求ID
     * @return 请求ID
     */
    public static String getCurrentRequestId() {
        Context context = contextHolder.get();
        return context != null ? context.getRequestId() : null;
    }
}