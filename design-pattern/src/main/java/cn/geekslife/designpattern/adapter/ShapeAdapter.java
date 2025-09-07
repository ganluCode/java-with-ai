package cn.geekslife.designpattern.adapter;

/**
 * 双向适配器 - 同时实现两个目标接口
 */
public class ShapeAdapter implements Shape, GeometricShape {
    private Rectangle rectangle;
    
    public ShapeAdapter(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
    
    // 实现Shape接口
    @Override
    public void draw() {
        rectangle.display();
    }
    
    @Override
    public void resize() {
        // 假设调整为100x50
        rectangle.changeSize(100, 50);
    }
    
    // 实现GeometricShape接口
    @Override
    public void render() {
        rectangle.display();
    }
    
    @Override
    public void scale(double factor) {
        // 简化实现
        int newWidth = (int) (rectangle.getWidth() * factor);
        int newHeight = (int) (rectangle.getHeight() * factor);
        rectangle.changeSize(newWidth, newHeight);
        System.out.println("Scaling rectangle by factor: " + factor);
    }
}