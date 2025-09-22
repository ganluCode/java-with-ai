package cn.geekslife.designpattern.iterator;

/**
 * 迭代器接口
 * 定义访问和遍历元素的接口
 * @param <T> 元素类型
 */
public interface Iterator<T> {
    /**
     * 移动到第一个元素
     */
    void first();
    
    /**
     * 移动到下一个元素
     */
    void next();
    
    /**
     * 判断是否还有下一个元素
     * @return 如果还有下一个元素返回true，否则返回false
     */
    boolean hasNext();
    
    /**
     * 获取当前元素
     * @return 当前元素
     */
    T currentItem();
    
    /**
     * 判断是否是第一个元素
     * @return 如果是第一个元素返回true，否则返回false
     */
    boolean isFirst();
    
    /**
     * 判断是否是最后一个元素
     * @return 如果是最后一个元素返回true，否则返回false
     */
    boolean isLast();
}