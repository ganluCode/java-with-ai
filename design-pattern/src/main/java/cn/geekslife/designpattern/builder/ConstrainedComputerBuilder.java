package cn.geekslife.designpattern.builder;

/**
 * 带约束的建造者实现
 * 确保产品的完整性
 */
public class ConstrainedComputerBuilder implements ComputerBuilder {
    private Computer computer;
    private boolean cpuSet = false;
    private boolean memorySet = false;
    private boolean storageSet = false;
    
    /**
     * 构造函数，初始化产品对象
     */
    public ConstrainedComputerBuilder() {
        this.computer = new Computer();
    }
    
    @Override
    public ConstrainedComputerBuilder buildCPU(String cpu) {
        if (cpu == null || cpu.trim().isEmpty()) {
            throw new IllegalArgumentException("CPU型号不能为空");
        }
        computer.setCpu(cpu);
        cpuSet = true;
        return this;
    }
    
    @Override
    public ConstrainedComputerBuilder buildMemory(String memory) {
        if (memory == null || memory.trim().isEmpty()) {
            throw new IllegalArgumentException("内存规格不能为空");
        }
        computer.setMemory(memory);
        memorySet = true;
        return this;
    }
    
    @Override
    public ConstrainedComputerBuilder buildStorage(String storage) {
        if (storage == null || storage.trim().isEmpty()) {
            throw new IllegalArgumentException("存储规格不能为空");
        }
        computer.setStorage(storage);
        storageSet = true;
        return this;
    }
    
    @Override
    public ConstrainedComputerBuilder buildGraphicsCard(String graphicsCard) {
        computer.setGraphicsCard(graphicsCard);
        return this;
    }
    
    @Override
    public ConstrainedComputerBuilder buildMotherboard(String motherboard) {
        computer.setMotherboard(motherboard);
        return this;
    }
    
    @Override
    public ConstrainedComputerBuilder buildPowerSupply(String powerSupply) {
        computer.setPowerSupply(powerSupply);
        return this;
    }
    
    @Override
    public ConstrainedComputerBuilder buildCoolingSystem(String coolingSystem) {
        computer.setCoolingSystem(coolingSystem);
        return this;
    }
    
    @Override
    public Computer build() {
        // 检查必要组件是否已设置
        if (!cpuSet || !memorySet || !storageSet) {
            throw new IllegalStateException("必须先设置CPU、内存和存储");
        }
        
        Computer result = this.computer;
        reset();
        return result;
    }
    
    @Override
    public ConstrainedComputerBuilder reset() {
        this.computer = new Computer();
        this.cpuSet = false;
        this.memorySet = false;
        this.storageSet = false;
        return this;
    }
    
    /**
     * 验证当前配置是否完整
     * @return 是否完整
     */
    public boolean isComplete() {
        return cpuSet && memorySet && storageSet;
    }
    
    /**
     * 获取构建进度
     * @return 构建进度信息
     */
    public String getBuildProgress() {
        StringBuilder sb = new StringBuilder();
        sb.append("构建进度:\n");
        sb.append("  CPU: ").append(cpuSet ? "✓" : "✗").append("\n");
        sb.append("  内存: ").append(memorySet ? "✓" : "✗").append("\n");
        sb.append("  存储: ").append(storageSet ? "✓" : "✗").append("\n");
        return sb.toString();
    }
}