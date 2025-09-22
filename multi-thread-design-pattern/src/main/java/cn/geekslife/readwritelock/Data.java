package cn.geekslife.readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 数据类 - 演示Read-Write Lock模式
 * 该类使用读写锁来管理对共享数据的访问
 */
public class Data {
    // 共享数据
    private String data;
    // 读写锁
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    /**
     * 构造函数
     * @param initialData 初始数据
     */
    public Data(String initialData) {
        this.data = initialData;
    }
    
    /**
     * 读取数据 - 使用读锁
     * 多个线程可以同时读取数据
     * @return 数据内容
     */
    public String read() {
        // 获取读锁
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "：读取数据 - " + data);
            // 模拟读取耗时
            Thread.sleep((long) (Math.random() * 10));
            return data;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            // 释放读锁
            lock.readLock().unlock();
        }
    }
    
    /**
     * 写入数据 - 使用写锁
     * 写操作需要独占锁
     * @param newData 新数据
     */
    public void write(String newData) {
        // 获取写锁
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "：写入数据 - " + newData);
            // 模拟写入耗时
            Thread.sleep((long) (Math.random() * 50));
            this.data = newData;
            System.out.println(Thread.currentThread().getName() + "：写入完成 - " + data);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 释放写锁
            lock.writeLock().unlock();
        }
    }
    
    /**
     * 获取数据长度 - 使用读锁
     * @return 数据长度
     */
    public int length() {
        lock.readLock().lock();
        try {
            return data != null ? data.length() : 0;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 检查数据是否包含指定内容 - 使用读锁
     * @param substring 子字符串
     * @return 是否包含
     */
    public boolean contains(String substring) {
        lock.readLock().lock();
        try {
            return data != null && data.contains(substring);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 获取当前数据的快照
     * @return 数据快照
     */
    public String snapshot() {
        lock.readLock().lock();
        try {
            return data != null ? new String(data) : null;
        } finally {
            lock.readLock().unlock();
        }
    }
}