package cn.geekslife.designpattern.factorymethod;

/**
 * 参数化工厂方法创建者
 * 通过参数决定创建哪种产品
 */
public abstract class ParameterizedCreator {
    
    /**
     * 参数化工厂方法
     * @param type 产品类型
     * @return 产品实例
     */
    public abstract Product factoryMethod(String type);
    
    /**
     * 创建产品
     * @param type 产品类型
     * @return 产品实例
     */
    public Product createProduct(String type) {
        Product product = factoryMethod(type);
        // 可以添加一些通用的初始化逻辑
        System.out.println("创建了" + type + "类型的产品");
        return product;
    }
    
    /**
     * 批量创建产品
     * @param type 产品类型
     * @param count 数量
     * @return 产品数组
     */
    public Product[] createProducts(String type, int count) {
        Product[] products = new Product[count];
        for (int i = 0; i < count; i++) {
            products[i] = factoryMethod(type);
        }
        return products;
    }
}