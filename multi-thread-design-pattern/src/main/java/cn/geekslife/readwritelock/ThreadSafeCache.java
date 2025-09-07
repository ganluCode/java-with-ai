package cn.geekslife.readwritelock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 线程安全的缓存类 - 演示Read-Write Lock模式在缓存中的应用
 */
public class ThreadSafeCache<K, V> {
    // 缓存数据
    private final Map<K, V> cache = new HashMap<>();
    // 读写锁
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    /**
     * 获取缓存值
     * @param key 键
     * @return 值
     */
    public V get(K key) {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "：读取缓存键 " + key);
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 设置缓存值
     * @param key 键
     * @param value 值
     */
    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "：设置缓存键 " + key + " = " + value);
            cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * 删除缓存值
     * @param key 键
     * @return 被删除的值
     */
    public V remove(K key) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "：删除缓存键 " + key);
            return cache.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * 检查是否包含键
     * @param key 键
     * @return 是否包含
     */
    public boolean containsKey(K key) {
        lock.readLock().lock();
        try {
            return cache.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 获取缓存大小
     * @return 缓存大小
     */
    public int size() {
        lock.readLock().lock();
        try {
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 清空缓存
     */
    public void clear() {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "：清空缓存");
            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * 获取所有键的快照
     * @return 键集合
     */
    public Iterable<K> keys() {
        lock.readLock().lock();
        try {
            return new HashMap<>(cache).keySet();
        } finally {
            lock.readLock().unlock();
        }
    }
}