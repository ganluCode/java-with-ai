package cn.geekslife.designpattern.factorymethod;

/**
 * 静态工厂类
 * 提供静态方法创建产品
 */
public class StaticFactory {
    
    /**
     * 静态工厂方法
     * @param type 产品类型
     * @return 产品实例
     */
    public static Product createProduct(String type) {
        switch (type.toUpperCase()) {
            case "A":
                return new ConcreteProductA();
            case "B":
                return new ConcreteProductB();
            case "C":
                return new ConcreteProductC();
            default:
                throw new IllegalArgumentException("未知的产品类型: " + type);
        }
    }
    
    /**
     * 静态工厂方法（带数量参数）
     * @param type 产品类型
     * @param count 数量
     * @return 产品数组
     */
    public static Product[] createProducts(String type, int count) {
        Product[] products = new Product[count];
        for (int i = 0; i < count; i++) {
            products[i] = createProduct(type);
        }
        return products;
    }
    
    /**
     * 获取产品描述
     * @param type 产品类型
     * @return 产品描述
     */
    public static String getProductDescription(String type) {
        Product product = createProduct(type);
        return product.getName() + " - 价格: " + product.getPrice() + "元";
    }
}