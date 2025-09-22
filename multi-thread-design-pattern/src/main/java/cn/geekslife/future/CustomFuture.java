package cn.geekslife.future;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;

/**
 * 自定义Future实现 - 演示Future模式的自定义实现
 */
public class CustomFuture<V> implements Future<V> {
    private V result;
    private boolean done = false;
    private boolean cancelled = false;
    private final Object lock = new Object();
    private Thread executingThread;
    
    /**
     * 获取结果 - 阻塞等待直到完成
     * @return 结果
     * @throws InterruptedException 中断异常
     * @throws ExecutionException 执行异常
     */
    @Override
    public V get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            while (!done && !cancelled) {
                lock.wait();
            }
            
            if (cancelled) {
                throw new RuntimeException("任务已被取消");
            }
            
            return result;
        }
    }
    
    /**
     * 获取结果 - 带超时时间
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 结果
     * @throws InterruptedException 中断异常
     * @throws ExecutionException 执行异常
     * @throws TimeoutException 超时异常
     */
    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (lock) {
            if (!done && !cancelled) {
                lock.wait(unit.toMillis(timeout));
                
                if (!done && !cancelled) {
                    throw new TimeoutException("获取结果超时");
                }
            }
            
            if (cancelled) {
                throw new RuntimeException("任务已被取消");
            }
            
            return result;
        }
    }
    
    /**
     * 检查是否完成
     * @return 是否完成
     */
    @Override
    public boolean isDone() {
        synchronized (lock) {
            return done;
        }
    }
    
    /**
     * 检查是否已取消
     * @return 是否已取消
     */
    @Override
    public boolean isCancelled() {
        synchronized (lock) {
            return cancelled;
        }
    }
    
    /**
     * 取消任务
     * @param mayInterruptIfRunning 是否中断正在执行的线程
     * @return 是否取消成功
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (lock) {
            if (done || cancelled) {
                return false;
            }
            
            cancelled = true;
            
            if (mayInterruptIfRunning && executingThread != null) {
                executingThread.interrupt();
            }
            
            lock.notifyAll();
            return true;
        }
    }
    
    /**
     * 设置执行线程
     * @param thread 执行线程
     */
    public void setExecutingThread(Thread thread) {
        synchronized (lock) {
            this.executingThread = thread;
        }
    }
    
    /**
     * 设置结果
     * @param result 结果
     */
    public void setResult(V result) {
        synchronized (lock) {
            if (done || cancelled) {
                return;
            }
            
            this.result = result;
            this.done = true;
            lock.notifyAll();
        }
    }
    
    /**
     * 设置异常结果
     * @param exception 异常
     */
    public void setException(Exception exception) {
        synchronized (lock) {
            if (done || cancelled) {
                return;
            }
            
            this.result = null;
            this.done = true;
            lock.notifyAll();
        }
    }
}