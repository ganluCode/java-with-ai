package com.example.iterator;

/**
 * 双向迭代器接口
 * 支持正向和反向遍历
 * @param <T> 元素类型
 */
public interface BidirectionalIterator<T> extends Iterator<T> {
    /**
     * 移动到最后一个元素
     */
    void last();
    
    /**
     * 移动到上一个元素
     */
    void previous();
    
    /**
     * 判断是否还有上一个元素
     * @return 如果还有上一个元素返回true，否则返回false
     */
    boolean hasPrevious();
}