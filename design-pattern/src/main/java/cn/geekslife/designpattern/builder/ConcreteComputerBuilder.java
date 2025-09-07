package cn.geekslife.designpattern.builder;

/**
 * 具体建造者实现
 * 实现Builder接口，构造和装配各个部件
 */
public class ConcreteComputerBuilder implements ComputerBuilder {
    private Computer computer;
    
    /**
     * 构造函数，初始化产品对象
     */
    public ConcreteComputerBuilder() {
        this.computer = new Computer();
    }
    
    @Override
    public ComputerBuilder buildCPU(String cpu) {
        computer.setCpu(cpu);
        System.out.println("构建CPU: " + cpu);
        return this;
    }
    
    @Override
    public ComputerBuilder buildMemory(String memory) {
        computer.setMemory(memory);
        System.out.println("构建内存: " + memory);
        return this;
    }
    
    @Override
    public ComputerBuilder buildStorage(String storage) {
        computer.setStorage(storage);
        System.out.println("构建存储: " + storage);
        return this;
    }
    
    @Override
    public ComputerBuilder buildGraphicsCard(String graphicsCard) {
        computer.setGraphicsCard(graphicsCard);
        System.out.println("构建显卡: " + graphicsCard);
        return this;
    }
    
    @Override
    public ComputerBuilder buildMotherboard(String motherboard) {
        computer.setMotherboard(motherboard);
        System.out.println("构建主板: " + motherboard);
        return this;
    }
    
    @Override
    public ComputerBuilder buildPowerSupply(String powerSupply) {
        computer.setPowerSupply(powerSupply);
        System.out.println("构建电源: " + powerSupply);
        return this;
    }
    
    @Override
    public ComputerBuilder buildCoolingSystem(String coolingSystem) {
        computer.setCoolingSystem(coolingSystem);
        System.out.println("构建散热系统: " + coolingSystem);
        return this;
    }
    
    @Override
    public Computer build() {
        Computer result = this.computer;
        reset(); // 重置建造者，准备构建下一个产品
        System.out.println("计算机构建完成!");
        return result;
    }
    
    @Override
    public ComputerBuilder reset() {
        this.computer = new Computer();
        return this;
    }
    
    /**
     * 获取当前正在构建的计算机（用于调试或中间状态检查）
     * @return 当前计算机对象
     */
    public Computer getCurrentComputer() {
        return computer;
    }
}