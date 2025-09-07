package cn.geekslife.designpattern.decorator;

/**
 * 蓝色填充装饰器 - 具体装饰器
 */
public class BlueFillDecorator extends ShapeDecorator {
    public BlueFillDecorator(Shape shape) {
        super(shape);
    }
    
    @Override
    public void draw() {
        super.draw();
        setBlueFill();
    }
    
    private void setBlueFill() {
        System.out.println("  添加蓝色填充");
    }
}