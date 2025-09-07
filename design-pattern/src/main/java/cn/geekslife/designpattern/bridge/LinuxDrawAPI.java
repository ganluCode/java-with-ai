package cn.geekslife.designpattern.bridge;

/**
 * Linux绘制实现 - 具体实现类
 */
public class LinuxDrawAPI implements DrawAPI {
    @Override
    public void drawCircle(int radius, int x, int y) {
        System.out.println("Linux: Drawing Circle[ radius: " + radius + ", x: " + x + ", y: " + y + "]");
    }
    
    @Override
    public void drawRectangle(int width, int height, int x, int y) {
        System.out.println("Linux: Drawing Rectangle[ width: " + width + ", height: " + height + ", x: " + x + ", y: " + y + "]");
    }
}