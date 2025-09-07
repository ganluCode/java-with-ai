package cn.geekslife.designpattern.builder;

/**
 * 抽象建造者接口
 * 定义创建产品各个部分的接口
 */
public interface ComputerBuilder {
    
    /**
     * 构建CPU
     * @param cpu CPU型号
     * @return 建造者实例
     */
    ComputerBuilder buildCPU(String cpu);
    
    /**
     * 构建内存
     * @param memory 内存规格
     * @return 建造者实例
     */
    ComputerBuilder buildMemory(String memory);
    
    /**
     * 构建存储
     * @param storage 存储规格
     * @return 建造者实例
     */
    ComputerBuilder buildStorage(String storage);
    
    /**
     * 构建显卡
     * @param graphicsCard 显卡型号
     * @return 建造者实例
     */
    ComputerBuilder buildGraphicsCard(String graphicsCard);
    
    /**
     * 构建主板
     * @param motherboard 主板型号
     * @return 建造者实例
     */
    ComputerBuilder buildMotherboard(String motherboard);
    
    /**
     * 构建电源
     * @param powerSupply 电源规格
     * @return 建造者实例
     */
    ComputerBuilder buildPowerSupply(String powerSupply);
    
    /**
     * 构建散热系统
     * @param coolingSystem 散热系统规格
     * @return 建造者实例
     */
    ComputerBuilder buildCoolingSystem(String coolingSystem);
    
    /**
     * 获取构建完成的产品
     * @return 构建完成的计算机产品
     */
    Computer build();
    
    /**
     * 重置建造者状态，准备构建新的产品
     * @return 建造者实例
     */
    ComputerBuilder reset();
}