package cn.geekslife.designpattern.composite;

/**
 * 文件 - 叶子节点
 */
public class File extends FileSystemComponent {
    private long size;
    
    public File(String name, long size) {
        super(name);
        this.size = size;
    }
    
    @Override
    public void display(int depth) {
        System.out.println(getDepthString(depth) + "- File: " + name + " (" + size + " bytes)");
    }
    
    @Override
    public long getSize() {
        return size;
    }
    
    // getter和setter方法
    public long getSizeValue() {
        return size;
    }
    
    public void setSize(long size) {
        this.size = size;
    }
}