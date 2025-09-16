package cn.geekslife.designpattern.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点类
 * @param <T> 节点数据类型
 */
public class TreeNode<T> {
    private T data;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;
    
    public TreeNode(T data) {
        this.data = data;
        this.children = new ArrayList<>();
    }
    
    public TreeNode(T data, TreeNode<T> parent) {
        this(data);
        this.parent = parent;
    }
    
    public void addChild(TreeNode<T> child) {
        child.setParent(this);
        this.children.add(child);
    }
    
    public void removeChild(TreeNode<T> child) {
        child.setParent(null);
        this.children.remove(child);
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public TreeNode<T> getParent() {
        return parent;
    }
    
    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }
    
    public List<TreeNode<T>> getChildren() {
        return children;
    }
    
    public boolean isLeaf() {
        return children.isEmpty();
    }
    
    public boolean isRoot() {
        return parent == null;
    }
    
    public int getDepth() {
        int depth = 0;
        TreeNode<T> current = this;
        while (current.getParent() != null) {
            depth++;
            current = current.getParent();
        }
        return depth;
    }
    
    @Override
    public String toString() {
        return "TreeNode{" +
                "data=" + data +
                ", children=" + children.size() +
                '}';
    }
}