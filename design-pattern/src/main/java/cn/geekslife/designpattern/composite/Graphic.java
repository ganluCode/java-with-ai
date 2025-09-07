package cn.geekslife.designpattern.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 图形组件抽象类 - 组件接口
 */
public abstract class Graphic {
    protected String name;
    
    public Graphic(String name) {
        this.name = name;
    }
    
    public abstract void draw();
    public abstract void move(int x, int y);
    public abstract double getArea();
    
    // 默认实现
    public void add(Graphic graphic) {
        throw new UnsupportedOperationException("不支持此操作");
    }
    
    public void remove(Graphic graphic) {
        throw new UnsupportedOperationException("不支持此操作");
    }
    
    public Graphic getChild(int i) {
        throw new UnsupportedOperationException("不支持此操作");
    }
    
    // getter方法
    public String getName() {
        return name;
    }
}