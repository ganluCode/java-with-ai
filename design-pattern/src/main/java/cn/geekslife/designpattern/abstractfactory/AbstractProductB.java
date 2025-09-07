package cn.geekslife.designpattern.abstractfactory;

/**
 * 抽象产品B接口
 * 定义产品B的通用接口
 */
public interface AbstractProductB {
    
    /**
     * 产品B的操作方法
     */
    void operate();
    
    /**
     * 获取产品B的类型
     * @return 产品B类型
     */
    String getType();
    
    /**
     * 获取产品B的功能描述
     * @return 功能描述
     */
    String getDescription();
}