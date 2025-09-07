package cn.geekslife.designpattern.builder;

/**
 * 建造者模式演示类
 * 展示建造者模式的使用方法
 */
public class BuilderPatternDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 建造者模式演示 ===\n");
        
        // 1. 基本建造者模式
        System.out.println("1. 基本建造者模式:");
        demonstrateBasicBuilder();
        
        // 2. 流式建造者模式
        System.out.println("\n2. 流式建造者模式:");
        demonstrateFluentBuilder();
        
        // 3. 带约束的建造者模式
        System.out.println("\n3. 带约束的建造者模式:");
        demonstrateConstrainedBuilder();
        
        // 4. 指挥者模式
        System.out.println("\n4. 指挥者模式:");
        demonstrateDirector();
        
        // 5. 逐步构建
        System.out.println("\n5. 逐步构建:");
        demonstrateStepByStepBuilding();
    }
    
    /**
     * 演示基本建造者模式
     */
    private static void demonstrateBasicBuilder() {
        ComputerBuilder builder = new ConcreteComputerBuilder();
        
        Computer computer = builder
                .buildCPU("Intel Core i7-12700K")
                .buildMemory("32GB DDR5 RAM")
                .buildStorage("1TB NVMe SSD")
                .buildGraphicsCard("NVIDIA GeForce RTX 3080")
                .build();
        
        System.out.println(computer.getConfigurationSummary());
    }
    
    /**
     * 演示流式建造者模式
     */
    private static void demonstrateFluentBuilder() {
        FluentComputerBuilder fluentBuilder = new FluentComputerBuilder();
        
        // 使用快速构建方法
        Computer basicComputer = fluentBuilder
                .buildBasic("AMD Ryzen 5 5600X", "16GB DDR4 RAM", "512GB SSD")
                .build();
        
        System.out.println("基础计算机配置:");
        System.out.println(basicComputer.getConfigurationSummary());
        
        // 重新使用建造者构建另一个计算机
        Computer highPerformanceComputer = fluentBuilder
                .buildHighPerformance("Intel Core i9-12900K", "64GB DDR5 RAM", "2TB NVMe SSD", "NVIDIA GeForce RTX 4090")
                .build();
        
        System.out.println("高性能计算机配置:");
        System.out.println(highPerformanceComputer.getConfigurationSummary());
    }
    
    /**
     * 演示带约束的建造者模式
     */
    private static void demonstrateConstrainedBuilder() {
        ConstrainedComputerBuilder constrainedBuilder = new ConstrainedComputerBuilder();
        
        try {
            // 尝试在没有设置必要组件的情况下构建（应该抛出异常）
            Computer incompleteComputer = constrainedBuilder.build();
        } catch (IllegalStateException e) {
            System.out.println("预期异常: " + e.getMessage());
        }
        
        // 正确设置必要组件后构建
        Computer completeComputer = constrainedBuilder
                .buildCPU("Intel Core i5-12600K")
                .buildMemory("16GB DDR4 RAM")
                .buildStorage("1TB SSD")
                .build();
        
        System.out.println("完整计算机配置:");
        System.out.println(completeComputer.getConfigurationSummary());
        
        // 检查构建进度
        constrainedBuilder.reset();
        System.out.println("重置后的构建进度:");
        System.out.println(constrainedBuilder.getBuildProgress());
        
        constrainedBuilder.buildCPU("AMD Ryzen 7 5800X");
        System.out.println("设置CPU后的构建进度:");
        System.out.println(constrainedBuilder.getBuildProgress());
    }
    
    /**
     * 演示指挥者模式
     */
    private static void demonstrateDirector() {
        ComputerDirector director = new ComputerDirector();
        
        // 使用具体建造者
        ComputerBuilder builder = new ConcreteComputerBuilder();
        
        // 构建不同类型的计算机
        Computer gamingComputer = director.buildGamingComputer(builder);
        System.out.println(gamingComputer.getConfigurationSummary());
        
        Computer officeComputer = director.buildOfficeComputer(builder);
        System.out.println(officeComputer.getConfigurationSummary());
        
        Computer serverComputer = director.buildServerComputer(builder);
        System.out.println(serverComputer.getConfigurationSummary());
        
        // 构建自定义计算机
        Computer customComputer = director.buildCustomComputer(builder, 
                "Intel Core i7-12700K", "32GB DDR5 RAM", "1TB NVMe SSD");
        System.out.println(customComputer.getConfigurationSummary());
    }
    
    /**
     * 演示逐步构建
     */
    private static void demonstrateStepByStepBuilding() {
        ComputerDirector director = new ComputerDirector();
        ComputerBuilder builder = new ConcreteComputerBuilder();
        
        Computer stepByStepComputer = director.buildComputerStepByStep(builder);
        System.out.println(stepByStepComputer.getConfigurationSummary());
    }
}