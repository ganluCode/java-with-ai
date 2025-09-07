package cn.geekslife.designpattern.bridge;

/**
 * 形状抽象类 - 抽象类
 */
public abstract class Shape {
    protected DrawAPI drawAPI;
    
    protected Shape(DrawAPI drawAPI) {
        this.drawAPI = drawAPI;
    }
    
    public abstract void draw();
}