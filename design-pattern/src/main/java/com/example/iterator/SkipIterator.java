package com.example.iterator;

/**
 * 跳过迭代器接口
 * 支持跳过指定数量的元素进行遍历
 * @param <T> 元素类型
 */
public interface SkipIterator<T> extends Iterator<T> {
    /**
     * 设置跳过步长
     * @param step 跳过步长
     */
    void setStep(int step);
    
    /**
     * 获取当前步长
     * @return 当前步长
     */
    int getStep();
    
    /**
     * 跳过指定数量的元素
     * @param count 跳过数量
     */
    void skip(int count);
}