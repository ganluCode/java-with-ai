package cn.geekslife.designpattern.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 图形组合 - 容器节点
 */
public class CompoundGraphic extends Graphic {
    private List<Graphic> children = new ArrayList<>();
    
    public CompoundGraphic(String name) {
        super(name);
    }
    
    @Override
    public void add(Graphic graphic) {
        children.add(graphic);
    }
    
    @Override
    public void remove(Graphic graphic) {
        children.remove(graphic);
    }
    
    @Override
    public Graphic getChild(int i) {
        return children.get(i);
    }
    
    @Override
    public void draw() {
        System.out.println("绘制组合图形 " + name + ":");
        for (Graphic graphic : children) {
            graphic.draw();
        }
    }
    
    @Override
    public void move(int x, int y) {
        System.out.println("移动组合图形 " + name + ":");
        for (Graphic graphic : children) {
            graphic.move(x, y);
        }
    }
    
    @Override
    public double getArea() {
        double totalArea = 0;
        for (Graphic graphic : children) {
            totalArea += graphic.getArea();
        }
        return totalArea;
    }
    
    // getter方法
    public List<Graphic> getChildren() {
        return new ArrayList<>(children);
    }
}