package cn.geekslife.designpattern.bridge;

/**
 * 绘制API接口 - 实现类接口
 */
public interface DrawAPI {
    void drawCircle(int radius, int x, int y);
    void drawRectangle(int width, int height, int x, int y);
}