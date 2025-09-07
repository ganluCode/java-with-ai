package cn.geekslife.designpattern.factorymethod;

/**
 * 抽象创建者
 * 声明工厂方法，定义产品的创建接口
 */
public abstract class Creator {
    
    /**
     * 工厂方法
     * 由具体创建者实现，用于创建具体产品
     * @return 产品实例
     */
    public abstract Product factoryMethod();
    
    /**
     * 业务方法
     * 使用工厂方法创建产品并使用它
     */
    public void anOperation() {
        Product product = factoryMethod();
        System.out.println("创建了产品: " + product.getName());
        product.use();
    }
    
    /**
     * 获取产品信息
     * @return 产品信息字符串
     */
    public String getProductInfo() {
        Product product = factoryMethod();
        return "产品名称: " + product.getName() + ", 价格: " + product.getPrice() + "元";
    }
}