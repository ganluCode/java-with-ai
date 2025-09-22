package cn.geekslife.readwritelock;

import java.util.concurrent.locks.StampedLock;

/**
 * 使用StampedLock实现的数据类
 * 演示Java 8引入的StampedLock的使用
 */
public class StampedData {
    // 共享数据
    private String data;
    // StampedLock
    private final StampedLock lock = new StampedLock();
    
    /**
     * 构造函数
     * @param initialData 初始数据
     */
    public StampedData(String initialData) {
        this.data = initialData;
    }
    
    /**
     * 读取数据 - 使用乐观读锁
     * @return 数据内容
     */
    public String readOptimistic() {
        // 尝试获取乐观读锁
        long stamp = lock.tryOptimisticRead();
        String currentData = data;
        
        // 验证数据是否被修改
        if (!lock.validate(stamp)) {
            // 乐观读失败，获取悲观读锁
            System.out.println(Thread.currentThread().getName() + "：乐观读失败，使用悲观读锁");
            stamp = lock.readLock();
            try {
                currentData = data;
                System.out.println(Thread.currentThread().getName() + "：悲观读取数据 - " + currentData);
            } finally {
                lock.unlockRead(stamp);
            }
        } else {
            System.out.println(Thread.currentThread().getName() + "：乐观读取数据 - " + currentData);
        }
        
        return currentData;
    }
    
    /**
     * 读取数据 - 使用悲观读锁
     * @return 数据内容
     */
    public String read() {
        long stamp = lock.readLock();
        try {
            System.out.println(Thread.currentThread().getName() + "：读取数据 - " + data);
            // 模拟读取耗时
            Thread.sleep((long) (Math.random() * 10));
            return data;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlockRead(stamp);
        }
    }
    
    /**
     * 写入数据
     * @param newData 新数据
     */
    public void write(String newData) {
        long stamp = lock.writeLock();
        try {
            System.out.println(Thread.currentThread().getName() + "：写入数据 - " + newData);
            // 模拟写入耗时
            Thread.sleep((long) (Math.random() * 50));
            this.data = newData;
            System.out.println(Thread.currentThread().getName() + "：写入完成 - " + data);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlockWrite(stamp);
        }
    }
    
    /**
     * 读取并写入数据 - 演示锁转换
     * @param newData 新数据
     * @return 原始数据
     */
    public String readAndWrite(String newData) {
        // 获取读锁
        long stamp = lock.readLock();
        String originalData = null;
        try {
            originalData = data;
            System.out.println(Thread.currentThread().getName() + "：读取原始数据 - " + originalData);
            
            // 尝试升级为写锁
            long writeStamp = lock.tryConvertToWriteLock(stamp);
            if (writeStamp != 0L) {
                // 升级成功
                stamp = writeStamp;
                System.out.println(Thread.currentThread().getName() + "：锁升级成功，写入新数据 - " + newData);
                this.data = newData;
            } else {
                // 升级失败，释放读锁并获取写锁
                lock.unlockRead(stamp);
                stamp = lock.writeLock();
                System.out.println(Thread.currentThread().getName() + "：锁升级失败，重新获取写锁，写入新数据 - " + newData);
                this.data = newData;
            }
        } finally {
            lock.unlock(stamp);
        }
        
        return originalData;
    }
}