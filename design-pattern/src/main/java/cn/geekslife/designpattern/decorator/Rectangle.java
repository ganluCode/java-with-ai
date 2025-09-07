package cn.geekslife.designpattern.decorator;

/**
 * 矩形 - 具体组件
 */
public class Rectangle implements Shape {
    private double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void draw() {
        System.out.println("绘制矩形，宽度: " + width + ", 高度: " + height);
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
    
    // getter和setter
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
}