package cn.geekslife.designpattern.builder;

/**
 * 产品类 - 计算机
 * 表示被构造的复杂对象
 */
public class Computer {
    private String cpu;
    private String memory;
    private String storage;
    private String graphicsCard;
    private String motherboard;
    private String powerSupply;
    private String coolingSystem;
    
    // 默认构造函数
    public Computer() {
    }
    
    // 带必要参数的构造函数
    public Computer(String cpu, String memory, String storage) {
        this.cpu = cpu;
        this.memory = memory;
        this.storage = storage;
    }
    
    // Getter和Setter方法
    public String getCpu() {
        return cpu;
    }
    
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }
    
    public String getMemory() {
        return memory;
    }
    
    public void setMemory(String memory) {
        this.memory = memory;
    }
    
    public String getStorage() {
        return storage;
    }
    
    public void setStorage(String storage) {
        this.storage = storage;
    }
    
    public String getGraphicsCard() {
        return graphicsCard;
    }
    
    public void setGraphicsCard(String graphicsCard) {
        this.graphicsCard = graphicsCard;
    }
    
    public String getMotherboard() {
        return motherboard;
    }
    
    public void setMotherboard(String motherboard) {
        this.motherboard = motherboard;
    }
    
    public String getPowerSupply() {
        return powerSupply;
    }
    
    public void setPowerSupply(String powerSupply) {
        this.powerSupply = powerSupply;
    }
    
    public String getCoolingSystem() {
        return coolingSystem;
    }
    
    public void setCoolingSystem(String coolingSystem) {
        this.coolingSystem = coolingSystem;
    }
    
    @Override
    public String toString() {
        return "Computer{" +
                "cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                ", storage='" + storage + '\'' +
                ", graphicsCard='" + graphicsCard + '\'' +
                ", motherboard='" + motherboard + '\'' +
                ", powerSupply='" + powerSupply + '\'' +
                ", coolingSystem='" + coolingSystem + '\'' +
                '}';
    }
    
    /**
     * 获取计算机配置摘要
     * @return 配置摘要
     */
    public String getConfigurationSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("计算机配置:\n");
        if (cpu != null) sb.append("  CPU: ").append(cpu).append("\n");
        if (memory != null) sb.append("  内存: ").append(memory).append("\n");
        if (storage != null) sb.append("  存储: ").append(storage).append("\n");
        if (graphicsCard != null) sb.append("  显卡: ").append(graphicsCard).append("\n");
        if (motherboard != null) sb.append("  主板: ").append(motherboard).append("\n");
        if (powerSupply != null) sb.append("  电源: ").append(powerSupply).append("\n");
        if (coolingSystem != null) sb.append("  散热: ").append(coolingSystem).append("\n");
        return sb.toString();
    }
}