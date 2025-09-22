package cn.geekslife.designpattern.iterator;

/**
 * 聚合接口
 * 定义创建相应迭代器对象的接口
 * @param <T> 元素类型
 */
public interface Aggregate<T> {
    /**
     * 创建迭代器
     * @return 迭代器对象
     */
    Iterator<T> createIterator();
    
    /**
     * 获取元素数量
     * @return 元素数量
     */
    int count();
    
    /**
     * 获取指定位置的元素
     * @param index 索引
     * @return 元素
     */
    T get(int index);
    
    /**
     * 设置指定位置的元素
     * @param index 索引
     * @param item 元素
     */
    void set(int index, T item);
    
    /**
     * 添加元素
     * @param item 元素
     */
    void add(T item);
    
    /**
     * 移除元素
     * @param item 元素
     * @return 是否移除成功
     */
    boolean remove(T item);
    
    /**
     * 清空所有元素
     */
    void clear();
}