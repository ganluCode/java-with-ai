package cn.geekslife.designpattern.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹 - 容器节点
 */
public class Folder extends FileSystemComponent {
    private List<FileSystemComponent> children = new ArrayList<>();
    
    public Folder(String name) {
        super(name);
    }
    
    @Override
    public void add(FileSystemComponent component) {
        children.add(component);
    }
    
    @Override
    public void remove(FileSystemComponent component) {
        children.remove(component);
    }
    
    @Override
    public FileSystemComponent getChild(int i) {
        return children.get(i);
    }
    
    @Override
    public void display(int depth) {
        System.out.println(getDepthString(depth) + "+ Folder: " + name);
        for (FileSystemComponent component : children) {
            component.display(depth + 1);
        }
    }
    
    @Override
    public long getSize() {
        long totalSize = 0;
        for (FileSystemComponent component : children) {
            totalSize += component.getSize();
        }
        return totalSize;
    }
    
    // getter方法
    public List<FileSystemComponent> getChildren() {
        return new ArrayList<>(children);
    }
}