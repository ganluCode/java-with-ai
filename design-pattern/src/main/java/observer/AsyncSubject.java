package observer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步通知的主题类
 * 提供异步通知机制，避免阻塞主线程
 */
public abstract class AsyncSubject extends EnhancedSubject {
    // 线程池用于异步通知
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    
    // 异步通知所有观察者
    public void notifyObserversAsync() {
        cleanUpObservers(); // 清理已被垃圾回收的观察者
        
        // 为每个观察者创建异步任务
        for (WeakReference<Observer> weakRef : observers) {
            Observer observer = weakRef.get();
            if (observer != null) {
                CompletableFuture.runAsync(() -> {
                    try {
                        observer.update(this);
                    } catch (Exception e) {
                        // 记录异常
                        System.err.println("Async observer notification failed: " + e.getMessage());
                        e.printStackTrace();
                    }
                }, executor);
            }
        }
    }
    
    // 关闭线程池
    public static void shutdown() {
        executor.shutdown();
    }
}