package cn.geekslife.designpattern.abstractfactory;

/**
 * 抽象工厂接口
 * 定义创建产品族的接口
 */
public interface AbstractFactory {
    
    /**
     * 创建产品A
     * @return 产品A实例
     */
    AbstractProductA createProductA();
    
    /**
     * 创建产品B
     * @return 产品B实例
     */
    AbstractProductB createProductB();
    
    /**
     * 创建完整的产品族
     * @return 产品族包装对象
     */
    default ProductFamily createProductFamily() {
        return new ProductFamily(createProductA(), createProductB());
    }
}