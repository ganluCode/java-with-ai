package cn.geekslife.designpattern.composite;

/**
 * 点 - 叶子节点
 */
public class Dot extends Graphic {
    private int x, y;
    
    public Dot(String name, int x, int y) {
        super(name);
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void draw() {
        System.out.println("绘制点 " + name + " 在坐标 (" + x + ", " + y + ")");
    }
    
    @Override
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
        System.out.println("移动点 " + name + " 到坐标 (" + this.x + ", " + this.y + ")");
    }
    
    @Override
    public double getArea() {
        return 0; // 点没有面积
    }
    
    // getter和setter方法
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}