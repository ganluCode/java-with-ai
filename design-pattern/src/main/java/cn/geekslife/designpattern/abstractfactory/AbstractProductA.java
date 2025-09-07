package cn.geekslife.designpattern.abstractfactory;

/**
 * 抽象产品A接口
 * 定义产品A的通用接口
 */
public interface AbstractProductA {
    
    /**
     * 产品A的使用方法
     */
    void use();
    
    /**
     * 获取产品A的名称
     * @return 产品A名称
     */
    String getName();
    
    /**
     * 获取产品A的价格
     * @return 产品A价格
     */
    double getPrice();
}