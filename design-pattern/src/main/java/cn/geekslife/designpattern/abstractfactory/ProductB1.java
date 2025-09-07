package cn.geekslife.designpattern.abstractfactory;

/**
 * 具体产品B1
 * 实现抽象产品B接口
 */
public class ProductB1 implements AbstractProductB {
    
    private static final String TYPE = "类型B1";
    private static final String DESCRIPTION = "这是产品B1的详细描述";
    
    @Override
    public void operate() {
        System.out.println("操作" + TYPE + "，描述：" + DESCRIPTION);
    }
    
    @Override
    public String getType() {
        return TYPE;
    }
    
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}