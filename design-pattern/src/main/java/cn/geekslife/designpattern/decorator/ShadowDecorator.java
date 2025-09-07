package cn.geekslife.designpattern.decorator;

/**
 * 阴影效果装饰器 - 具体装饰器
 */
public class ShadowDecorator extends ShapeDecorator {
    public ShadowDecorator(Shape shape) {
        super(shape);
    }
    
    @Override
    public void draw() {
        super.draw();
        addShadow();
    }
    
    private void addShadow() {
        System.out.println("  添加阴影效果");
    }
}