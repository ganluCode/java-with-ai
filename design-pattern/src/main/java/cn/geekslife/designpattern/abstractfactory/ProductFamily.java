package cn.geekslife.designpattern.abstractfactory;

/**
 * 产品族包装类
 * 封装一组相关的产品
 */
public class ProductFamily {
    
    private final AbstractProductA productA;
    private final AbstractProductB productB;
    
    public ProductFamily(AbstractProductA productA, AbstractProductB productB) {
        this.productA = productA;
        this.productB = productB;
    }
    
    /**
     * 使用产品族
     */
    public void useFamily() {
        System.out.println("=== 使用产品族 ===");
        productA.use();
        productB.operate();
        System.out.println("=== 产品族使用完毕 ===");
    }
    
    /**
     * 获取产品A
     * @return 产品A
     */
    public AbstractProductA getProductA() {
        return productA;
    }
    
    /**
     * 获取产品B
     * @return 产品B
     */
    public AbstractProductB getProductB() {
        return productB;
    }
    
    /**
     * 获取产品族信息
     * @return 产品族信息字符串
     */
    public String getFamilyInfo() {
        return "产品族信息: " + productA.getName() + " + " + productB.getType();
    }
}