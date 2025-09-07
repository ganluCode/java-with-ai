package cn.geekslife.designpattern.composite;

/**
 * 圆形 - 叶子节点
 */
public class Circle extends Graphic {
    private int x, y, radius;
    
    public Circle(String name, int x, int y, int radius) {
        super(name);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    
    @Override
    public void draw() {
        System.out.println("绘制圆形 " + name + " 在坐标 (" + x + ", " + y + ") 半径 " + radius);
    }
    
    @Override
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
        System.out.println("移动圆形 " + name + " 到坐标 (" + this.x + ", " + this.y + ")");
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
    
    // getter和setter方法
    public int getX() { return x; }
    public int getY() { return y; }
    public int getRadius() { return radius; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setRadius(int radius) { this.radius = radius; }
}