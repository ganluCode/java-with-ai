package cn.geekslife.designpattern.decorator;

/**
 * 圆形 - 具体组件
 */
public class Circle implements Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public void draw() {
        System.out.println("绘制圆形，半径: " + radius);
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
    
    // getter和setter
    public double getRadius() { return radius; }
    public void setRadius(double radius) { this.radius = radius; }
}