package cn.geekslife.designpattern.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体树形聚合实现
 * @param <T> 元素类型
 */
public class ConcreteTreeAggregate<T> implements TreeAggregate<T> {
    private TreeNode<T> root;
    
    public ConcreteTreeAggregate() {
    }
    
    public ConcreteTreeAggregate(TreeNode<T> root) {
        this.root = root;
    }
    
    @Override
    public Iterator<T> createIterator() {
        // 为树形结构创建专门的迭代器
        return new TreeIteratorImpl<>(this);
    }
    
    @Override
    public int count() {
        if (root == null) return 0;
        return countNodes(root);
    }
    
    private int countNodes(TreeNode<T> node) {
        int count = 1; // 当前节点
        for (TreeNode<T> child : node.getChildren()) {
            count += countNodes(child);
        }
        return count;
    }
    
    @Override
    public T get(int index) {
        if (root == null) return null;
        List<T> items = new ArrayList<>();
        collectItems(root, items);
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }
    
    private void collectItems(TreeNode<T> node, List<T> items) {
        items.add(node.getData());
        for (TreeNode<T> child : node.getChildren()) {
            collectItems(child, items);
        }
    }
    
    @Override
    public void set(int index, T item) {
        // 树形结构不支持通过索引设置元素
        throw new UnsupportedOperationException("Tree structure does not support setting elements by index");
    }
    
    @Override
    public void add(T item) {
        if (root == null) {
            root = new TreeNode<>(item);
        } else {
            // 添加到根节点的第一个子节点
            TreeNode<T> newNode = new TreeNode<>(item);
            if (!root.getChildren().isEmpty()) {
                root.getChildren().get(0).addChild(newNode);
            } else {
                root.addChild(newNode);
            }
        }
    }
    
    @Override
    public boolean remove(T item) {
        if (root == null) return false;
        return removeNodeWithData(root, item);
    }
    
    private boolean removeNodeWithData(TreeNode<T> node, T item) {
        if (node.getData() != null && node.getData().equals(item)) {
            if (node.isRoot()) {
                root = null;
            } else {
                node.getParent().removeChild(node);
            }
            return true;
        }
        
        for (TreeNode<T> child : new ArrayList<>(node.getChildren())) {
            if (removeNodeWithData(child, item)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void clear() {
        root = null;
    }
    
    @Override
    public TreeNode<T> getRoot() {
        return root;
    }
    
    @Override
    public void setRoot(TreeNode<T> root) {
        this.root = root;
    }
    
    @Override
    public void addChild(TreeNode<T> parent, TreeNode<T> child) {
        if (parent != null) {
            parent.addChild(child);
        }
    }
    
    @Override
    public void removeNode(TreeNode<T> node) {
        if (node != null && node.getParent() != null) {
            node.getParent().removeChild(node);
        }
    }
    
    @Override
    public int getNodeCount() {
        return count();
    }
    
    /**
     * 树形迭代器实现
     */
    private static class TreeIteratorImpl<T> implements Iterator<T> {
        private ConcreteTreeAggregate<T> treeAggregate;
        private List<T> items;
        private int currentIndex = 0;
        
        public TreeIteratorImpl(ConcreteTreeAggregate<T> treeAggregate) {
            this.treeAggregate = treeAggregate;
            this.items = new ArrayList<>();
            if (treeAggregate.getRoot() != null) {
                collectItems(treeAggregate.getRoot(), items);
            }
        }
        
        private void collectItems(TreeNode<T> node, List<T> items) {
            items.add(node.getData());
            for (TreeNode<T> child : node.getChildren()) {
                collectItems(child, items);
            }
        }
        
        @Override
        public void first() {
            currentIndex = 0;
        }
        
        @Override
        public void next() {
            if (currentIndex < items.size()) {
                currentIndex++;
            }
        }
        
        @Override
        public boolean hasNext() {
            return currentIndex < items.size();
        }
        
        @Override
        public T currentItem() {
            if (currentIndex >= 0 && currentIndex < items.size()) {
                return items.get(currentIndex);
            }
            return null;
        }
        
        @Override
        public boolean isFirst() {
            return currentIndex == 0;
        }
        
        @Override
        public boolean isLast() {
            return currentIndex == items.size() - 1;
        }
    }
}