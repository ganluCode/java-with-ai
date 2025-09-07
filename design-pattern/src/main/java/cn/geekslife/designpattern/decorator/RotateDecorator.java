package cn.geekslife.designpattern.decorator;

/**
 * 旋转效果装饰器 - 具体装饰器
 */
public class RotateDecorator extends ShapeDecorator {
    private double angle;
    
    public RotateDecorator(Shape shape, double angle) {
        super(shape);
        this.angle = angle;
    }
    
    @Override
    public void draw() {
        super.draw();
        rotate();
    }
    
    private void rotate() {
        System.out.println("  旋转 " + angle + " 度");
    }
    
    // getter和setter
    public double getAngle() { return angle; }
    public void setAngle(double angle) { this.angle = angle; }
}