package cn.geekslife.designpattern.iterator;

/**
 * 树形聚合接口
 * @param <T> 元素类型
 */
public interface TreeAggregate<T> extends Aggregate<T> {
    /**
     * 获取根节点
     * @return 根节点
     */
    TreeNode<T> getRoot();
    
    /**
     * 设置根节点
     * @param root 根节点
     */
    void setRoot(TreeNode<T> root);
    
    /**
     * 添加子节点
     * @param parent 父节点
     * @param child 子节点
     */
    void addChild(TreeNode<T> parent, TreeNode<T> child);
    
    /**
     * 移除节点
     * @param node 节点
     */
    void removeNode(TreeNode<T> node);
    
    /**
     * 获取节点数量
     * @return 节点数量
     */
    int getNodeCount();
}