package cn.geekslife.designpattern.factorymethod;

/**
 * 抽象产品接口
 * 定义产品的通用接口
 */
public interface Product {
    
    /**
     * 产品使用方法
     */
    void use();
    
    /**
     * 获取产品名称
     * @return 产品名称
     */
    String getName();
    
    /**
     * 获取产品价格
     * @return 产品价格
     */
    double getPrice();
}