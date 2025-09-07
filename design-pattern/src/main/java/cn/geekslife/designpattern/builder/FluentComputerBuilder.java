package cn.geekslife.designpattern.builder;

/**
 * 流式建造者实现
 * 提供更灵活的建造方式
 */
public class FluentComputerBuilder implements ComputerBuilder {
    private Computer computer;
    
    /**
     * 构造函数，初始化产品对象
     */
    public FluentComputerBuilder() {
        this.computer = new Computer();
    }
    
    @Override
    public FluentComputerBuilder buildCPU(String cpu) {
        computer.setCpu(cpu);
        return this;
    }
    
    @Override
    public FluentComputerBuilder buildMemory(String memory) {
        computer.setMemory(memory);
        return this;
    }
    
    @Override
    public FluentComputerBuilder buildStorage(String storage) {
        computer.setStorage(storage);
        return this;
    }
    
    @Override
    public FluentComputerBuilder buildGraphicsCard(String graphicsCard) {
        computer.setGraphicsCard(graphicsCard);
        return this;
    }
    
    @Override
    public FluentComputerBuilder buildMotherboard(String motherboard) {
        computer.setMotherboard(motherboard);
        return this;
    }
    
    @Override
    public FluentComputerBuilder buildPowerSupply(String powerSupply) {
        computer.setPowerSupply(powerSupply);
        return this;
    }
    
    @Override
    public FluentComputerBuilder buildCoolingSystem(String coolingSystem) {
        computer.setCoolingSystem(coolingSystem);
        return this;
    }
    
    @Override
    public Computer build() {
        Computer result = this.computer;
        reset();
        return result;
    }
    
    @Override
    public FluentComputerBuilder reset() {
        this.computer = new Computer();
        return this;
    }
    
    /**
     * 快速构建基础计算机
     * @param cpu CPU型号
     * @param memory 内存规格
     * @param storage 存储规格
     * @return 流式建造者实例
     */
    public FluentComputerBuilder buildBasic(String cpu, String memory, String storage) {
        return buildCPU(cpu)
                .buildMemory(memory)
                .buildStorage(storage);
    }
    
    /**
     * 快速构建高性能计算机
     * @param cpu CPU型号
     * @param memory 内存规格
     * @param storage 存储规格
     * @param graphicsCard 显卡型号
     * @return 流式建造者实例
     */
    public FluentComputerBuilder buildHighPerformance(String cpu, String memory, String storage, String graphicsCard) {
        return buildBasic(cpu, memory, storage)
                .buildGraphicsCard(graphicsCard);
    }
    
    /**
     * 获取当前计算机配置（用于调试）
     * @return 当前计算机对象
     */
    public Computer getCurrentComputer() {
        return computer;
    }
}