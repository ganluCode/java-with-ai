package cn.geekslife.designpattern.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件系统组件抽象类 - 组件接口
 */
public abstract class FileSystemComponent {
    protected String name;
    
    public FileSystemComponent(String name) {
        this.name = name;
    }
    
    public abstract void display(int depth);
    public abstract long getSize();
    
    // 默认实现，叶子节点不需要这些操作
    public void add(FileSystemComponent component) {
        throw new UnsupportedOperationException("不支持此操作");
    }
    
    public void remove(FileSystemComponent component) {
        throw new UnsupportedOperationException("不支持此操作");
    }
    
    public FileSystemComponent getChild(int i) {
        throw new UnsupportedOperationException("不支持此操作");
    }
    
    protected String getDepthString(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }
    
    // getter方法
    public String getName() {
        return name;
    }
}