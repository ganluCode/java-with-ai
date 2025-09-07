package com.example.iterator;

import java.util.function.Predicate;

/**
 * 过滤迭代器接口
 * 支持根据条件过滤元素进行遍历
 * @param <T> 元素类型
 */
public interface FilterIterator<T> extends Iterator<T> {
    /**
     * 设置过滤条件
     * @param predicate 过滤条件
     */
    void setFilter(Predicate<T> predicate);
    
    /**
     * 获取过滤条件
     * @return 过滤条件
     */
    Predicate<T> getFilter();
    
    /**
     * 重置迭代器到开始位置
     */
    void reset();
}