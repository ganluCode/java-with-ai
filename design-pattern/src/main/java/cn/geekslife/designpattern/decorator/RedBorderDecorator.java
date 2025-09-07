package cn.geekslife.designpattern.decorator;

/**
 * 红色边框装饰器 - 具体装饰器
 */
public class RedBorderDecorator extends ShapeDecorator {
    public RedBorderDecorator(Shape shape) {
        super(shape);
    }
    
    @Override
    public void draw() {
        super.draw();
        setRedBorder();
    }
    
    private void setRedBorder() {
        System.out.println("  添加红色边框");
    }
}