package cn.geekslife.designpattern.iterator;

/**
 * 树形结构迭代器接口
 * 用于遍历树形结构
 * @param <T> 元素类型
 */
public interface TreeIterator<T> extends Iterator<T> {
    /**
     * 设置遍历方式
     * @param traversalType 遍历方式
     */
    void setTraversalType(TraversalType traversalType);
    
    /**
     * 获取遍历方式
     * @return 遍历方式
     */
    TraversalType getTraversalType();
    
    /**
     * 获取当前节点的深度
     * @return 当前节点深度
     */
    int getCurrentDepth();
    
    /**
     * 判断当前节点是否为叶子节点
     * @return 如果是叶子节点返回true，否则返回false
     */
    boolean isLeaf();
}