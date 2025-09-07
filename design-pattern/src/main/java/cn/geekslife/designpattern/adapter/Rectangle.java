package cn.geekslife.designpattern.adapter;

/**
 * 矩形类 - 需要被适配的类
 */
public class Rectangle {
    private int width;
    private int height;
    
    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public void display() {
        System.out.println("Displaying rectangle: " + width + "x" + height);
    }
    
    public void changeSize(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println("Resizing rectangle to: " + width + "x" + height);
    }
    
    // getter方法
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
}