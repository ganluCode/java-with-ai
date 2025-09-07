package cn.geekslife.readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

/**
 * 使用公平锁策略的Data类
 * 演示如何使用公平锁避免写饥饿问题
 */
public class FairData {
    // 共享数据
    private String data;
    // 公平读写锁
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    
    /**
     * 构造函数
     * @param initialData 初始数据
     */
    public FairData(String initialData) {
        this.data = initialData;
    }
    
    /**
     * 读取数据 - 使用公平读锁
     * @return 数据内容
     */
    public String read() {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "：公平读取数据 - " + data);
            // 模拟读取耗时
            Thread.sleep((long) (Math.random() * 15));
            return data;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 写入数据 - 使用公平写锁
     * @param newData 新数据
     */
    public void write(String newData) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "：公平写入数据 - " + newData);
            // 模拟写入耗时
            Thread.sleep((long) (Math.random() * 30));
            this.data = newData;
            System.out.println(Thread.currentThread().getName() + "：公平写入完成 - " + data);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * 获取锁的排队长度
     * @return 排队长度
     */
    public int getQueueLength() {
        return lock.getQueueLength();
    }
    
    /**
     * 检查是否有线程正在等待获取写锁
     * @return 是否有等待的写线程
     */
    public boolean hasWaiters() {
        return lock.hasWaiters(lock.writeLock());
    }
}