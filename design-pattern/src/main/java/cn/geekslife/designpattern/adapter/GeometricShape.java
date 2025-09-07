package cn.geekslife.designpattern.adapter;

/**
 * 几何形状接口 - 目标接口2
 */
public interface GeometricShape {
    void render();
    void scale(double factor);
}